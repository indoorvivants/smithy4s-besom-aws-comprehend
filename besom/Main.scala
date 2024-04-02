//> using scala "3.3.3"
//> using options -Werror -Wunused:all -Wvalue-discard -Wnonunit-statement
//> using dep "org.virtuslab::besom-core:0.2.2"
//> using dep "org.virtuslab::besom-aws:6.23.0-core.0.2"
//> using dep "org.virtuslab::besom-awsx:2.5.0-core.0.2"
//> using dep "com.lihaoyi::upickle::3.2.0"

import besom.*
import besom.api.aws, besom.api.awsx, awsx.ecr, awsx.lb, awsx.ecs
import besom.api.awsx.ecs.inputs.*
import besom.api.awsx.lb.ApplicationLoadBalancerArgs
import besom.api.aws.ecs.inputs.ServiceNetworkConfigurationArgs
import besom.api.aws.cloudwatch.LogGroupArgs
import besom.json.*

@main def main = Pulumi.run {
  val repository =
    ecr.Repository(
      "sentiment-service-repo",
      ecr.RepositoryArgs(forceDelete = true)
    )

  val image = ecr.Image(
    "sentiment-service-image",
    ecr.ImageArgs(
      repositoryUrl = repository.url,
      context = p"../app",
      platform = "linux/amd64"
    )
  )

  val vpc = awsx.ec2.Vpc("sentiment-service-vpc")

  val loadBalancer = lb.ApplicationLoadBalancer(
    "sentiment-service-lb",
    ApplicationLoadBalancerArgs(subnetIds = vpc.publicSubnetIds)
  )

  val cluster = aws.ecs.Cluster("sentiment-service-cluster")

  val logGroup =
    aws.cloudwatch.LogGroup(
      "sentiment-service-log-group",
      LogGroupArgs(retentionInDays = 7, name = "log-group")
    )

  val service =
    image.imageUri.flatMap: image =>
      ecs.FargateService(
        "sentiment-service-fargate",
        ecs.FargateServiceArgs(
          networkConfiguration = ServiceNetworkConfigurationArgs(
            subnets = vpc.publicSubnetIds,
            assignPublicIp = true,
            securityGroups =
              loadBalancer.defaultSecurityGroup.map(_.map(_.id)).map(_.toList)
          ),
          cluster = cluster.arn,
          taskDefinitionArgs = FargateServiceTaskDefinitionArgs(
            containers = Map(
              "sentiment-service" -> TaskDefinitionContainerDefinitionArgs(
                image = image,
                name = "sentiment-service",
                cpu = 128,
                memory = 512,
                essential = true,
                logConfiguration = TaskDefinitionLogConfigurationArgs(
                  logDriver = "awslogs",
                  options = JsObject(
                    "awslogs-group"         -> JsString("log-group"),
                    "awslogs-region"        -> JsString("us-east-1"),
                    "awslogs-stream-prefix" -> JsString("ecs")
                  )
                ),
                portMappings = List(
                  TaskDefinitionPortMappingArgs(
                    targetGroup = loadBalancer.defaultTargetGroup
                  )
                )
              )
            )
          )
        )
      )

  Stack(logGroup).exports(
    image = image.imageUri,
    service = service,
    vpc = vpc.id,
    cluster = cluster.id,
    url = p"http://${loadBalancer.loadBalancer.dnsName}"
  )
}
