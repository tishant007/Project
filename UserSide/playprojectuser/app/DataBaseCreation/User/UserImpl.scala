package DataBaseCreation.User
import DataBaseCreation.DataBase.{EmailId, User}
import UserSide.{Orders, UserId}
import slick.driver.PostgresDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserImpl extends UserMethod {
  val userTableQuery = TableQuery[UserTable]
  def createTable  = {
    Database.forConfig("mydb").run(userTableQuery.schema.create)
  }
  override def userSignUp(user: User): DBIO[String] = {
    (for{
      _ <- userTableQuery += user
    }yield{
      "User has been added successfully"
    }).transactionally
  }

  override def userLogin(userId: UserId, emailId: EmailId): DBIO[User] = {
    userTableQuery.filter(user => user.userId===userId && user.emailId===emailId).result.headOption.map{
      case Some(user) =>user
      case None => throw new Exception(s"User of $userId & $emailId doesn't exist")
    }
  }

}
