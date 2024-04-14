.PHONY: generate assembly 

generate:
	smithy4s generate --dependencies com.disneystreaming.smithy\:aws-comprehend-spec\:2023.09.22 only.smithy --output aws-generated/

assembly:
	scala-cli --power package --assembly App.scala aws-generated -f -o ./assembly
