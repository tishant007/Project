package DataBaseCreation.Product

import DataBaseCreation.DataBase.Product
import SellerSide.{ProductId, SellerId}
import com.google.inject.Singleton
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._


@Singleton
class ProductTable(tag: Tag) extends Table[Product](tag, "ProductTable"){
  def productId = column[ProductId]("ProductId")

  def pricePerUnit = column[Int]("PricePerUnit")

  def quantity = column[Int]("Quantity")

  def sellerId = column[SellerId]("SellerId")

  def approvalStatus = column[String]("ApprovalStatus")

  def * = (productId, sellerId, approvalStatus, pricePerUnit, quantity) <> ((Product.apply _).tupled, Product.unapply)
}
