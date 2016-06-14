package models

import org.joda.time.DateTime
import java.sql.Timestamp
import slick.driver.MySQLDriver.api._

case class PosiblesCausas(
   Id: Long,
   IdEpisodioDolor:String,
   IdTipoCausa:Long,
   Descripcion: String)

class PosiblesCausasTable(tag: Tag) extends Table[PosiblesCausas](tag, "PosiblesCausas") {
  implicit val jdateColumnType = MappedColumnType.base[ DateTime, Timestamp ]( dt => new Timestamp( dt.getMillis ), ts => new DateTime( ts.getTime ) )
  def Id = column[Long]("Id", O.PrimaryKey, O.AutoInc)
  def IdEpisodioDolor = column[String]("IdEpisodioDolor")
  def IdTipoCausa = column[Long]("IdTipoCausa")
  def Descripcion = column[String]("Descripcion")
  override def * = (Id, IdEpisodioDolor, IdTipoCausa, Descripcion) <>(PosiblesCausas.tupled, PosiblesCausas.unapply _)
}
