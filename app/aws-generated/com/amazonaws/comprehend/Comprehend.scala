package com.amazonaws.comprehend

import smithy4s.Endpoint
import smithy4s.Hints
import smithy4s.Schema
import smithy4s.Service
import smithy4s.ShapeId
import smithy4s.Transformation
import smithy4s.kinds.PolyFunction5
import smithy4s.kinds.toPolyFunction5.const5
import smithy4s.schema.ErrorSchema
import smithy4s.schema.OperationSchema
import smithy4s.schema.Schema.bijection
import smithy4s.schema.Schema.union

/** <p>Amazon Comprehend is an Amazon Web Services service for gaining insight into the content of documents.
  *       Use these actions to determine the topics contained in your documents, the topics they
  *       discuss, the predominant sentiment expressed in them, the predominant language used, and
  *       more.</p>
  */
trait ComprehendGen[F[_, _, _, _, _]] {
  self =>

  /** <p>Inspects text and returns an inference of the prevailing sentiment
    *         (<code>POSITIVE</code>, <code>NEUTRAL</code>, <code>MIXED</code>, or <code>NEGATIVE</code>). </p>
    * @param Text
    *   <p>A UTF-8 text string. The maximum string size is 5 KB.</p>
    * @param LanguageCode
    *   <p>The language of the input documents. You can specify any of the primary languages
    *         supported by Amazon Comprehend. All documents must be in the same language.</p>
    */
  def detectSentiment(text: CustomerInputString, languageCode: LanguageCode): F[DetectSentimentRequest, ComprehendOperation.DetectSentimentError, DetectSentimentResponse, Nothing, Nothing]

  def transform: Transformation.PartiallyApplied[ComprehendGen[F]] = Transformation.of[ComprehendGen[F]](this)
}

object ComprehendGen extends Service.Mixin[ComprehendGen, ComprehendOperation] {

  val id: ShapeId = ShapeId("com.amazonaws.comprehend", "Comprehend_20171127")
  val version: String = "2017-11-27"

  val hints: Hints = Hints(
    aws.auth.Sigv4(name = "comprehend"),
    smithy.api.Title("Amazon Comprehend"),
    aws.protocols.AwsJson1_1(http = None, eventStreamHttp = None),
    smithy.api.Documentation("<p>Amazon Comprehend is an Amazon Web Services service for gaining insight into the content of documents.\n      Use these actions to determine the topics contained in your documents, the topics they\n      discuss, the predominant sentiment expressed in them, the predominant language used, and\n      more.</p>"),
    aws.api.Service(sdkId = "Comprehend", arnNamespace = Some(aws.api.ArnNamespace("comprehend")), cloudFormationName = Some(aws.api.CloudFormationName("Comprehend")), cloudTrailEventSource = Some("comprehend.amazonaws.com"), endpointPrefix = Some("comprehend")),
  ).lazily

  def apply[F[_]](implicit F: Impl[F]): F.type = F

  object ErrorAware {
    def apply[F[_, _]](implicit F: ErrorAware[F]): F.type = F
    type Default[F[+_, +_]] = Constant[smithy4s.kinds.stubs.Kind2[F]#toKind5]
  }

  val endpoints: Vector[smithy4s.Endpoint[ComprehendOperation, _, _, _, _, _]] = Vector(
    ComprehendOperation.DetectSentiment,
  )

  def input[I, E, O, SI, SO](op: ComprehendOperation[I, E, O, SI, SO]): I = op.input
  def ordinal[I, E, O, SI, SO](op: ComprehendOperation[I, E, O, SI, SO]): Int = op.ordinal
  override def endpoint[I, E, O, SI, SO](op: ComprehendOperation[I, E, O, SI, SO]) = op.endpoint
  class Constant[P[-_, +_, +_, +_, +_]](value: P[Any, Nothing, Nothing, Nothing, Nothing]) extends ComprehendOperation.Transformed[ComprehendOperation, P](reified, const5(value))
  type Default[F[+_]] = Constant[smithy4s.kinds.stubs.Kind1[F]#toKind5]
  def reified: ComprehendGen[ComprehendOperation] = ComprehendOperation.reified
  def mapK5[P[_, _, _, _, _], P1[_, _, _, _, _]](alg: ComprehendGen[P], f: PolyFunction5[P, P1]): ComprehendGen[P1] = new ComprehendOperation.Transformed(alg, f)
  def fromPolyFunction[P[_, _, _, _, _]](f: PolyFunction5[ComprehendOperation, P]): ComprehendGen[P] = new ComprehendOperation.Transformed(reified, f)
  def toPolyFunction[P[_, _, _, _, _]](impl: ComprehendGen[P]): PolyFunction5[ComprehendOperation, P] = ComprehendOperation.toPolyFunction(impl)

  type DetectSentimentError = ComprehendOperation.DetectSentimentError
  val DetectSentimentError = ComprehendOperation.DetectSentimentError
}

sealed trait ComprehendOperation[Input, Err, Output, StreamedInput, StreamedOutput] {
  def run[F[_, _, _, _, _]](impl: ComprehendGen[F]): F[Input, Err, Output, StreamedInput, StreamedOutput]
  def ordinal: Int
  def input: Input
  def endpoint: Endpoint[ComprehendOperation, Input, Err, Output, StreamedInput, StreamedOutput]
}

object ComprehendOperation {

