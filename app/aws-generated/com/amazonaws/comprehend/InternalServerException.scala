package com.amazonaws.comprehend

import smithy4s.Hints
import smithy4s.Schema
import smithy4s.ShapeId
import smithy4s.ShapeTag
import smithy4s.Smithy4sThrowable
import smithy4s.schema.Schema.string
import smithy4s.schema.Schema.struct

/** <p>An internal server error occurred. Retry your request.</p> */
final case class InternalServerException(message: Option[String] = None) extends Smithy4sThrowable {
  override def getMessage(): String = message.orNull
}

object InternalServerException extends ShapeTag.Companion[InternalServerException] {
  val id: ShapeId = ShapeId("com.amazonaws.comprehend", "InternalServerException")

  val hints: Hints = Hints(
    smithy.api.Error.SERVER.widen,
    smithy.api.Documentation("<p>An internal server error occurred. Retry your request.</p>"),
    smithy.api.HttpError(500),
  ).lazily

  // constructor using the original order from the spec
  private def make(message: Option[String]): InternalServerException = InternalServerException(message)

  implicit val schema: Schema[InternalServerException] = struct(
    string.optional[InternalServerException]("Message", _.message),
  )(make).withId(id).addHints(hints)
}
