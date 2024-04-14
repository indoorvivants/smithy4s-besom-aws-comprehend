# Scala 3 service on AWS with Besom and Smithy4s AWS SDK

This is an accompanying repo for [Besom and Smithy4s on AWS - Scala 3 good vibes only](https://blog.indoorvivants.com/2024-04-14-besom-smithy4s-aws) blogpost.

Run the service (needs AWS credentials configured, like aws-cli): `cd app && sbt reStart`

Deploy the service to AWS (needs Pulumi CLI and AWS credentials): `cd besom && pulumi up -s dev`

This branch uses SBT for service building - there's a pure [scala-cli branch](https://github.com/indoorvivants/smithy4s-besom-aws-comprehend/tree/scala-cli) available.
