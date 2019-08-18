package DataBaseCreation.Seller

import DataBaseCreation.DataBase.{EmailId, Seller}
import SellerSide.SellerId
import com.google.inject.Singleton
import slick.driver.PostgresDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class SellerTable(tag: Tag) extends Table[Seller](tag, "SellerTable"){
  def sellerId = column[SellerId]("SellerId")

  def sellerName = column[String]("SellerName")

  def emailId = column[EmailId]("EmailId")

  def * = (sellerId, sellerName, emailId) <> ((Seller.apply _).tupled, Seller.unapply)

}
