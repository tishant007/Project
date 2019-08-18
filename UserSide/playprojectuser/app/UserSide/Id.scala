package UserSide

import play.api.libs.json.{Format, JsResult, JsValue, Json}
import slick.driver.PostgresDriver.api._
trait Id {
  val id : Long
}
case class OrderId(id : Long) extends Id{
  val orderId = id
}
object OrderId {
  implicit val idFormat = new Format[OrderId] {
    override def reads(json: JsValue): JsResult[OrderId] = {
      json.validate[Long].map(OrderId(_))
    }
    override def writes(o: OrderId): JsValue = {
      Json.toJson(o.id)
    }
  }
}
case class SubOrderId(id : Long) extends Id{
  val subOrderId = id
}
object SubOrderId {
  implicit val idFormat = new Format[SubOrderId] {
    override def reads(json: JsValue): JsResult[SubOrderId] = {
      json.validate[Long].map(SubOrderId(_))
    }
    override def writes(o: SubOrderId): JsValue = {
      Json.toJson(o.id)
    }
  }
}
case class OrderItemId(id : Long) extends Id{
  val orderItemId = id
}
object OrderItemId {
  implicit val idFormat = new Format[OrderItemId] {
    override def reads(json: JsValue): JsResult[OrderItemId] = {
      json.validate[Long].map(OrderItemId(_))
    }

    override def writes(o: OrderItemId): JsValue = {
      Json.toJson(o.id)
    }
  }
}
case class UserId(id : Long) extends Id{
  val userId = id
}
object UserId {
  implicit val idFormat = new Format[UserId] {
    override def reads(json: JsValue): JsResult[UserId] = {
      json.validate[Long].map(UserId(_))
    }

    override def writes(o: UserId): JsValue = {
      Json.toJson(o.id)
    }
  }
  implicit val columnFormat = MappedColumnType.base[UserId, Long](fromUserId, fromLong)
  private def fromUserId(userId: UserId) = {
    userId.id
  }
  private def fromLong(id : Long) = {
    UserId(id)
  }
}
case class UserAddressId(id : Long) extends Id{
  val userAddressId = id
}
object UserAddressId {
  implicit val idFormat = new Format[UserAddressId] {
    override def reads(json: JsValue): JsResult[UserAddressId] = {
      json.validate[Long].map(UserAddressId(_))
    }

    override def writes(o: UserAddressId): JsValue = {
      Json.toJson(o.id)
    }
  }
}
case class SellerId(id : Long) extends Id{
  val sellerId = id
}
object SellerId {
  implicit val idFormat = new Format[SellerId] {
    override def reads(json: JsValue): JsResult[SellerId] = {
      json.validate[Long].map(SellerId(_))
    }

    override def writes(o: SellerId): JsValue = {
      Json.toJson(o.id)
    }
  }
}
case class ProductId(id : Long) extends Id{
  val productId = id
}
object ProductId {
  implicit val idFormat = new Format[ProductId] {
    override def reads(json: JsValue): JsResult[ProductId] = {
      json.validate[Long].map(ProductId(_))
    }

    override def writes(o: ProductId): JsValue = {
      Json.toJson(o.id)
    }
  }
}