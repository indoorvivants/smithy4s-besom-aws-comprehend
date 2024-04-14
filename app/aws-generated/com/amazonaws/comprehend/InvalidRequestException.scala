package com.amazonaws.comprehend

import smithy4s.Hints
import smithy4s.Schema
import smithy4s.ShapeId
import smithy4s.ShapeTag
import smithy4s.Smithy4sThrowable
import smithy4s.schema.Schema.string
import smithy4s.schema.Schema.struct

/** <p>The request is invalid.</p>
  * @param Detail
  *   <p>Provides additional detail about why the request failed:</p>
  *            <ul>
  *               <li>
  *                  <p>Document size is too large - Check the size of your file and resubmit the request.</p>
  *               </li>
  *               <li>
  *                  <p>Document type is not supported - Check the file type and resubmit the request.</p>
  *               </li>
  *               <li>
  *                  <p>Too many pages in the document - Check the number of pages in your file and resubmit the request.</p>
  *               </li>
  *               <li>
  *                  <p>Access denied to Amazon Textract - Verify that your account has permission to use Amazon Textract API operations and resubmit the request.</p>
  *               </li>
  *            </ul>
  */
final case class InvalidRequestException(message: Option[String] = None, reason: Option[InvalidRequestReason] = None, detail: Option[InvalidRequestDetail] = None) extends Smithy4sThrowable {
  override def getMessage(): String = message.orNull
}

object InvalidRequestException extends ShapeTag.Companion[InvalidRequestException] {
  val id: ShapeId = ShapeId("com.amazonaws.comprehend", "InvalidRequestException")

  val hints: Hints = Hints(
    smithy.api.Error.CLIENT.widen,
    smithy.api.Documentation("<p>The request is invalid.</p>"),
    smithy.api.HttpError(400),
  ).lazily

  // constructor using the original order from the spec
  private def make(message: Option[String], reason: Option[InvalidRequestReason], detail: Option[InvalidRequestDetail]): InvalidRequestException = InvalidRequestException(message, reason, detail)

  implicit val schema: Schema[InvalidRequestException] = struct(
    string.optional[InvalidRequestException]("Message", _.message),
    InvalidRequestReason.schema.optional[InvalidRequestException]("Reason", _.reason),
    InvalidRequestDetail.schema.optional[InvalidRequestException]("Detail", _.detail),
  )(make).withId(id).addHints(hints)
}
