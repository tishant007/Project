package SellerSide

import DataBaseCreation.DataBase._
import com.google.inject.{Inject, Singleton}
import scala.concurrent.Future
import DataBaseCreation.OrderAndSubOrder.OrderAndSubOrderMethod
import DataBaseCreation.OrderItem.OrderItemMethod
import DataBaseCreation.Orders.OrderMethod
import DataBaseCreation.Product.ProductMethod
import DataBaseCreation.Seller.SellerMethod
import DataBaseCreation.SubOrderAndOrderItem.SubOrderAndOrderItemMethod
import DataBaseCreation.SubOrders.SubOrderMethod
import org.joda.time.DateTime
import slick.driver.PostgresDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global


@Singleton
class SellerSideImpl @Inject()(orderAndSubOrderMethod: OrderAndSubOrderMethod,
                               orderItemMethod: OrderItemMethod,
                               orderMethod : OrderMethod,
                               productMethod: ProductMethod,
                               sellerMethod: SellerMethod,
                               subOrderAndOrderItemMethod: SubOrderAndOrderItemMethod,
                               subOrderMethod : SubOrderMethod) extends SellerSideFunctions {


  override def exec[T](action: DBIO[T]) = {
    Database.forConfig("mydb").run(action)
  }

  override def saveOrder(orders: Orders): Future[String] = {
    exec(orderMethod.addOrder(orders))
  }

  override def getOrderByUserId(orderId: OrderId, userId: UserId): Future[List[Orders]] = {
    exec(for{
      listOfOrders <- orderMethod.getAllOrdersByUserId(userId)
    } yield {
      listOfOrders.isEmpty match {
        case false => listOfOrders
        case true => throw new Exception(s"Orders for this $userId doesn't exist")
      }
    })
  }

  override def getListOfOrders(list: List[OrderId]): Future[List[(OrderId, Boolean)]] = {
    exec{
      DBIO.sequence(list.map{orderId =>
        orderMethod.checkOrder(orderId).map(bool => (orderId, bool))
      })
    }
  }

  override def getAllOrdersByUserIdForLast15Days(userId: UserId): Future[List[Orders]] = {
    exec(orderMethod.getAllOrdersByDate(DateTime.now, userId))
  }

  override def getAllOrdersByOrderItemId(orderItemId: OrderItemId): Future[List[Orders]] = {
    exec(for {
      listOfSubOrderId <- subOrderAndOrderItemMethod.getAllSubOrdersByOrderItemId(orderItemId)
      listOfOrders <- DBIO.sequence(listOfSubOrderId.map{ suborderId =>
        (for{
          listOfOrderId <- orderAndSubOrderMethod.getAllOrderBySubOrderId(suborderId)
          order <- DBIO.sequence(listOfOrderId.map(orderMethod.getOrderByOrderId))
        } yield {
          order
        }).transactionally
      })
    } yield {
      listOfOrders.flatten
    })
  }

  override def getAllProducts: Future[List[Product]] = {
    exec(productMethod.getAllProducts)
  }

  override def updateProducts(list: List[Product]): Future[String] = {
    exec(productMethod.updateProductAfterPurchase(list)).recover(throw new Exception(s"Error : Products cannot be updated after purchase"))
  }

  override def signUpSeller(seller: Seller): Future[String] = {
    exec(sellerMethod.sellerSignUp(seller))
  }

  override def loginSeller(sellerId: SellerId, emailId: EmailId): Future[String] = {
    exec(sellerMethod.sellerLogin(sellerId, emailId)).map(_ => "Logged In")
  }

  override def adminApprovesProduct(productId: ProductId, status: String): Future[String] = {
    exec(productMethod.updateApprovalStatus(status, productId)).map(_=>"Product Approved")
  }

  override def sellerSubOrderApproval(subOrderId: SubOrderId, status: String): Future[String] = {
    exec(sellerMethod.sellerApprovesSubOrders(subOrderId, status)).map(_ => "SubOrder Approved")
  }

  override def sellerOnBoardProduct(product: Product): Future[String] = {
    exec(sellerMethod.sellerOnBoardingProduct(product))
  }
}
