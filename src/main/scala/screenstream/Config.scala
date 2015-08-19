package screenstream

object Config {
  lazy val ytClientId = System.getenv("yt_client_id")
  lazy val ytClientSecret = System.getenv("yt_secret")
}
