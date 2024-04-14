package com.amazonaws.comprehend

import smithy4s.Hints
import smithy4s.Schema
import smithy4s.ShapeId
import smithy4s.ShapeTag
import smithy4s.Smithy4sThrowable
import smithy4s.schema.Schema.string
import smithy4s.schema.Schema.struct

/** <p>Amazon Comprehend can't process the language of the input text. For custom entity
  *       recognition APIs, only English, Spanish, French, Italian, German, or Portuguese are accepted.
  *       For a list of supported languages,
  *       <a href="https://docs.aws.amazon.com/comprehend/latest/dg/supported-languages.html">Supported languages</a> in the Comprehend Developer Guide.
  *     </p>
  */
final case class UnsupportedLanguageException(message: Option[String] = None) extends Smithy4sThrowable {
  override def getMessage(): String = message.orNull
}

object UnsupportedLanguageException extends ShapeTag.Companion[UnsupportedLanguageException] {
  val id: ShapeId = ShapeId("com.amazonaws.comprehend", "UnsupportedLanguageException")

  val hints: Hints = Hints(
    smithy.api.Error.CLIENT.widen,
    smithy.api.Documentation("<p>Amazon Comprehend can\'t process the language of the input text. For custom entity\n      recognition APIs, only English, Spanish, French, Italian, German, or Portuguese are accepted.\n      For a list of supported languages,\n      <a href=\"https://docs.aws.amazon.com/comprehend/latest/dg/supported-languages.html\">Supported languages</a> in the Comprehend Developer Guide.\n    </p>"),
    smithy.api.HttpError(400),
  ).lazily

  // constructor using the original order from the spec
  private def make(message: Option[String]): UnsupportedLanguageException = UnsupportedLanguageException(message)

  implicit val schema: Schema[UnsupportedLanguageException] = struct(
    string.optional[UnsupportedLanguageException]("Message", _.message),
  )(make).withId(id).addHints(hints)
}
