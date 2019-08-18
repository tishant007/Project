package UserSide

import DataBaseCreation.DataBase
import DataBaseCreation.DataBase.{EmailId, User}
import DataBaseCreation.User.UserMethod
import com.google.inject.{Inject, Singleton}
import play.api.Logger
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.libs.ws.WSClient
import slick.driver.PostgresDriver.api._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class UserSideImpl @Inject()(WSClient: WSClient,
                             userMethod: UserMethod) extends UserSideFunctions {
  override def saveOrder(orders: Orders) : Future[String] = {
    WSClient.url("localhost:8080/api/v1/save").post(Json.toJson(orders)).map(_.body)
  }

  override def userViewProduct: Future[List[Product]] = {
    WSClient.url("localhost:8080/api/v1/allProducts").get().map{ data =>
      Json.parse(data.body).validate[List[Product]] match {
        case JsSuccess(list, _) => list
        case JsError(errors) =>
          Logger.error(s"Error : $errors")
          throw new Exception("Invalid Json format")
      }
    }
  }

  override def orderCartOfProducts: Future[List[Orders]] = ???

  override def viewHistoryOfOrders: Future[List[Orders]] = ???

  override def userSignUp(user: User): Future[String] = {
    exec(userMethod.userSignUp(user))
  }

  override def userLogin(userId: UserId, emailId: EmailId): Future[User] = {
    exec(userMethod.userLogin(userId, emailId))
  }

  override def exec[T](action: DBIO[T]): Future[T] = {
    Database.forConfig("mydb").run(action)
  }
}
