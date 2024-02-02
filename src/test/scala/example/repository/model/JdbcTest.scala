package example.repository.model

import scalikejdbc.ResultSetIterator

import java.sql.{DriverManager, ResultSet}


object JdbcTest {
  Class.forName("com.mysql.cj.jdbc.Driver")
  val connection = DriverManager.getConnection("jdbc:mysql://172.17.0.2:3306/test", "root", "slalikejdbc")
  val metaData = connection.getMetaData

  def listAllTables(schema: String,
                    types: List[String]): collection.Seq[String] = {
    val (catalog, _schema) = {
      (schema, metaData.getDatabaseProductName) match {
        case (null, _) => (null, null)
        case (s, _) if s.isEmpty => (null, null)
        case (s, "MySQL") => (s, null)
        case (s, _) => (null, s)
      }
    }
    // TABLE_CAT, TABLE_SCHEM, TABLE_NAME
    new ResultSetIterator(
      metaData.getTables(catalog, _schema, "%", types.toArray)
    ).map {
      t => (t.string(""))
    }.toList
  }

  def main(args: Array[String]): Unit = {

    val tables = listAllTables("test", List("TABLE"))

    metaData.getDatabaseProductName
    val catalogs = metaData.getCatalogs
    printResultSet(catalogs)

    val columns = metaData.getColumns("test", "test", "users", "%")

    println("\ncolumns===========\n")
    println(getColumnName(columns))
    printResultSet(columns, 1, 2, 3, 4)

  }

  @scala.annotation.tailrec
  final def printResultSet(result: ResultSet): Unit = {
    if (result.next()) {
      val resultColumns =
        for (c <- 1 to result.getMetaData.getColumnCount) yield {
          result.getString(c)
        }
      println(resultColumns.mkString("  "))
      printResultSet(result)
    }
  }

  final def printResultSet(result: ResultSet, column: Int*): Unit = {
    if (result.next()) {
      val resultColumns =
        for (c <- column) yield {
          result.getString(c)
        }
      println(resultColumns.mkString("  "))
      printResultSet(result, column: _*)
    }
  }

  def getColumnName(resultSet: ResultSet): Seq[String] = {
    val data = resultSet.getMetaData
    for (c <- 1 to data.getColumnCount) yield {
      data.getColumnName(c)
    }
  }

}
