package models

import slick.driver.MySQLDriver.api._

case class TipoCausa(
   Id: Long,
   Nombre:String)

class TipoCausaTable(tag: Tag) extends Table[TipoCausa](tag, "TipoCausa") {
  def Id = column[Long]("Id", O.PrimaryKey,O.AutoInc)
  def Nombre = column[String]("Nombre")
  override def * = (Id, Nombre) <>(TipoCausa.tupled, TipoCausa.unapply _)
}

