package com.amazonaws.comprehend

import smithy4s.Enumeration
import smithy4s.Hints
import smithy4s.Schema
import smithy4s.ShapeId
import smithy4s.ShapeTag
import smithy4s.schema.EnumTag
import smithy4s.schema.Schema.enumeration

sealed abstract class SentimentType(_value: String, _name: String, _intValue: Int, _hints: Hints) extends Enumeration.Value {
  override type EnumType = SentimentType
  override val value: String = _value
  override val name: String = _name
  override val intValue: Int = _intValue
  override val hints: Hints = _hints
  override def enumeration: Enumeration[EnumType] = SentimentType
  @inline final def widen: SentimentType = this
}
object SentimentType extends Enumeration[SentimentType] with ShapeTag.Companion[SentimentType] {
  val id: ShapeId = ShapeId("com.amazonaws.comprehend", "SentimentType")

  val hints: Hints = Hints(
    alloy.OpenEnum(),
  ).lazily

  case object POSITIVE extends SentimentType("POSITIVE", "POSITIVE", 0, Hints.empty)
  case object NEGATIVE extends SentimentType("NEGATIVE", "NEGATIVE", 1, Hints.empty)
  case object NEUTRAL extends SentimentType("NEUTRAL", "NEUTRAL", 2, Hints.empty)
  case object MIXED extends SentimentType("MIXED", "MIXED", 3, Hints.empty)
  final case class $Unknown(str: String) extends SentimentType(str, "$Unknown", -1, Hints.empty)

  val $unknown: String => SentimentType = $Unknown(_)

  val values: List[SentimentType] = List(
    POSITIVE,
    NEGATIVE,
    NEUTRAL,
    MIXED,
  )
  val tag: EnumTag[SentimentType] = EnumTag.OpenStringEnum($unknown)
  implicit val schema: Schema[SentimentType] = enumeration(tag, values).withId(id).addHints(hints)
}
