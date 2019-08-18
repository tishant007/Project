package DataBaseCreation.Product

import DataBaseCreation.DataBase.Product
import SellerSide.ProductId
import com.google.inject.Singleton
import slick.driver
import slick.driver.PostgresDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ProductImpl extends ProductMethod {
  private val productTableQuery = TableQuery[ProductTable]
  def createTable = {
    productTableQuery.schema.create
  }

  override def getAllProducts : DBIO[List[Product]] = {
    productTableQuery.result.map(_.toList)
  }

  override def updateProductAfterPurchase(list: List[Product]): DBIO[String] = {
    list.map{product =>
      productTableQuery.filter(_.productId===product.productId).map(_.quantity).update{
        productTableQuery.filter(_.productId===product.productId).result.head.asInstanceOf[Product].quantity
        - product.quantity}
    }
    DBIO.successful("Product table has been successfully updated")
  }

  override def addProduct(product: Product): DBIO[String] = {
    for{
      _ <- productTableQuery += product
    } yield {
      "Product has been successfully added"
    }
  }

  override def updateApprovalStatus(status: String, productId: ProductId): DBIO[String] = {
    for{
       _ <- productTableQuery.filter(_.productId===productId).map(_.approvalStatus).update(status)
    } yield {
      "Product has been approved"
    }
  }
}
