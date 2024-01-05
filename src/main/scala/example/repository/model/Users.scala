package example.repository.model

import scalikejdbc._
import java.time.{ZonedDateTime}

case class Users(
  user: Option[String] = None,
  currentConnections: Long,
  totalConnections: Long,
  maxSessionControlledMemory: Long,
  maxSessionTotalMemory: Long,
  userId: Int,
  username: String,
  password: String,
  fullName: String,
  email: String,
  phoneNumber: Option[String] = None,
  address: Option[String] = None,
  createdAt: Option[ZonedDateTime] = None,
  updatedAt: Option[ZonedDateTime] = None) {

  def save()(implicit session: DBSession = Users.autoSession): Users = Users.save(this)(session)

  def destroy()(implicit session: DBSession = Users.autoSession): Int = Users.destroy(this)(session)

}


object Users extends SQLSyntaxSupport[Users] {

  override val schemaName = Some("slalikejdbc")

  override val tableName = "users"

  override val columns = Seq("USER", "CURRENT_CONNECTIONS", "TOTAL_CONNECTIONS", "MAX_SESSION_CONTROLLED_MEMORY", "MAX_SESSION_TOTAL_MEMORY", "user_id", "username", "password", "full_name", "email", "phone_number", "address", "created_at", "updated_at")

  def apply(u: SyntaxProvider[Users])(rs: WrappedResultSet): Users = apply(u.resultName)(rs)
  def apply(u: ResultName[Users])(rs: WrappedResultSet): Users = new Users(
    user = rs.get(u.user),
    currentConnections = rs.get(u.currentConnections),
    totalConnections = rs.get(u.totalConnections),
    maxSessionControlledMemory = rs.get(u.maxSessionControlledMemory),
    maxSessionTotalMemory = rs.get(u.maxSessionTotalMemory),
    userId = rs.get(u.userId),
    username = rs.get(u.username),
    password = rs.get(u.password),
    fullName = rs.get(u.fullName),
    email = rs.get(u.email),
    phoneNumber = rs.get(u.phoneNumber),
    address = rs.get(u.address),
    createdAt = rs.get(u.createdAt),
    updatedAt = rs.get(u.updatedAt)
  )

  val u = Users.syntax("u")

  override val autoSession = AutoSession

  def find(userId: Int)(implicit session: DBSession = autoSession): Option[Users] = {
    sql"""select ${u.result.*} from ${Users as u} where ${u.userId} = ${userId}"""
      .map(Users(u.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Users] = {
    sql"""select ${u.result.*} from ${Users as u}""".map(Users(u.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    sql"""select count(1) from ${Users.table}""".map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Users] = {
    sql"""select ${u.result.*} from ${Users as u} where ${where}"""
      .map(Users(u.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Users] = {
    sql"""select ${u.result.*} from ${Users as u} where ${where}"""
      .map(Users(u.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    sql"""select count(1) from ${Users as u} where ${where}"""
      .map(_.long(1)).single.apply().get
  }

  def create(
    user: Option[String] = None,
    currentConnections: Long,
    totalConnections: Long,
    maxSessionControlledMemory: Long,
    maxSessionTotalMemory: Long,
    username: String,
    password: String,
    fullName: String,
    email: String,
    phoneNumber: Option[String] = None,
    address: Option[String] = None,
    createdAt: Option[ZonedDateTime] = None,
    updatedAt: Option[ZonedDateTime] = None)(implicit session: DBSession = autoSession): Users = {
    val generatedKey = sql"""
      insert into ${Users.table} (
        ${column.user},
        ${column.currentConnections},
        ${column.totalConnections},
        ${column.maxSessionControlledMemory},
        ${column.maxSessionTotalMemory},
        ${column.username},
        ${column.password},
        ${column.fullName},
        ${column.email},
        ${column.phoneNumber},
        ${column.address},
        ${column.createdAt},
        ${column.updatedAt}
      ) values (
        ${user},
        ${currentConnections},
        ${totalConnections},
        ${maxSessionControlledMemory},
        ${maxSessionTotalMemory},
        ${username},
        ${password},
        ${fullName},
        ${email},
        ${phoneNumber},
        ${address},
        ${createdAt},
        ${updatedAt}
      )
      """.updateAndReturnGeneratedKey.apply()

    Users(
      userId = generatedKey.toInt,
      user = user,
      currentConnections = currentConnections,
      totalConnections = totalConnections,
      maxSessionControlledMemory = maxSessionControlledMemory,
      maxSessionTotalMemory = maxSessionTotalMemory,
      username = username,
      password = password,
      fullName = fullName,
      email = email,
      phoneNumber = phoneNumber,
      address = address,
      createdAt = createdAt,
      updatedAt = updatedAt)
  }

  def batchInsert(entities: collection.Seq[Users])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(String, Any)]] = entities.map(entity =>
      Seq(
        "user" -> entity.user,
        "currentConnections" -> entity.currentConnections,
        "totalConnections" -> entity.totalConnections,
        "maxSessionControlledMemory" -> entity.maxSessionControlledMemory,
        "maxSessionTotalMemory" -> entity.maxSessionTotalMemory,
        "username" -> entity.username,
        "password" -> entity.password,
        "fullName" -> entity.fullName,
        "email" -> entity.email,
        "phoneNumber" -> entity.phoneNumber,
        "address" -> entity.address,
        "createdAt" -> entity.createdAt,
        "updatedAt" -> entity.updatedAt))
    SQL("""insert into users(
      USER,
      CURRENT_CONNECTIONS,
      TOTAL_CONNECTIONS,
      MAX_SESSION_CONTROLLED_MEMORY,
      MAX_SESSION_TOTAL_MEMORY,
      username,
      password,
      full_name,
      email,
      phone_number,
      address,
      created_at,
      updated_at
    ) values (
      {user},
      {currentConnections},
      {totalConnections},
      {maxSessionControlledMemory},
      {maxSessionTotalMemory},
      {username},
      {password},
      {fullName},
      {email},
      {phoneNumber},
      {address},
      {createdAt},
      {updatedAt}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: Users)(implicit session: DBSession = autoSession): Users = {
    sql"""
      update
        ${Users.table}
      set
        ${column.user} = ${entity.user},
        ${column.currentConnections} = ${entity.currentConnections},
        ${column.totalConnections} = ${entity.totalConnections},
        ${column.maxSessionControlledMemory} = ${entity.maxSessionControlledMemory},
        ${column.maxSessionTotalMemory} = ${entity.maxSessionTotalMemory},
        ${column.userId} = ${entity.userId},
        ${column.username} = ${entity.username},
        ${column.password} = ${entity.password},
        ${column.fullName} = ${entity.fullName},
        ${column.email} = ${entity.email},
        ${column.phoneNumber} = ${entity.phoneNumber},
        ${column.address} = ${entity.address},
        ${column.createdAt} = ${entity.createdAt},
        ${column.updatedAt} = ${entity.updatedAt}
      where
        ${column.userId} = ${entity.userId}
      """.update.apply()
    entity
  }

  def destroy(entity: Users)(implicit session: DBSession = autoSession): Int = {
    sql"""delete from ${Users.table} where ${column.userId} = ${entity.userId}""".update.apply()
  }

}
