package controllers

import java.io.File
import java.nio.file.Paths
import javax.inject.Inject

import play.api.mvc.{AbstractController, ControllerComponents}




class ScalaFileUploadController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action {
    Ok(views.html.metadata())
  }

  def upload = Action(parse.multipartFormData) { request =>
    request.body.file("picture").map { picture =>

      // only get the last part of the filename..
      // otherwise someone can send a path like ../../home/foo/bar.txt to write to other files on the system
      val uploadPath = new java.io.File(".").getCanonicalPath
      val uploadFolder = "/public/uploads/"
      val filename = Paths.get(picture.filename).getFileName

//      picture.ref.moveTo(Paths.get(s"/tmp/picture/$filename"), replace = true) //todo save to temp folder
      val file = new File(uploadPath + uploadFolder + picture.filename)
//      val file = new File(s"/tmp/picture/$filename")

      picture.ref.moveTo(file, replace = true)
      file.length
      Ok(file.length.toString)
    }.getOrElse {
      Redirect(routes.ScalaFileUploadController.upload()).flashing(
        "error" -> "Missing file")
    }
  }
}
