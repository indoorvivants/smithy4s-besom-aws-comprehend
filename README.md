# Scala 3 service on AWS with Besom and Smithy4s AWS SDK

This is an accompanying repo for [Besom and Smithy4s on AWS - Scala 3 good vibes only](https://blog.indoorvivants.com/2024-04-14-besom-smithy4s-aws) blogpost.

This branch uses Scala CLI for service building. Go back to main branch if you want to see the SBT version.

Run the service (needs AWS credentials configured, like aws-cli): `cd app && scala-cli run App.scala aws-generated`

Deploy the service to AWS (needs Pulumi CLI and AWS credentials): `cd besom && pulumi up -s dev`

