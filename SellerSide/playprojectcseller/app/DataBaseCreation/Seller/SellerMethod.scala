package DataBaseCreation.Seller

import DataBaseCreation.DataBase.{EmailId, Product, Seller}
import SellerSide.{SellerId, SubOrderId, SubOrders}
import com.google.inject.ImplementedBy
import slick.driver.PostgresDriver.api._

@ImplementedBy(classOf[SellerImpl])
trait SellerMethod {
  def sellerSignUp(seller : Seller) : DBIO[String]
  def sellerLogin(sellerId : SellerId, emailId: EmailId) : DBIO[Seller]
  def sellerOnBoardingProduct(product : Product) : DBIO[String]
  def sellerApprovesSubOrders(subOrderId: SubOrderId, status : String) : DBIO[String]
  def updateProductAfterPurchaseApproval(list: List[Product]) : DBIO[String]
}