  object reified extends ComprehendGen[ComprehendOperation] {
    def detectSentiment(text: CustomerInputString, languageCode: LanguageCode): DetectSentiment = DetectSentiment(DetectSentimentRequest(text, languageCode))
  }
  class Transformed[P[_, _, _, _, _], P1[_ ,_ ,_ ,_ ,_]](alg: ComprehendGen[P], f: PolyFunction5[P, P1]) extends ComprehendGen[P1] {
    def detectSentiment(text: CustomerInputString, languageCode: LanguageCode): P1[DetectSentimentRequest, ComprehendOperation.DetectSentimentError, DetectSentimentResponse, Nothing, Nothing] = f[DetectSentimentRequest, ComprehendOperation.DetectSentimentError, DetectSentimentResponse, Nothing, Nothing](alg.detectSentiment(text, languageCode))
  }

  def toPolyFunction[P[_, _, _, _, _]](impl: ComprehendGen[P]): PolyFunction5[ComprehendOperation, P] = new PolyFunction5[ComprehendOperation, P] {
    def apply[I, E, O, SI, SO](op: ComprehendOperation[I, E, O, SI, SO]): P[I, E, O, SI, SO] = op.run(impl) 
  }
  final case class DetectSentiment(input: DetectSentimentRequest) extends ComprehendOperation[DetectSentimentRequest, ComprehendOperation.DetectSentimentError, DetectSentimentResponse, Nothing, Nothing] {
    def run[F[_, _, _, _, _]](impl: ComprehendGen[F]): F[DetectSentimentRequest, ComprehendOperation.DetectSentimentError, DetectSentimentResponse, Nothing, Nothing] = impl.detectSentiment(input.text, input.languageCode)
    def ordinal: Int = 0
    def endpoint: smithy4s.Endpoint[ComprehendOperation,DetectSentimentRequest, ComprehendOperation.DetectSentimentError, DetectSentimentResponse, Nothing, Nothing] = DetectSentiment
  }
  object DetectSentiment extends smithy4s.Endpoint[ComprehendOperation,DetectSentimentRequest, ComprehendOperation.DetectSentimentError, DetectSentimentResponse, Nothing, Nothing] {
    val schema: OperationSchema[DetectSentimentRequest, ComprehendOperation.DetectSentimentError, DetectSentimentResponse, Nothing, Nothing] = Schema.operation(ShapeId("com.amazonaws.comprehend", "DetectSentiment"))
      .withInput(DetectSentimentRequest.schema)
      .withError(DetectSentimentError.errorSchema)
      .withOutput(DetectSentimentResponse.schema)
      .withHints(smithy.api.Documentation("<p>Inspects text and returns an inference of the prevailing sentiment\n        (<code>POSITIVE</code>, <code>NEUTRAL</code>, <code>MIXED</code>, or <code>NEGATIVE</code>). </p>"))
    def wrap(input: DetectSentimentRequest): DetectSentiment = DetectSentiment(input)
  }
  sealed trait DetectSentimentError extends scala.Product with scala.Serializable { self =>
    @inline final def widen: DetectSentimentError = this
    def $ordinal: Int

    object project {
      def internalServerException: Option[InternalServerException] = DetectSentimentError.InternalServerExceptionCase.alt.project.lift(self).map(_.internalServerException)
      def unsupportedLanguageException: Option[UnsupportedLanguageException] = DetectSentimentError.UnsupportedLanguageExceptionCase.alt.project.lift(self).map(_.unsupportedLanguageException)
      def textSizeLimitExceededException: Option[TextSizeLimitExceededException] = DetectSentimentError.TextSizeLimitExceededExceptionCase.alt.project.lift(self).map(_.textSizeLimitExceededException)
      def invalidRequestException: Option[InvalidRequestException] = DetectSentimentError.InvalidRequestExceptionCase.alt.project.lift(self).map(_.invalidRequestException)
    }

    def accept[A](visitor: DetectSentimentError.Visitor[A]): A = this match {
      case value: DetectSentimentError.InternalServerExceptionCase => visitor.internalServerException(value.internalServerException)
      case value: DetectSentimentError.UnsupportedLanguageExceptionCase => visitor.unsupportedLanguageException(value.unsupportedLanguageException)
      case value: DetectSentimentError.TextSizeLimitExceededExceptionCase => visitor.textSizeLimitExceededException(value.textSizeLimitExceededException)
      case value: DetectSentimentError.InvalidRequestExceptionCase => visitor.invalidRequestException(value.invalidRequestException)
    }
  }
  object DetectSentimentError extends ErrorSchema.Companion[DetectSentimentError] {

