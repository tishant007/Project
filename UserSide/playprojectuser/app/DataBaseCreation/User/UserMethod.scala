package DataBaseCreation.User

import DataBaseCreation.DataBase.{EmailId, User}
import UserSide.UserId
import com.google.inject.ImplementedBy
import slick.driver.PostgresDriver.api._

@ImplementedBy(classOf[UserImpl])
trait UserMethod {
  def userSignUp(user : User) : DBIO[String]
  def userLogin(userId : UserId, emailId: EmailId) : DBIO[User]
}
