//> using scala "3.3.3"
//> using options -Werror -Wunused:all -Wvalue-discard -Wnonunit-statement
//> using dep "org.virtuslab::besom-core:0.2.2"
//> using dep "org.virtuslab::besom-aws:6.23.0-core.0.2"
//> using dep "org.virtuslab::besom-awsx:2.5.0-core.0.2"

import besom.*
import besom.api.aws
import besom.api.aws.cloudwatch.LogGroupArgs
import besom.api.aws.ecs.inputs.ServiceLoadBalancerArgs
import besom.api.aws.ecs.inputs.ServiceNetworkConfigurationArgs
import besom.api.aws.iam.RolePolicyAttachmentArgs
import besom.api.awsx
import besom.api.awsx.awsx.inputs.DefaultRoleWithPolicyArgs
import besom.api.awsx.ecs.FargateTaskDefinitionArgs
import besom.api.awsx.ecs.inputs.*
import besom.api.awsx.lb.ApplicationLoadBalancerArgs
import besom.json.*

import awsx.ecr
import awsx.lb
import awsx.ecs

@main def main =
  Pulumi.run:
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
        LogGroupArgs(retentionInDays = 7, name = "sentiment-service-logs")
      )

    val task = awsx.ecs.FargateTaskDefinition(
      "sentiment-service-task",
      FargateTaskDefinitionArgs(
        containers = Map(
          "sentiment-service" -> TaskDefinitionContainerDefinitionArgs(
            image = image.imageUri,
            name = "sentiment-service",
            cpu = 128,
            memory = 512,
            essential = true,
            logConfiguration = TaskDefinitionLogConfigurationArgs(
              logDriver = "awslogs",
              options = JsObject(
                "awslogs-group"         -> JsString("sentiment-service-logs"),
                "awslogs-region"        -> JsString("us-east-1"),
                "awslogs-stream-prefix" -> JsString("ecs")
              )
            ),
            portMappings = List(
              TaskDefinitionPortMappingArgs(
                containerPort = 80
              )
            )
          )
        )
      )
    )

    val policyAttachment = aws.iam.RolePolicyAttachment(
      "sentiment-service-task-comprehend-policy",
      RolePolicyAttachmentArgs(
        role = task.taskRole.map(_.get), // TODO
        policyArn = "arn:aws:iam::aws:policy/ComprehendReadOnly"
      )
    )

    val service = aws.ecs.Service(
      "sentiment-service",
      aws.ecs.ServiceArgs(
        launchType = "FARGATE",
        taskDefinition = task.taskDefinition.arn,
        cluster = cluster.arn,
        desiredCount = 1,
        networkConfiguration = ServiceNetworkConfigurationArgs(
          subnets = vpc.publicSubnetIds,
          assignPublicIp = true,
          securityGroups =
            loadBalancer.defaultSecurityGroup.map(_.map(_.id)).map(_.toList)
        ),
        loadBalancers = List(
          ServiceLoadBalancerArgs(
            containerName = "sentiment-service",
            containerPort = 80,
            targetGroupArn = loadBalancer.defaultTargetGroup.arn
          )
        )
      )
    )

    Stack(logGroup, service, cluster, policyAttachment).exports(
      image = image.imageUri,
      url = p"http://${loadBalancer.loadBalancer.dnsName}"
    )
