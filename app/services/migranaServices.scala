package services

import models.{EpisodioCompleto, Episodio, Repository}
import scala.concurrent.{ExecutionContext, Future}

object migranaServices {

  def addEpisodioCompleto(epiCompleto: EpisodioCompleto): Future[Int] = {
    Repository.addEpisodioCompleto(epiCompleto)
  }

  def getEpisodiosCompletos(idPaciente: Option[ Long ]): Future[List[EpisodioCompleto]] = {
    Repository.getEpisodiosCompletos(idPaciente)
  }
}
