package com.example.helloworld

import play.api.mvc._
import play.api.routing.sird._
import play.core.server.AkkaHttpServer
import Results._
import play.api.routing.Router
// import play.api.Logger

object HelloWorldPlayScala extends PlayApp {

  // private val logger = Logger(getClass())

  val routes = {
    case GET(p"/hello/$to") => 
      Action {
        println(s"serving /hello/$to")
        Ok(s"Hello $to!")
      }

    case GET(p"/") => 
      Action {
        println(s"serving /")
        Ok("")
      }
  }


}
