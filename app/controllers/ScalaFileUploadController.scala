package controllers

import java.nio.file.Paths
import javax.inject.Inject

import play.api.mvc.{AbstractController, ControllerComponents}

class ScalaFileUploadController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action {
    val uploadPath = new java.io.File(".").getCanonicalPath

    //    Ok(views.html.metadata())
    Ok(uploadPath)
  }

  def upload = Action(parse.multipartFormData) { request =>
    request.body.file("picture").map { picture =>

      // only get the last part of the filename
      // otherwise someone can send a path like ../../home/foo/bar.txt to write to other files on the system
      val uploadPath = new java.io.File(".").getCanonicalPath
      val uploadFolder = "/public/uploads/"
      val filename = Paths.get(picture.filename).getFileName

      picture.ref.moveTo(Paths.get(s"/tmp/picture/$filename"), replace = true)
      Ok("File uploaded")
    }.getOrElse {
      Redirect(routes.ScalaFileUploadController.upload()).flashing(
        "error" -> "Missing file")
    }
  }

}
