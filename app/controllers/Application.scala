package controllers
import models._
import services.migranaServices
import play.api.mvc._
import play.api.libs.json.{Json, Writes}
import slick.dbio.FutureAction

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Application extends Controller {

  import play.api.mvc._
  import play.api.libs.json._

  implicit val episodioFormat = Json.format[Episodio]
  implicit val pacienteFormat = Json.format[Paciente]

  def index= Action {
    Ok(views.html.index("Experimento 1 -Latencia y Escalabilidad "))
  }

  def consultarEpisodios(id: Option[ Long ]) = Action.async { implicit request =>
    migranaServices.getEpisodios(id) map { episodio =>
      Ok( Json.toJson( episodio ) )
    }
  }

  def consultarEpisodiosPorPaciente(TipoDocumento: String, NumeroDocumento: Long)= Action.async { implicit request =>
    migranaServices.getEpisodiosPorPaciente(TipoDocumento,NumeroDocumento) map { episodiosPorPaciente =>
      Ok( Json.toJson( episodiosPorPaciente ) )
    }
  }

  def consultarPacientes(TipoDocumento:Option[ String ],  NumeroDocumento: Option[Long ]): Action[AnyContent] = Action.async { implicit request =>
    migranaServices.getPacientes(TipoDocumento,NumeroDocumento ) map { pacientes =>
      Ok( Json.toJson( pacientes ) )
    }
  }

  def agregarEpisodio= Action.async { implicit request =>
    request.body.asJson.map { json =>
      json.validate[Episodio].map{
        case (episodio) => {
          Repository.addEpisodio(episodio).map(resultado => {
            //migranaServices.generarAlertaPorMuchosDolores(episodio.IdPaciente)
            Ok("Episodio agregado satisfactoriamente")
          })
        }
      }.recoverTotal{
        e => Future(BadRequest("Existe un error en el JSON: "+ JsError.toFlatJson(e)))
      }
    }.getOrElse {
      Future(BadRequest("Se esperaba un json para ejecutar el POST"))
    }
  }

  def sincronizarEpisodios = Action { request =>
    request.body.asJson.map { json =>
      json.validate[List [Episodio]].map{
        case (listEpisodios) =>{
          migranaServices.addListEpisodios(listEpisodios).map(resultado => println(resultado))
          Ok("Episodios sincronizados satisfactoriamente" )
        }
      }.recoverTotal{
        e => BadRequest("Existe un error en el JSON: "+ JsError.toFlatJson(e))
      }
    }.getOrElse {
      BadRequest("Se esperaba un json para ejecutar el POST")
    }
  }
}

