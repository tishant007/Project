package controllers

import SellerSide.Orders
import com.google.inject.Inject
import play.api.libs.json.{JsError, JsSuccess, Json}
import SellerSide._
import play.api.Logger
import play.api.mvc.{Action, Controller}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import DataBaseCreation.DataBase.{EmailId, Product, Seller}

class SellerSideController @Inject()(sellerSideFunctions: SellerSideFunctions) extends Controller{
  def saveOrder() = Action.async(parse.json) {
    implicit request =>
      request.body.validate[Orders] match {
        case JsSuccess(orders, _) => sellerSideFunctions.saveOrder(orders).map(Ok(_))
        case JsError(errors) =>
          Logger.error(s"$errors found")
          Future.successful(BadRequest(s"Cannot parse this Json"))
      }
  }

  def checkOrderForAUserId(orderId: String, userId: String) = Action.async(parse.json){
    _ =>
      sellerSideFunctions.getOrderByUserId(OrderId(orderId.toLong), UserId(userId.toLong)).map(o => Ok(Json.toJson(o))).recover{
        case exception =>
          Logger.error(s"Error : $exception")
          BadRequest(s"Order for $userId doesn't exist")
      }
  }

  def getOrdersOfLast15Days(userId: String) = Action.async(parse.json) {
    _ =>
      sellerSideFunctions.getAllOrdersByUserIdForLast15Days(UserId(userId.toLong)).map(o => Ok(Json.toJson(o))).recover{
        case exception =>
          Logger.error(s"Error : $exception")
          BadRequest(s"No orders for this user : $userId")
      }
  }

  def getAllOrdersByOrderItemId(orderItemId: String) = Action.async(parse.json) {
    _ =>
      sellerSideFunctions.getAllOrdersByOrderItemId(OrderItemId(orderItemId.toLong)).map(o => Ok(Json.toJson(o))).recover {
        case exception =>
          Logger.error(s"Error : $exception")
          BadRequest(s"No orders for this order item : $orderItemId")
      }
  }

  def getListOfResponse() = Action.async(parse.json) {
    implicit request =>
      request.body.validate[List[OrderId]] match {
        case JsSuccess(list, _) => sellerSideFunctions.getListOfOrders(list).map{ list =>
          Ok{Json.toJson(list.map{
            case (orderId, response) => List(orderId.toString, response.toString)
          })}
        }
        case JsError(error)=>
          Logger.error(s"Error : $error")
          Future.successful(BadRequest(s"Cannot parse this Json"))
      }
  }

  def getAllProducts() = Action.async(parse.json) {
    _ =>
      sellerSideFunctions.getAllProducts.map(o => Ok(Json.toJson(o)))
  }

  def sellerLogin(sellerId: SellerId, emailId: EmailId) = Action.async(parse.json) {
    _=>
      sellerSideFunctions.loginSeller(sellerId, emailId).map(o => Ok(Json.toJson(o)))
  }
  def sellerSignUp(seller : Seller) = Action.async(parse.json) {
    _=>
      sellerSideFunctions.signUpSeller(seller).map(o => Ok(Json.toJson(o)))
  }
  def sellerOnBoardProduct(product: Product) = Action.async(parse.json){
    _=>
      sellerSideFunctions.sellerOnBoardProduct(product).map(o => Ok(Json.toJson(o)))
  }
  def adminSellerApprovesProduct(productId: ProductId) = Action.async(parse.json){
    _=>
      sellerSideFunctions.adminApprovesProduct(productId).map(o => Ok(Json.toJson(o)))
  }
  def sellerApprovesSubOrder(subOrderId: SubOrderId) = Action.async(parse.json) {
    _=>
      sellerSideFunctions.sellerSubOrderApproval(subOrderId).map(o=>Ok(Json.toJson(o)))
  }
  def updateAllProductsAfterPurchase(list : List[Product]) = Action.async(parse.json) {
    _=>
      sellerSideFunctions.updateProducts(list).map(o => Ok(Json.toJson(o)))
  }
}
