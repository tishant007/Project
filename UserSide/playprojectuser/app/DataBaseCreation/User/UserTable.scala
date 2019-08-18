package DataBaseCreation.User

import DataBaseCreation.DataBase.{EmailId, User}
import UserSide.UserId
import slick.lifted.Tag
import slick.driver.PostgresDriver.api._

class UserTable(tag : Tag) extends Table[User](tag, "UserTable") {
  def userId = column[UserId]("UserId")
  def userName = column[String]("UserName")
  def emailId = column[EmailId]("EmailId")
  def * = (userId, userName, emailId) <> ((User.apply _).tupled, User.unapply)
}
