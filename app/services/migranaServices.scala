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

  def generarAlertaPorMuchosDolores(idPaciente: Long )(implicit ec: ExecutionContext): Future[Int] = {
    Repository.getEpisodios(Some(idPaciente)).map(episodios=> {
      if(episodios.length>8){
        println("Alerta!!! usted ya ha tenido "+episodios.length + " dolores de cabeza, por favor saque una cita " )}
      episodios.length
    })
  }

}
