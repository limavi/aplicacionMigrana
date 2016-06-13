package models

import org.joda.time.DateTime
import java.sql.Timestamp
import slick.driver.MySQLDriver.api._

case class MedicamentoEpisodio(
   id: Long,
   IdEpisodioDolor:Long,
   Medicamento: String)

class MedicamentoEpisodioTable(tag: Tag) extends Table[MedicamentoEpisodio](tag, "medicamentoepisodio") {
  implicit val jdateColumnType = MappedColumnType.base[ DateTime, Timestamp ]( dt => new Timestamp( dt.getMillis ), ts => new DateTime( ts.getTime ) )
  def Id = column[Long]("Id", O.PrimaryKey,O.AutoInc)
  def IdEpisodioDolor = column[Long]("IdEpisodioDolor")
  def Medicamento = column[String]("Medicamento")
  override def * = (Id, IdEpisodioDolor, Medicamento) <>(MedicamentoEpisodio.tupled, MedicamentoEpisodio.unapply _)
}

