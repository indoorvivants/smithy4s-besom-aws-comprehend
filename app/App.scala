import cats.effect.*
import org.http4s.ember.client.EmberClientBuilder

import smithy4s.aws.* // AWS specific interpreters // AWS specific interpreters
import com.amazonaws.comprehend.* // Generated code from specs. // Generated code from specs.
import scala.language.dynamics
import scalatags.Text.TypedTag
import org.http4s.HttpApp
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.scalatags.*
import com.comcast.ip4s.*
import scala.concurrent.duration.*
import org.http4s.UrlForm
import cats.syntax.all.*

object Server extends IOApp.Simple:

  val AWS_REGION = AwsRegion.US_EAST_1

  def run =
    httpServer
      .use: server =>
        scribe.cats.io.info(s"Running at: ${server.baseUri}") *>
          IO.never

  import scribe.json.ScribeCirceJsonSupport
  scribe.Logger.root
    .orphan()
    .clearHandlers()
    .withHandler(
      writer = ScribeCirceJsonSupport.writer(scribe.writer.ConsoleWriter)
    )
    .replace()

  def awsEnvironment(
      httpClient: org.http4s.client.Client[IO]
  ): Resource[IO, AwsEnvironment[IO]] =
    std
      .Env[IO]
      .get("AWS_CONTAINER_CREDENTIALS_RELATIVE_URI")
      .toResource
      .flatMap:
        case None =>
          AwsEnvironment.default[IO](httpClient, AWS_REGION)

        // Below is a hack to work around a bug in credentials loading
        // chain in smithy4s - we manually prioritise ECS credentials
        case Some(_) =>
          val provider = new AwsCredentialsProvider[IO]
          provider
            .refreshing(
              provider
                .fromECS(httpClient, 10.second)
                .onError(exc =>
                  scribe.cats.io
                    .error("Failed to get credentials from ECS endpoint", exc)
                )
            )
            .map: cred =>
              AwsEnvironment.make(
                httpClient,
                IO.pure(AWS_REGION),
                cred,
                IO.realTime.map(_.toSeconds).map(Timestamp(_, 0))
              )

  def comprehend: Resource[IO, Comprehend[IO]] =
    for
      httpClient <- EmberClientBuilder.default[IO].build
      awsEnv     <- awsEnvironment(httpClient)
      service    <- AwsClient(Comprehend, awsEnv)
    yield service

  def httpServer =
    comprehend
      .map(routes(_))
      .flatMap: routes =>
        EmberServerBuilder
          .default[IO]
          .withPort(
            sys.env
              .get("SMITHY4S_PORT")
              .flatMap(Port.fromString)
              .getOrElse(port"8080")
          )
          .withHost(host"0.0.0.0")
          .withHttpApp(routes)
          .withShutdownTimeout(0.second)
          .build

  def routes(comprehend: Comprehend[IO]) =
    import org.http4s.dsl.io.*
    import org.http4s.implicits.*
    HttpApp[IO] { // ...
      case GET -> Root =>
        Ok(Html.template(Html.userInput))

      case request @ POST -> Root / "analyse" =>
        request
          .as[UrlForm]
          .flatMap: form =>
            form.get("text").headOption.map(_.trim).filter(_.nonEmpty) match
              case None =>
                Ok(Html.error("Text cannot be empty"))

              case Some(text) if text.length > 1024 =>
                Ok(Html.error("Text cannot be longer than 1024 characters"))

              case Some(text) =>
                comprehend
                  .detectSentiment(CustomerInputString(text), LanguageCode.EN)
                  .attempt
                  .flatMap:
                    case Left(ex) =>
                      scribe.cats.io
                        .error("Failed to detect sentiment for text", ex) *>
                        Ok(Html.error("Some internal error has occurred"))
                    case Right(res) =>
                      res.sentiment match
                        case None => Ok(Html.error("No sentiment detected"))
                        case Some(sentiment) => Ok(Html.sentiment(sentiment))

      case _ => NotFound()
    }
  end routes

end Server

object Html:
  import scalatags.Text.all.*

  def template(contents: TypedTag[String]) =
    html(
      body(
        cls := "h-14 bg-gradient-to-r from-cyan-800 to-blue-800 p-8 w-full flex justify-center",
        div(
          cls := "container",
          h1(cls  := "text-8xl text-white m-4", "Hello Besom and Smithy4s!"),
          div(cls := "rounded-lg p-4 bg-slate-100", contents)
        )
      ),
      script(src := "https://unpkg.com/htmx.org@1.9.11"),
      script(src := "https://cdn.tailwindcss.com")
    )
  end template

  val userInput =
    div(
      form(
        div(
          cls := "flex justify-center items-center",
          textarea(
            placeholder := "type here...",
            name        := "text",
            cls         := "w-6/12 text-lg p-4 m-4 border-2 border-slate-800",
            rows        := "10"
          ),
          div(
            cls := "space-y-4",
            p(
              "This is a toy service that analyses text sentiment by calling ",
              a(
                href := "https://docs.aws.amazon.com/comprehend/latest/dg/what-is.html",
                "AWS Comprehend",
                cls := "underline"
              ),
              " service."
            ),
            p(
              "See the full code in ",
              a(
                href := "https://github.com/indoorvivants/smithy4s-besom-aws-comprehend",
                "Github repository",
                cls := "underline"
              )
            ),
            p(
              "You can read more about it in ",
              a(
                href := "https://blog.indoorvivants.com/2024-04-05-besom-smithy4s-aws",
                "my blogpost",
                cls := "underline"
              )
            ),
            button(
              hx.post   := "/analyse",
              hx.target := "#result",
              "Analyse",
              cls := "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded text-xl"
            )
          )
        )
      ),
      div(cls := "text-xl", "Sentiment: ", span(id := "result", "..."))
    )
  end userInput

  def error(msg: String) =
    div(cls := "text-lg text-red-600", msg)

  def sentiment(tpe: SentimentType) =
    import SentimentType.*
    val (message, color) = tpe match
      case NEGATIVE => "Negative, cheer the fuck up"     -> "text-red-600"
      case POSITIVE => "Positive, tone it down a little" -> "text-green-600"
      case NEUTRAL =>
        "Neutral, stop sitting on a fence all the time" -> "text-sky-600"
      case _ => "No idea" -> "text-slate-600"
    span(cls := s"text-xl $color", message)
  end sentiment

  /** Helper to create htmx attributes. `hx.replace` will create a `hx-replace`
    * attribute
    */
  object hx extends Dynamic:
    def selectDynamic(name: String): scalatags.Text.Attr =
      scalatags.Text.tags.attr("hx-" + name)

end Html
