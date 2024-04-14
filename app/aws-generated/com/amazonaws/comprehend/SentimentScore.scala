package com.amazonaws.comprehend

import smithy4s.Hints
import smithy4s.Schema
import smithy4s.ShapeId
import smithy4s.ShapeTag
import smithy4s.schema.Schema.float
import smithy4s.schema.Schema.struct

/** <p>Describes the level of confidence that Amazon Comprehend has in the accuracy of its
  *       detection of sentiments.</p>
  * @param Positive
  *   <p>The level of confidence that Amazon Comprehend has in the accuracy of its detection of
  *         the <code>POSITIVE</code> sentiment.</p>
  * @param Negative
  *   <p>The level of confidence that Amazon Comprehend has in the accuracy of its detection of
  *         the <code>NEGATIVE</code> sentiment.</p>
  * @param Neutral
  *   <p>The level of confidence that Amazon Comprehend has in the accuracy of its detection of
  *         the <code>NEUTRAL</code> sentiment.</p>
  * @param Mixed
  *   <p>The level of confidence that Amazon Comprehend has in the accuracy of its detection of
  *         the <code>MIXED</code> sentiment.</p>
  */
final case class SentimentScore(positive: Option[Float] = None, negative: Option[Float] = None, neutral: Option[Float] = None, mixed: Option[Float] = None)

object SentimentScore extends ShapeTag.Companion[SentimentScore] {
  val id: ShapeId = ShapeId("com.amazonaws.comprehend", "SentimentScore")

  val hints: Hints = Hints(
    smithy.api.Documentation("<p>Describes the level of confidence that Amazon Comprehend has in the accuracy of its\n      detection of sentiments.</p>"),
  ).lazily

  // constructor using the original order from the spec
  private def make(positive: Option[Float], negative: Option[Float], neutral: Option[Float], mixed: Option[Float]): SentimentScore = SentimentScore(positive, negative, neutral, mixed)

  implicit val schema: Schema[SentimentScore] = struct(
    float.optional[SentimentScore]("Positive", _.positive).addHints(smithy.api.Documentation("<p>The level of confidence that Amazon Comprehend has in the accuracy of its detection of\n      the <code>POSITIVE</code> sentiment.</p>")),
    float.optional[SentimentScore]("Negative", _.negative).addHints(smithy.api.Documentation("<p>The level of confidence that Amazon Comprehend has in the accuracy of its detection of\n      the <code>NEGATIVE</code> sentiment.</p>")),
    float.optional[SentimentScore]("Neutral", _.neutral).addHints(smithy.api.Documentation("<p>The level of confidence that Amazon Comprehend has in the accuracy of its detection of\n      the <code>NEUTRAL</code> sentiment.</p>")),
    float.optional[SentimentScore]("Mixed", _.mixed).addHints(smithy.api.Documentation("<p>The level of confidence that Amazon Comprehend has in the accuracy of its detection of\n      the <code>MIXED</code> sentiment.</p>")),
  )(make).withId(id).addHints(hints)
}
