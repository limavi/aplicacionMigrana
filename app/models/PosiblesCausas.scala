package models

import org.joda.time.DateTime
import java.sql.Timestamp
import slick.driver.MySQLDriver.api._

case class PosiblesCausas(
   id: Long,
   IdEpisodioDolor:Long,
   IdTipoCausa:Long,
   Descrpcion: String)

class PosiblesCausasTable(tag: Tag) extends Table[PosiblesCausas](tag, "posiblesCausas") {
  implicit val jdateColumnType = MappedColumnType.base[ DateTime, Timestamp ]( dt => new Timestamp( dt.getMillis ), ts => new DateTime( ts.getTime ) )
  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def IdEpisodioDolor = column[Long]("IdEpisodioDolor")
  def IdTipoCausa = column[Long]("IdTipoCausa")
  def Descrpcion = column[String]("Descrpcion")
  override def * = (id, IdEpisodioDolor, IdTipoCausa, Descrpcion) <>(PosiblesCausas.tupled, PosiblesCausas.unapply _)
}

