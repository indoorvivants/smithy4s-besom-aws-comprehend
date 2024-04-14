package com.amazonaws.comprehend

import smithy4s.Enumeration
import smithy4s.Hints
import smithy4s.Schema
import smithy4s.ShapeId
import smithy4s.ShapeTag
import smithy4s.schema.EnumTag
import smithy4s.schema.Schema.enumeration

sealed abstract class LanguageCode(_value: String, _name: String, _intValue: Int, _hints: Hints) extends Enumeration.Value {
  override type EnumType = LanguageCode
  override val value: String = _value
  override val name: String = _name
  override val intValue: Int = _intValue
  override val hints: Hints = _hints
  override def enumeration: Enumeration[EnumType] = LanguageCode
  @inline final def widen: LanguageCode = this
}
object LanguageCode extends Enumeration[LanguageCode] with ShapeTag.Companion[LanguageCode] {
  val id: ShapeId = ShapeId("com.amazonaws.comprehend", "LanguageCode")

  val hints: Hints = Hints(
    alloy.OpenEnum(),
  ).lazily

  case object EN extends LanguageCode("en", "EN", 0, Hints.empty)
  case object ES extends LanguageCode("es", "ES", 1, Hints.empty)
  case object FR extends LanguageCode("fr", "FR", 2, Hints.empty)
  case object DE extends LanguageCode("de", "DE", 3, Hints.empty)
  case object IT extends LanguageCode("it", "IT", 4, Hints.empty)
  case object PT extends LanguageCode("pt", "PT", 5, Hints.empty)
  case object AR extends LanguageCode("ar", "AR", 6, Hints.empty)
  case object HI extends LanguageCode("hi", "HI", 7, Hints.empty)
  case object JA extends LanguageCode("ja", "JA", 8, Hints.empty)
  case object KO extends LanguageCode("ko", "KO", 9, Hints.empty)
  case object ZH extends LanguageCode("zh", "ZH", 10, Hints.empty)
  case object ZH_TW extends LanguageCode("zh-TW", "ZH_TW", 11, Hints.empty)
  final case class $Unknown(str: String) extends LanguageCode(str, "$Unknown", -1, Hints.empty)

  val $unknown: String => LanguageCode = $Unknown(_)

  val values: List[LanguageCode] = List(
    EN,
    ES,
    FR,
    DE,
    IT,
    PT,
    AR,
    HI,
    JA,
    KO,
    ZH,
    ZH_TW,
  )
  val tag: EnumTag[LanguageCode] = EnumTag.OpenStringEnum($unknown)
  implicit val schema: Schema[LanguageCode] = enumeration(tag, values).withId(id).addHints(hints)
}
