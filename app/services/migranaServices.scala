package services

import models.{Paciente, Episodio, Repository}
import scala.concurrent.{ExecutionContext, Future}

object migranaServices {

  def addEpisodio(epiCompleto: Episodio): Future[String] = {
    Repository.addEpisodio(epiCompleto)
  }

  def addListEpisodios(epiCompleto: List[ Episodio]): Future[String] = {
    Repository.addListEpisodios(epiCompleto)
  }


  def getEpisodios(idPaciente: Option[ Long ]): Future[Seq[Episodio]] = {
    Repository.getEpisodios(idPaciente)
  }

  def getPacientes(idPaciente: Option[ Long ]): Future[Seq[Paciente]] = {
    Repository.getPacientes(idPaciente)
  }
}
