package models

import slick.driver.MySQLDriver.api._

case class TipoCausa(
   id: Long,
   Nombre:String)

class TipoCausaTable(tag: Tag) extends Table[TipoCausa](tag, "tipoCausa") {
  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def Nombre = column[String]("Nombre")
  override def * = (id, Nombre) <>(TipoCausa.tupled, TipoCausa.unapply _)
}

