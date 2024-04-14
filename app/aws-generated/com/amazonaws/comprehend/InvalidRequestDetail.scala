package com.amazonaws.comprehend

import smithy4s.Hints
import smithy4s.Schema
import smithy4s.ShapeId
import smithy4s.ShapeTag
import smithy4s.schema.Schema.struct

/** <p>Provides additional detail about why the request failed:</p>
  *          <ul>
  *             <li>
  *                <p>Document size is too large - Check the size of your file and resubmit the request.</p>
  *             </li>
  *             <li>
  *                <p>Document type is not supported - Check the file type and resubmit the request.</p>
  *             </li>
  *             <li>
  *                <p>Too many pages in the document - Check the number of pages in your file and resubmit the request.</p>
  *             </li>
  *             <li>
  *                <p>Access denied to Amazon Textract - Verify that your account has permission to use Amazon Textract API operations and resubmit the request.</p>
  *             </li>
  *          </ul>
  * @param Reason
  *   <p>Reason code is <code>INVALID_DOCUMENT</code>.</p>
  */
final case class InvalidRequestDetail(reason: Option[InvalidRequestDetailReason] = None)

object InvalidRequestDetail extends ShapeTag.Companion[InvalidRequestDetail] {
  val id: ShapeId = ShapeId("com.amazonaws.comprehend", "InvalidRequestDetail")

  val hints: Hints = Hints(
    smithy.api.Documentation("<p>Provides additional detail about why the request failed:</p>\n         <ul>\n            <li>\n               <p>Document size is too large - Check the size of your file and resubmit the request.</p>\n            </li>\n            <li>\n               <p>Document type is not supported - Check the file type and resubmit the request.</p>\n            </li>\n            <li>\n               <p>Too many pages in the document - Check the number of pages in your file and resubmit the request.</p>\n            </li>\n            <li>\n               <p>Access denied to Amazon Textract - Verify that your account has permission to use Amazon Textract API operations and resubmit the request.</p>\n            </li>\n         </ul>"),
  ).lazily

  // constructor using the original order from the spec
  private def make(reason: Option[InvalidRequestDetailReason]): InvalidRequestDetail = InvalidRequestDetail(reason)

  implicit val schema: Schema[InvalidRequestDetail] = struct(
    InvalidRequestDetailReason.schema.optional[InvalidRequestDetail]("Reason", _.reason).addHints(smithy.api.Documentation("<p>Reason code is <code>INVALID_DOCUMENT</code>.</p>")),
  )(make).withId(id).addHints(hints)
}
