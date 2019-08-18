package DataBaseCreation.Product

import DataBaseCreation.DataBase.Product
import SellerSide.ProductId
import com.google.inject.ImplementedBy
import slick.driver.PostgresDriver.api._

@ImplementedBy(classOf[ProductImpl])
trait ProductMethod {
  def addProduct(product: Product) : DBIO[String]
  def getAllProducts : DBIO[List[Product]]
  def updateProductAfterPurchase(list: List[Product]) : DBIO[String]
  def updateApprovalStatus(status : String, productId: ProductId) : DBIO[String]
}
