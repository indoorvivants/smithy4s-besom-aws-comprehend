//> using scala "3.3.3"
//> using options -Werror -Wunused:all -Wvalue-discard -Wnonunit-statement
//> using dep "org.virtuslab::besom-core:0.2.2"
//> using dep "org.virtuslab::besom-aws:6.23.0-core.0.2"
//> using dep "org.virtuslab::besom-awsx:2.5.0-core.0.2"
//> using dep "com.lihaoyi::upickle::3.2.0"

import besom.*
import besom.api.aws, besom.api.awsx, awsx.ecr, awsx.lb, awsx.ecs
import besom.api.awsx.ecs.inputs.*
import besom.api.aws.ec2.VpcArgs
import besom.api.aws.ec2.SubnetArgs
import besom.api.awsx.lb.ApplicationLoadBalancerArgs
import besom.api.aws.ecs.inputs.ServiceNetworkConfigurationArgs

@main def main = Pulumi.run {
  val repository =
    ecr.Repository("repo", ecr.RepositoryArgs(forceDelete = true))

  val image = ecr.Image(
    "server",
    ecr.ImageArgs(
      repositoryUrl = repository.url,
      context = p"../app",
      platform = "linux/amd64"
    )
  )

  val vpc = aws.ec2.Vpc("main", VpcArgs(cidrBlock = "10.0.0.0/24"))

  val gateway = vpc
    .flatMap(_.id)
    .flatMap: vpc =>
      aws.ec2.InternetGateway(
        "gateway"
      )

  val attachment =
    aws.ec2.InternetGatewayAttachment(
      "attachment",
      aws.ec2.InternetGatewayAttachmentArgs(
        internetGatewayId = gateway.id,
        vpcId = vpc.id
      )
    )

  val subnet1 = aws.ec2.Subnet(
    "subnet1",
    SubnetArgs(
      vpcId = vpc.id,
      cidrBlock = "10.0.0.0/25",
      availabilityZone = "us-east-1a"
    )
  )

  val subnet2 = aws.ec2.Subnet(
    "subnet2",
    SubnetArgs(
      vpcId = vpc.id,
      cidrBlock = "10.0.0.128/25",
      availabilityZone = "us-east-1b"
    )
  )

  val subnetIDs = Output.sequence(List(subnet1, subnet2)).map(_.map(_.id))

  val loadBalancer = lb.ApplicationLoadBalancer(
    "lb",
    ApplicationLoadBalancerArgs(subnetIds = subnetIDs)
  )

  val cluster = aws.ecs.Cluster("cluster")

  val service =
    image.imageUri.flatMap: im =>
      ecs.FargateService(
        "service",
        ecs.FargateServiceArgs(
          networkConfiguration = ServiceNetworkConfigurationArgs(
            subnets = subnetIDs,
            assignPublicIp = true,
            securityGroups =
              loadBalancer.defaultSecurityGroup.map(_.map(_.id)).map(_.toList)
          ),
          cluster = cluster.arn,
          taskDefinitionArgs = FargateServiceTaskDefinitionArgs(
            containers = Map(
              "sentiment" -> TaskDefinitionContainerDefinitionArgs(
                image = im,
                name = "sentiment-service",
                cpu = 128,
                memory = 512,
                essential = true
                // portMappings = List(
                //   TaskDefinitionPortMappingArgs(
                //     containerPort = 8080,
                //     targetGroup = loadBalancer.defaultTargetGroup
                //   )
                // )
              )
            )
          )
        )
      )

  Stack(attachment).exports(
    image = image.imageUri,
    service = service.id,
    vpc = vpc.id,
    cluster = cluster.id,
    url = p"http://${loadBalancer.loadBalancer.dnsName}",
    gw = gateway
  )
}
