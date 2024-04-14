package com.amazonaws.comprehend

import smithy4s.Enumeration
import smithy4s.Hints
import smithy4s.Schema
import smithy4s.ShapeId
import smithy4s.ShapeTag
import smithy4s.schema.EnumTag
import smithy4s.schema.Schema.enumeration

sealed abstract class InvalidRequestDetailReason(_value: String, _name: String, _intValue: Int, _hints: Hints) extends Enumeration.Value {
  override type EnumType = InvalidRequestDetailReason
  override val value: String = _value
  override val name: String = _name
  override val intValue: Int = _intValue
  override val hints: Hints = _hints
  override def enumeration: Enumeration[EnumType] = InvalidRequestDetailReason
  @inline final def widen: InvalidRequestDetailReason = this
}
object InvalidRequestDetailReason extends Enumeration[InvalidRequestDetailReason] with ShapeTag.Companion[InvalidRequestDetailReason] {
  val id: ShapeId = ShapeId("com.amazonaws.comprehend", "InvalidRequestDetailReason")

  val hints: Hints = Hints(
    alloy.OpenEnum(),
  ).lazily

  case object DOCUMENT_SIZE_EXCEEDED extends InvalidRequestDetailReason("DOCUMENT_SIZE_EXCEEDED", "DOCUMENT_SIZE_EXCEEDED", 0, Hints.empty)
  case object UNSUPPORTED_DOC_TYPE extends InvalidRequestDetailReason("UNSUPPORTED_DOC_TYPE", "UNSUPPORTED_DOC_TYPE", 1, Hints.empty)
  case object PAGE_LIMIT_EXCEEDED extends InvalidRequestDetailReason("PAGE_LIMIT_EXCEEDED", "PAGE_LIMIT_EXCEEDED", 2, Hints.empty)
  case object TEXTRACT_ACCESS_DENIED extends InvalidRequestDetailReason("TEXTRACT_ACCESS_DENIED", "TEXTRACT_ACCESS_DENIED", 3, Hints.empty)
  final case class $Unknown(str: String) extends InvalidRequestDetailReason(str, "$Unknown", -1, Hints.empty)

  val $unknown: String => InvalidRequestDetailReason = $Unknown(_)

  val values: List[InvalidRequestDetailReason] = List(
    DOCUMENT_SIZE_EXCEEDED,
    UNSUPPORTED_DOC_TYPE,
    PAGE_LIMIT_EXCEEDED,
    TEXTRACT_ACCESS_DENIED,
  )
  val tag: EnumTag[InvalidRequestDetailReason] = EnumTag.OpenStringEnum($unknown)
  implicit val schema: Schema[InvalidRequestDetailReason] = enumeration(tag, values).withId(id).addHints(hints)
}
