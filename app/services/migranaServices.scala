package services

import models._

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


  def getEpisodiosPorPaciente(TipoDocumento: String, NumeroDocumento: Long) = {
    Repository.getEpisodiosPorPaciente(TipoDocumento,NumeroDocumento)
  }

  def getPacientes(TipoDocumento:Option[ String ],  NumeroDocumento: Option[Long ]): Future[Seq[Paciente]] = {
    Repository.getPacientes(TipoDocumento, NumeroDocumento)
  }

  def generarAlertaPorMuchosDolores(idPaciente: Long ): Future[Int] = {
    import scala.concurrent.ExecutionContext.Implicits.global
    Repository.getTotalEpisodosPorPaciente(idPaciente).map(total=> {
      if(total>12000){
        println("Alerta!!! usted ya ha tenido "+ total + " dolores de cabeza, por favor saque una cita " )}
      total
    })
  }
}
