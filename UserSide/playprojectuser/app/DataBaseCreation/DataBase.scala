package DataBaseCreation

import UserSide.{ProductId, SellerId, UserId}
import play.api.libs.json.{Format, JsResult, JsValue, Json}
import slick.driver.PostgresDriver.api._

trait DataBase
object DataBase {
  case class EmailId(id : String)
  object EmailId {
    implicit val emailIdFormat = new Format[EmailId] {
      override def reads(json: JsValue): JsResult[EmailId] = {
        json.validate[String].map(EmailId(_))
      }

      override def writes(o: EmailId): JsValue = {
        Json.toJson(o.id)
      }
    }
    implicit val columnFormat = MappedColumnType.base[EmailId, String](fromEmailId, fromString)
    private def fromEmailId(emailId: EmailId) = {
      emailId.id
    }
    private def fromString(string: String) = {
      EmailId(string)
    }
  }
  case class User(userId: UserId, userName : String, emailId : EmailId)
  object User {
    implicit val userFormat = Json.format[User]
  }

}
