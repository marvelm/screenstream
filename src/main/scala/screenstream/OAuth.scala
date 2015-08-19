package screenstream

import scala.concurrent.duration.Duration
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import java.net.URI
import java.net.URLEncoder

import spray.routing.HttpService
import spray.routing.authentication.BasicAuth
import spray.routing.directives.CachingDirectives._
import spray.routing.directives.DetachMagnet
import spray.httpx.SprayJsonSupport._
import spray.httpx.encoding._
import spray.httpx.unmarshalling._
import spray.httpx.marshalling._
import spray.http._
import spray.client.pipelining._
import spray.json._
import DefaultJsonProtocol._

trait YoutubeOAuth extends HttpService {
  def encode(str: String) = URLEncoder.encode(str, "UTF-8")

  // Redirect the user to accounts.google.com
  // to start the OAuth process
  val youtubeOAuthStartPath = "youtube_auth"
  val youtubeOAuthStart =
    path(youtubeOAuthStartPath) {
      get {
        val scopes = """https://www.googleapis.com/auth/youtube
https://www.googleapis.com/auth/youtube.upload
https://www.googleapis.com/auth/youtube.readonly
""".split("\n").foldLeft("")(_ + "," + encode(_))

        val url = s"""https://accounts.google.com/o/oauth2/auth?
client_id=${encode(Config.ytClientId)}&
redirect_uri=${encode(youtubeOAuthEndPath)}&
scope=$scopes&
response_type=code&
access_type=offline
""".replace("\n", "")

        redirect(url, StatusCodes.TemporaryRedirect)
      }
    }

  private val pipeline: HttpRequest => Future[String] = sendReceive ~> unmarshal[String]

  // The redirect_uri of the OAuth process
  val youtubeOAuthEndPath = "youtube_auth_complete"
  val youtubeOAuthEnd =
    path(youtubeOAuthEndPath) {
      parameters('code.?) { code =>
        get {
          detach(new DetachMagnet()) {
            val url = s"""https://accounts.google.com/o/oauth2/token?
code=${code.get}&
client_id=${encode(Config.ytClientId)}&
client_secret=${encode(Config.ytClientSecret)}
""".replace("\n", "")
            val res: Future[String] = pipeline(Get(""))
            complete {
              res.map { res =>
                val fields = res.parseJson.asJsObject.fields
                val accessToken = fields("access_token").toString()
                val tokenType = fields("token_type").toString() // Bearer
                val expiresIn = fields("expires_in").toString().toInt
                val refreshToken = fields("refresh_token").toString().toInt

                // TODO add to db
                <h1>You have been authenticated</h1>
              }.recover {
                case _ => <h1>Error authenticating</h1>
              }
            }
          }
        }
      }
    }
}
