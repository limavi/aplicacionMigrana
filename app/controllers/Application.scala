package controllers

import java.util.UUID

import models._
import play.api.libs.json
import services.migranaServices
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.{ Json, Writes }

class Application extends Controller {

  import play.api.mvc._
  import play.api.libs.json._
  import play.api.libs.functional.syntax._

  implicit val episodioFormat = Json.format[Episodio]
  implicit val pacienteFormat = Json.format[Paciente]

  def index= Action {
    Ok(views.html.index("Experimento 1"))
  }

  def consultarEpisodios(idPaciente: Option[ Long ]) = Action.async { implicit request =>
    migranaServices.getEpisodios(idPaciente) map { episodios =>
      Ok( Json.toJson( episodios ) )
    }
  }

  def consultarPacientes(idPaciente: Option[ Long ]) = Action.async { implicit request =>
    migranaServices.getPacientes(idPaciente) map { pacientes =>
      Ok( Json.toJson( pacientes ) )
    }
  }

  def agregarEpisodio = Action { request =>
    request.body.asJson.map { json =>
      json.validate[Episodio].map{
        case (episodio) =>{
          migranaServices.addEpisodioCompleto(episodio).map(resultado => println(resultado))
          Ok("Episodio agregado satisfactoriamente" )
        }
      }.recoverTotal{
        e => BadRequest("Existe un error en el JSON: "+ JsError.toFlatJson(e))
      }
    }.getOrElse {
      BadRequest("Se esperaba un json para ejecutar el POST")
    }
  }


}

