package com.amazonaws.comprehend

import smithy4s.Enumeration
import smithy4s.Hints
import smithy4s.Schema
import smithy4s.ShapeId
import smithy4s.ShapeTag
import smithy4s.schema.EnumTag
import smithy4s.schema.Schema.enumeration

sealed abstract class InvalidRequestReason(_value: String, _name: String, _intValue: Int, _hints: Hints) extends Enumeration.Value {
  override type EnumType = InvalidRequestReason
  override val value: String = _value
  override val name: String = _name
  override val intValue: Int = _intValue
  override val hints: Hints = _hints
  override def enumeration: Enumeration[EnumType] = InvalidRequestReason
  @inline final def widen: InvalidRequestReason = this
}
object InvalidRequestReason extends Enumeration[InvalidRequestReason] with ShapeTag.Companion[InvalidRequestReason] {
  val id: ShapeId = ShapeId("com.amazonaws.comprehend", "InvalidRequestReason")

  val hints: Hints = Hints(
    alloy.OpenEnum(),
  ).lazily

  case object INVALID_DOCUMENT extends InvalidRequestReason("INVALID_DOCUMENT", "INVALID_DOCUMENT", 0, Hints.empty)
  final case class $Unknown(str: String) extends InvalidRequestReason(str, "$Unknown", -1, Hints.empty)

  val $unknown: String => InvalidRequestReason = $Unknown(_)

  val values: List[InvalidRequestReason] = List(
    INVALID_DOCUMENT,
  )
  val tag: EnumTag[InvalidRequestReason] = EnumTag.OpenStringEnum($unknown)
  implicit val schema: Schema[InvalidRequestReason] = enumeration(tag, values).withId(id).addHints(hints)
}
