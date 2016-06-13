package models

import slick.lifted.TableQuery

import scala.concurrent.Future

import org.joda.time.DateTime
import java.sql.Timestamp
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.{Json, Writes}
import scala.concurrent.Future
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global


object Repository {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  val episodioTableQuery = TableQuery[EpisodioTable]
  val MedicamentoEpisodioTableQuery = TableQuery[MedicamentoEpisodioTable]

  def addEpisodioCompleto(epiCompleto: EpisodioCompleto): Future[Int] = {
    val x: Future[Option[Int]] = dbConfig.db.run(MedicamentoEpisodioTableQuery.forceInsertAll(epiCompleto.medicamentos))
    for {
      episodio <- dbConfig.db.run(episodioTableQuery += epiCompleto.episodio)
      medicamentoInsertado <- dbConfig.db.run(MedicamentoEpisodioTableQuery.forceInsertAll(epiCompleto.medicamentos))
    }yield (episodio)
  }


  def getEpisodiosCompletos(idPaciente: Option[ Long] ): Future[List[EpisodioCompleto]] = {
    val query= idPaciente match {
      case Some(idPaciente) =>
       episodioTableQuery.filter(_.idPaciente === idPaciente) joinLeft MedicamentoEpisodioTableQuery on { case (episodio, medicamento) =>
          episodio.id === medicamento.IdEpisodioDolor
        }
      case None=>
       episodioTableQuery joinLeft MedicamentoEpisodioTableQuery on { case (episodio, medicamento) =>
          episodio.id === medicamento.IdEpisodioDolor
        }
    }
    val resultF = dbConfig.db.run(query.result)
    resultF.map { result =>
      val grouped: Map[Episodio, Seq[(Episodio, Option[MedicamentoEpisodio])]] = result.groupBy { case (episodio,_) => episodio }
      grouped.map { case (episodio, seq) =>
        val medicamentos: Seq[MedicamentoEpisodio] = seq.flatMap { case (_,medicOpt) => medicOpt }
        EpisodioCompleto(episodio, medicamentos)
      }.toList
    }
  }
































  def getEpisodiosPorPaciente(idPaciente: Long ): Future[Seq[Episodio]] = {
    dbConfig.db.run(episodioTableQuery.filter(_.idPaciente === idPaciente).result)
  }

  def listAllEpisodios: Future[Seq[Episodio]]= {
    dbConfig.db.run(episodioTableQuery.result)
  }

}
