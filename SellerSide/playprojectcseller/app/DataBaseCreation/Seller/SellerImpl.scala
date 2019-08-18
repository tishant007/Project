package DataBaseCreation.Seller

import DataBaseCreation.DataBase._
import slick.driver.PostgresDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import DataBaseCreation.DataBase.Seller
import DataBaseCreation.Product.ProductMethod
import DataBaseCreation.SubOrders.SubOrderMethod
import SellerSide.{SellerId, SubOrderId}
import com.google.inject.{Inject, Singleton}

@Singleton
class SellerImpl @Inject()(productMethod: ProductMethod, subOrderMethod: SubOrderMethod) extends SellerMethod {
  private val sellerTableQuery = TableQuery[SellerTable]
  private def createTable= {
    sellerTableQuery.schema.create
  }

  override def sellerSignUp(seller: Seller): DBIO[String] = {
    (for{
      _ <- sellerTableQuery += seller
    } yield {
      "Seller has been added successfully"
    }).transactionally
  }

  override def sellerLogin(sellerId: SellerId, emailId: EmailId): DBIO[Seller] = {
    sellerTableQuery.filter(seller => seller.sellerId===sellerId && seller.emailId==emailId).result.headOption.map{
      case Some(s) => s
      case None => throw new Exception(s"Seller of $sellerId & $emailId doesn't exist")
    }
  }

  override def sellerOnBoardingProduct(product: Product): DBIO[String] = {
    productMethod.addProduct(product)
  }

  override def sellerApprovesSubOrders(subOrderId: SubOrderId, status : String): DBIO[String] = {
    subOrderMethod.updateSubOrderStatus(status, subOrderId)
  }

  override def updateProductAfterPurchaseApproval(list: List[Product]): DBIO[String] = {
    productMethod.updateProductAfterPurchase(list)
  }
}
