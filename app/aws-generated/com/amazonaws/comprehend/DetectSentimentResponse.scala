package com.amazonaws.comprehend

import smithy4s.Hints
import smithy4s.Schema
import smithy4s.ShapeId
import smithy4s.ShapeTag
import smithy4s.schema.Schema.struct

/** @param Sentiment
  *   <p>The inferred sentiment that Amazon Comprehend has the highest level of confidence
  *         in.</p>
  * @param SentimentScore
  *   <p>An object that lists the sentiments, and their corresponding confidence
  *         levels.</p>
  */
final case class DetectSentimentResponse(sentiment: Option[SentimentType] = None, sentimentScore: Option[SentimentScore] = None)

object DetectSentimentResponse extends ShapeTag.Companion[DetectSentimentResponse] {
  val id: ShapeId = ShapeId("com.amazonaws.comprehend", "DetectSentimentResponse")

  val hints: Hints = Hints(
    smithy.api.Output(),
    smithy.api.Sensitive(),
  ).lazily

  // constructor using the original order from the spec
  private def make(sentiment: Option[SentimentType], sentimentScore: Option[SentimentScore]): DetectSentimentResponse = DetectSentimentResponse(sentiment, sentimentScore)

  implicit val schema: Schema[DetectSentimentResponse] = struct(
    SentimentType.schema.optional[DetectSentimentResponse]("Sentiment", _.sentiment).addHints(smithy.api.Documentation("<p>The inferred sentiment that Amazon Comprehend has the highest level of confidence\n      in.</p>")),
    SentimentScore.schema.optional[DetectSentimentResponse]("SentimentScore", _.sentimentScore).addHints(smithy.api.Documentation("<p>An object that lists the sentiments, and their corresponding confidence\n      levels.</p>")),
  )(make).withId(id).addHints(hints)
}
