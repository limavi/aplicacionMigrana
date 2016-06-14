package models

import org.joda.time.{DateTime, LocalDate}
import slick.lifted.TableQuery
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.{Json, Writes}

import scala.concurrent.{ExecutionContext, Future}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global

object Repository {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  val episodioTableQuery = TableQuery[EpisodioTable]
  val pacienteQuery = TableQuery[PacienteTable]


  def addEpisodio(epi: Episodio): Future[String] = {
    dbConfig.db.run(episodioTableQuery += epi).map(res => "episodio agregado correctamente").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def addListEpisodios(epi: List[Episodio]): Future[String] = {
    dbConfig.db.run(episodioTableQuery.++=(epi)).map(res => "episodios sincronizados").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def getEpisodios(idPaciente: Option[ Long] ): Future[Seq[Episodio]] = {
    val query= idPaciente match {
      case Some(idPaciente) =>episodioTableQuery.filter(_.IdPaciente === idPaciente)
      case None=>episodioTableQuery
    }
    dbConfig.db.run(query.result)
  }

  def getTotalEpisodos(idPaciente: Option[ Long] ): Future[Int] = {
    val query= idPaciente match {
      case Some(idPaciente) =>episodioTableQuery.filter(_.IdPaciente === idPaciente).length
      case None=>episodioTableQuery.length
    }
    dbConfig.db.run(query.result)
  }

  def getPacientes(idPaciente: Option[Long] ): Future[Seq[Paciente]] = {
    idPaciente match{
      case Some(idPaciente)=>  dbConfig.db.run(pacienteQuery.filter(_.Id === idPaciente).result)
      case None=>dbConfig.db.run(pacienteQuery.result)
    }
  }
}
