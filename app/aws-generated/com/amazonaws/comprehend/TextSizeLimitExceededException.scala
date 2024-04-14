package com.amazonaws.comprehend

import smithy4s.Hints
import smithy4s.Schema
import smithy4s.ShapeId
import smithy4s.ShapeTag
import smithy4s.Smithy4sThrowable
import smithy4s.schema.Schema.string
import smithy4s.schema.Schema.struct

/** <p>The size of the input text exceeds the limit. Use a smaller document.</p> */
final case class TextSizeLimitExceededException(message: Option[String] = None) extends Smithy4sThrowable {
  override def getMessage(): String = message.orNull
}

object TextSizeLimitExceededException extends ShapeTag.Companion[TextSizeLimitExceededException] {
  val id: ShapeId = ShapeId("com.amazonaws.comprehend", "TextSizeLimitExceededException")

  val hints: Hints = Hints(
    smithy.api.Error.CLIENT.widen,
    smithy.api.Documentation("<p>The size of the input text exceeds the limit. Use a smaller document.</p>"),
    smithy.api.HttpError(400),
  ).lazily

  // constructor using the original order from the spec
  private def make(message: Option[String]): TextSizeLimitExceededException = TextSizeLimitExceededException(message)

  implicit val schema: Schema[TextSizeLimitExceededException] = struct(
    string.optional[TextSizeLimitExceededException]("Message", _.message),
  )(make).withId(id).addHints(hints)
}
