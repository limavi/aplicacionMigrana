package controllers
import models._
import services.migranaServices
import play.api.mvc._
import play.api.libs.json.{ Json, Writes }
import scala.concurrent.ExecutionContext.Implicits.global

class Application extends Controller {

  import play.api.mvc._
  import play.api.libs.json._

  implicit val episodioFormat = Json.format[Episodio]
  implicit val pacienteFormat = Json.format[Paciente]

  def index= Action {
    Ok(views.html.index("Experimento 1"))
  }

  def consultarEpisodios(idPaciente: Option[ Long ]) = Action.async { implicit request =>
    migranaServices.getEpisodios(idPaciente) map { episodio =>
      Ok( Json.toJson( episodio ) )
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
          migranaServices.addEpisodio(episodio).map(resultado => println(resultado))
          migranaServices.generarAlertaPorMuchosDolores(episodio.IdPaciente)
          Ok("Episodio agregado satisfactoriamente" )
        }
      }.recoverTotal{
        e => BadRequest("Existe un error en el JSON: "+ JsError.toFlatJson(e))
      }
    }.getOrElse {
      BadRequest("Se esperaba un json para ejecutar el POST")
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

