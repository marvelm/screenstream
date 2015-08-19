package screenstream

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

import java.net.URI
import java.net.URLEncoder

import spray.routing.HttpService
import spray.routing.authentication.BasicAuth
import spray.routing.directives.CachingDirectives._
import spray.routing.directives.DetachMagnet
import spray.httpx.encoding._
import spray.http.Uri
import spray.http.StatusCodes

trait YoutubeOAuth extends HttpService {
  def encode(str: String) = URLEncoder.encode(str, "UTF-8")

  // Redirect the user to accounts.google.com
  // to start the OAuth process
  val youtubeOAuthStart =
    path("youtube_auth") {
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

  // The redirect_uri of the OAuth process
  val youtubeOAuthEndPath = ""
  val youtubeOAuthEnd =
    path(youtubeOAuthEndPath) {
      parameters('code.?) { code =>
        get {
          detach(new DetachMagnet()) {
            complete {
              // TODO detach, get refresh token, and add to database
              <h1>You have been authenticated</h1>
            }
          }
        }
      }
    }
}
