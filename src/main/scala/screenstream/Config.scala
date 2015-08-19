package screenstream

import com.zaxxer.hikari.{ HikariConfig, HikariDataSource }
import scala.slick.driver.PostgresDriver.simple._

object Config {
  lazy val ytClientId = System.getenv("yt_client_id")
  lazy val ytClientSecret = System.getenv("yt_secret")

  val db = {
    val config = new HikariConfig
    val pgHost = System.getenv("pg_host")
    val pgPort = System.getenv("pg_port")
    val pgDb = System.getenv("pg_db")
    val pgUser = System.getenv("pg_user")
    val pgPass = System.getenv("pg_pass")

    config.setJdbcUrl(s"jdbc:postgresql://$pgHost:$pgPort/$pgDb")
    config.setUsername(pgUser)
    config.setPassword(pgPass)

    val ds = new HikariDataSource(config)
    ds.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource")
    Database.forDataSource(ds)
  }
}
