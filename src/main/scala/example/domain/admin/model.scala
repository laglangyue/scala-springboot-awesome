package example.domain.admin

import java.util.UUID

case class Identify(id: String)

object Identify {
  def uuid: Identify = {
    Identify(UUID.randomUUID().toString)
  }
}

case class User(id: Identify, name: String, age: Int)
