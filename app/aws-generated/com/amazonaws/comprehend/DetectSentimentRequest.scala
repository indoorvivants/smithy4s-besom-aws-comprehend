package com.amazonaws.comprehend

import smithy4s.Hints
import smithy4s.Schema
import smithy4s.ShapeId
import smithy4s.ShapeTag
import smithy4s.schema.Schema.struct

/** @param Text
  *   <p>A UTF-8 text string. The maximum string size is 5 KB.</p>
  * @param LanguageCode
  *   <p>The language of the input documents. You can specify any of the primary languages
  *         supported by Amazon Comprehend. All documents must be in the same language.</p>
  */
final case class DetectSentimentRequest(text: CustomerInputString, languageCode: LanguageCode)

object DetectSentimentRequest extends ShapeTag.Companion[DetectSentimentRequest] {
  val id: ShapeId = ShapeId("com.amazonaws.comprehend", "DetectSentimentRequest")

  val hints: Hints = Hints(
    smithy.api.Input(),
  ).lazily

  // constructor using the original order from the spec
  private def make(text: CustomerInputString, languageCode: LanguageCode): DetectSentimentRequest = DetectSentimentRequest(text, languageCode)

  implicit val schema: Schema[DetectSentimentRequest] = struct(
    CustomerInputString.schema.required[DetectSentimentRequest]("Text", _.text).addHints(smithy.api.Documentation("<p>A UTF-8 text string. The maximum string size is 5 KB.</p>")),
    LanguageCode.schema.required[DetectSentimentRequest]("LanguageCode", _.languageCode).addHints(smithy.api.Documentation("<p>The language of the input documents. You can specify any of the primary languages\n      supported by Amazon Comprehend. All documents must be in the same language.</p>")),
  )(make).withId(id).addHints(hints)
}