    def internalServerException(internalServerException: InternalServerException): DetectSentimentError = InternalServerExceptionCase(internalServerException)
    def unsupportedLanguageException(unsupportedLanguageException: UnsupportedLanguageException): DetectSentimentError = UnsupportedLanguageExceptionCase(unsupportedLanguageException)
    def textSizeLimitExceededException(textSizeLimitExceededException: TextSizeLimitExceededException): DetectSentimentError = TextSizeLimitExceededExceptionCase(textSizeLimitExceededException)
    def invalidRequestException(invalidRequestException: InvalidRequestException): DetectSentimentError = InvalidRequestExceptionCase(invalidRequestException)

    val id: ShapeId = ShapeId("com.amazonaws.comprehend", "DetectSentimentError")

    val hints: Hints = Hints.empty

    final case class InternalServerExceptionCase(internalServerException: InternalServerException) extends DetectSentimentError { final def $ordinal: Int = 0 }
    final case class UnsupportedLanguageExceptionCase(unsupportedLanguageException: UnsupportedLanguageException) extends DetectSentimentError { final def $ordinal: Int = 1 }
    final case class TextSizeLimitExceededExceptionCase(textSizeLimitExceededException: TextSizeLimitExceededException) extends DetectSentimentError { final def $ordinal: Int = 2 }
    final case class InvalidRequestExceptionCase(invalidRequestException: InvalidRequestException) extends DetectSentimentError { final def $ordinal: Int = 3 }

    object InternalServerExceptionCase {
      val hints: Hints = Hints.empty
      val schema: Schema[DetectSentimentError.InternalServerExceptionCase] = bijection(InternalServerException.schema.addHints(hints), DetectSentimentError.InternalServerExceptionCase(_), _.internalServerException)
      val alt = schema.oneOf[DetectSentimentError]("InternalServerException")
    }
    object UnsupportedLanguageExceptionCase {
      val hints: Hints = Hints.empty
      val schema: Schema[DetectSentimentError.UnsupportedLanguageExceptionCase] = bijection(UnsupportedLanguageException.schema.addHints(hints), DetectSentimentError.UnsupportedLanguageExceptionCase(_), _.unsupportedLanguageException)
      val alt = schema.oneOf[DetectSentimentError]("UnsupportedLanguageException")
    }
    object TextSizeLimitExceededExceptionCase {
      val hints: Hints = Hints.empty
      val schema: Schema[DetectSentimentError.TextSizeLimitExceededExceptionCase] = bijection(TextSizeLimitExceededException.schema.addHints(hints), DetectSentimentError.TextSizeLimitExceededExceptionCase(_), _.textSizeLimitExceededException)
      val alt = schema.oneOf[DetectSentimentError]("TextSizeLimitExceededException")
    }
    object InvalidRequestExceptionCase {
      val hints: Hints = Hints.empty
      val schema: Schema[DetectSentimentError.InvalidRequestExceptionCase] = bijection(InvalidRequestException.schema.addHints(hints), DetectSentimentError.InvalidRequestExceptionCase(_), _.invalidRequestException)
      val alt = schema.oneOf[DetectSentimentError]("InvalidRequestException")
    }

    trait Visitor[A] {
      def internalServerException(value: InternalServerException): A
      def unsupportedLanguageException(value: UnsupportedLanguageException): A
      def textSizeLimitExceededException(value: TextSizeLimitExceededException): A
      def invalidRequestException(value: InvalidRequestException): A
    }

    object Visitor {
      trait Default[A] extends Visitor[A] {
        def default: A
        def internalServerException(value: InternalServerException): A = default
        def unsupportedLanguageException(value: UnsupportedLanguageException): A = default
        def textSizeLimitExceededException(value: TextSizeLimitExceededException): A = default
        def invalidRequestException(value: InvalidRequestException): A = default
      }
    }

    implicit val schema: Schema[DetectSentimentError] = union(
      DetectSentimentError.InternalServerExceptionCase.alt,
      DetectSentimentError.UnsupportedLanguageExceptionCase.alt,
      DetectSentimentError.TextSizeLimitExceededExceptionCase.alt,
      DetectSentimentError.InvalidRequestExceptionCase.alt,
    ){
      _.$ordinal
    }
    def liftError(throwable: Throwable): Option[DetectSentimentError] = throwable match {
      case e: InternalServerException => Some(DetectSentimentError.InternalServerExceptionCase(e))
      case e: UnsupportedLanguageException => Some(DetectSentimentError.UnsupportedLanguageExceptionCase(e))
      case e: TextSizeLimitExceededException => Some(DetectSentimentError.TextSizeLimitExceededExceptionCase(e))
      case e: InvalidRequestException => Some(DetectSentimentError.InvalidRequestExceptionCase(e))
      case _ => None
    }
    def unliftError(e: DetectSentimentError): Throwable = e match {
      case DetectSentimentError.InternalServerExceptionCase(e) => e
      case DetectSentimentError.UnsupportedLanguageExceptionCase(e) => e
      case DetectSentimentError.TextSizeLimitExceededExceptionCase(e) => e
      case DetectSentimentError.InvalidRequestExceptionCase(e) => e
    }
  }
}

