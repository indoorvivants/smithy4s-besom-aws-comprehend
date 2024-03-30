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

object Server extends IOApp.Simple:

  def run =
    httpServer.use: server =>
      IO.println(s"Running at: ${server.baseUri}") *>
        IO.never

  def comprehend: Resource[IO, Comprehend[IO]] =
    for
      httpClient <- EmberClientBuilder.default[IO].build
      awsEnv     <- AwsEnvironment.default(httpClient, AwsRegion.EU_CENTRAL_1)
      service    <- AwsClient(Comprehend, awsEnv)
    yield service

  def httpServer =
    comprehend
      .map(routes(_))
      .flatMap: routes =>
        EmberServerBuilder
          .default[IO]
          .withPort(port"8080")
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
              case Some(text) =>
                comprehend
                  .detectSentiment(CustomerInputString(text), LanguageCode.EN)
                  .attempt
                  .flatMap:
                    case Left(ex) => Ok(Html.error(ex.getMessage))
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
