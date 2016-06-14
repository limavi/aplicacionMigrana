package models

import java.util.UUID
import java.util.concurrent.Executors

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
  val medicamentoEpisodioTableQuery = TableQuery[MedicamentoEpisodioTable]
  val tipoCausaQuery = TableQuery[TipoCausaTable]
  val posiblesCausasQuery = TableQuery[PosiblesCausasTable]
  val pacienteQuery = TableQuery[PacienteTable]


  def addEpisodioCompleto(epiCompleto: EpisodioCompleto): Future[Int] = {
    val uuid: String = UUID.randomUUID.toString
    for {
      episodioInsertado       <- dbConfig.db.run(episodioTableQuery += epiCompleto.episodio.copy(Id=uuid))
      medicamentoInsertado    <- dbConfig.db.run(medicamentoEpisodioTableQuery.forceInsertAll(epiCompleto.medicamentos.map(_.copy(Id=0,IdEpisodioDolor = uuid))))
      posiblesCausasInsertado <- dbConfig.db.run(posiblesCausasQuery.forceInsertAll(epiCompleto.posiblesCausas.map(_.copy(Id=0, IdEpisodioDolor = uuid))))
    }yield episodioInsertado
  }

  def getEpisodiosCompletos(idPaciente: Option[ Long] ): Future[List[EpisodioCompleto]] = {
    val query= idPaciente match {
      case Some(idPaciente) =>
        episodioTableQuery.filter(_.IdPaciente === idPaciente) joinLeft medicamentoEpisodioTableQuery on {_.Id===_.IdEpisodioDolor} joinLeft posiblesCausasQuery on {_._1.Id ===_.IdEpisodioDolor}
      case None=>
        episodioTableQuery joinLeft medicamentoEpisodioTableQuery on {_.Id===_.IdEpisodioDolor} joinLeft posiblesCausasQuery on {_._1.Id ===_.IdEpisodioDolor}
    }
    val resultF = dbConfig.db.run(query.result)
    resultF.map { result =>
      val grouped: Map[(Episodio, Option[MedicamentoEpisodio]), Seq[((Episodio, Option[MedicamentoEpisodio]), Option[PosiblesCausas])]] = result.groupBy { case (episodio,_) => episodio }
      grouped.map { case (episodio, seq) =>
        val medicamentos: Seq[MedicamentoEpisodio] = seq.flatMap { case (x,y) => x._2 }
        val posiblesCausas: Seq[PosiblesCausas] = seq.flatMap { case (_,posiblesCausasOpt) => posiblesCausasOpt }
        EpisodioCompleto(episodio._1, medicamentos,posiblesCausas)
      }.toList
    }
  }

  def getPacientes(idPaciente: Option[Long] ): Future[Seq[Paciente]] = {
    idPaciente match{
      case Some(idPaciente)=>  dbConfig.db.run(pacienteQuery.filter(_.Id === idPaciente).result)
      case None=>dbConfig.db.run(pacienteQuery.result)
    }
  }

}
