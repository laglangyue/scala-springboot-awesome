package example.repository.model

import org.scalatest.flatspec.FixtureAnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scalikejdbc._
import scalikejdbc.scalatest.AutoRollback


class UsersSpec extends FixtureAnyFlatSpec with Matchers with AutoRollback {
  

  behavior of "Users"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = Users.find(123)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = Users.findBy(sqls"user_id = ${123}")
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = Users.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = Users.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = Users.findAllBy(sqls"user_id = ${123}")
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = Users.countBy(sqls"user_id = ${123}")
    count should be >(0L)
  }

  it should "save a record" in { implicit session =>
    val entity = Users.findAll().head
    // TODO modify something
    val modified = entity
    val updated = Users.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = Users.findAll().head
    val deleted = Users.destroy(entity)
    deleted should be(1)
    val shouldBeNone = Users.find(123)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = Users.findAll()
    entities.foreach(e => Users.destroy(e))
    val batchInserted = Users.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
