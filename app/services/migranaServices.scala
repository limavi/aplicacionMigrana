package services

import models.{Paciente, EpisodioCompleto, Episodio, Repository}
import scala.concurrent.{ExecutionContext, Future}

object migranaServices {

  def addEpisodioCompleto(epiCompleto: EpisodioCompleto): Future[Int] = {
    Repository.addEpisodioCompleto(epiCompleto)
  }

  def getEpisodiosCompletos(idPaciente: Option[ Long ]): Future[List[EpisodioCompleto]] = {
    Repository.getEpisodiosCompletos(idPaciente)
  }

  def getPacientes(idPaciente: Option[ Long ]): Future[Seq[Paciente]] = {
    Repository.getPacientes(idPaciente)
  }
}
