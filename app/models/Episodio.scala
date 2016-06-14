package models

import org.joda.time.DateTime
import java.sql.Timestamp
import slick.driver.MySQLDriver.api._

case class Episodio(
   Id: String,
   IdPaciente:Long,
   FechayHoraEpisodio: DateTime,
   NivelDolor: String,
   LocalizacionDolor:String,
   PromedioHorasSuenoSemanal:Long)

case class EpisodioCompleto(
   episodio: Episodio,
   medicamentos:Seq[MedicamentoEpisodio],
   posiblesCausas: Seq[PosiblesCausas]
   )

class EpisodioTable(tag: Tag) extends Table[Episodio](tag, "EpisodioDolor") {
  implicit val jdateColumnType = MappedColumnType.base[ DateTime, Timestamp ]( dt => new Timestamp( dt.getMillis ), ts => new DateTime( ts.getTime ) )
  def Id = column[String]("Id",O.PrimaryKey )
  def IdPaciente = column[Long]("IdPaciente")
  def FechayHoraEpisodio = column[ DateTime ]( "FechayHoraEpisodio")
  def NivelDolor = column[String]("NivelDolor")
  def LocalizacionDolor = column[String]("LocalizacionDolor")
  def PromedioHorasSuenoSemanal = column[Long]("PromedioHorasSuenoSemanal")
  override def * = (Id, IdPaciente, FechayHoraEpisodio, NivelDolor, LocalizacionDolor,PromedioHorasSuenoSemanal) <>(Episodio.tupled, Episodio.unapply _)
}





