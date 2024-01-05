package example.domain.admin

import example.entity.contorller.AddUser
import org.springframework.stereotype.Service

@Service
class AdminService {

  def add(addUser: AddUser): User = {
    User(Identify.uuid, addUser.name, addUser.age)
  }
}
