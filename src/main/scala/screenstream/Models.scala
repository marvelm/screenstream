package screenstream

import slick.driver.PostgresDriver.api._
import java.sql.Timestamp

object Models {
  val youtubeTokens = TableQuery[YoutubeTokens]
}

case class YoutubeToken(
  id: Int,
  created: Timestamp,
  accessToken: String,
  tokenType: String,
  expiresIn: Int,
  refreshToken: String
)

class YoutubeTokens(tag: Tag) extends Table[YoutubeToken](tag, "YoutubeTokens") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def created = column[Timestamp]("created")
  def accessToken = column[String]("accessToken")
  def tokenType = column[String]("tokenType")
  def expiresIn = column[Int]("expiresIn")
  def refreshToken = column[String]("refreshToken")
  def * = (id, created, accessToken, tokenType, expiresIn, refreshToken) <> (YoutubeToken.tupled, YoutubeToken.unapply)
}
