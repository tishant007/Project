package controllers

import DataBaseCreation.DataBase.EmailId
import UserSide.Orders
import com.google.inject.Inject
import play.api.libs.json.{JsError, JsSuccess}
import UserSide._
import play.api.Logger
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserSideController @Inject()(userSideFunctions: UserSideFunctions) extends Controller{
  def saveOrdersSubordersOrderItems = Action.async(parse.json) {
    request =>
      request.body.validate[Orders] match {
        case JsSuccess(orders, _) => userSideFunctions.saveOrder(orders).map(Ok(_))
        case JsError(errors) =>
          Logger.error(s"$errors found")
          Future.successful(BadRequest(s"Cannot parse this Json"))
      }
  }
  def viewAllProducts() = Action.async(parse.json) {
    _ =>
      userSideFunctions.userViewProduct.map(Ok(_))
  }
}
