package models

import org.joda.time.DateTime
import java.sql.Timestamp
import slick.driver.MySQLDriver.api._

case class Episodio(
   id: Long,
   idPaciente:Long,
   fechayHoraEpisodio: DateTime,
   nivelDolor: String,
   localizacionDolor:String,
   promedioHorasSuenoSemanal:Long)

case class EpisodioCompleto(
   episodio: Episodio,
   medicamentos:Seq[MedicamentoEpisodio]
   )

class EpisodioTable(tag: Tag) extends Table[Episodio](tag, "episodio") {
  implicit val jdateColumnType = MappedColumnType.base[ DateTime, Timestamp ]( dt => new Timestamp( dt.getMillis ), ts => new DateTime( ts.getTime ) )
  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def idPaciente = column[Long]("idPaciente")
  def fechayHoraEpisodio = column[ DateTime ]( "fechayHoraEpisodio")
  def nivelDolor = column[String]("nivelDolor")
  def localizacionDolor = column[String]("localizacionDolor")
  def promedioHorasSuenoSemanal = column[Long]("promedioHorasSuenoSemanal")
  override def * = (id, idPaciente, fechayHoraEpisodio, nivelDolor, localizacionDolor,promedioHorasSuenoSemanal) <>(Episodio.tupled, Episodio.unapply _)
}





