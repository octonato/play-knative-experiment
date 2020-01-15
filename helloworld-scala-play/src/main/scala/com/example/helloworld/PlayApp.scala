package com.example.helloworld 
import play.api.mvc._
import play.api.routing.sird._
import play.core.server.AkkaHttpServer
import play.api.BuiltInComponents
import play.api.mvc._
import play.api.routing.sird._
import play.core.server.AkkaHttpServer
import play.core.server.ServerConfig
import play.api.Environment
import play.api.ApplicationLoader
// import play.api.LoggerConfigurator
import play.api.BuiltInComponentsFromContext
import play.api.routing.Router
import play.filters.HttpFiltersComponents
import play.api.Mode

abstract class PlayApp {

  def routes: Router.Routes
  
  val context = ApplicationLoader.Context.create(Environment.simple(mode = Mode.Prod))

  def components: BuiltInComponents = new BuiltInComponentsFromContext(context) with HttpFiltersComponents {
    // Do the logging configuration
    // LoggerConfigurator(context.environment.classLoader).foreach {
    //   _.configure(context.environment, context.initialConfiguration, Map.empty)
    // }
    override def router: Router = Router.from(routes)
  }
  
  // // make Action available in scope
  lazy val Action = components.defaultActionBuilder

  // /** users can override serverConfig */
  def serverConfig = ServerConfig(port = Some(9000))

  def main(args: Array[String]): Unit = {
    val server = 
      AkkaHttpServer.fromApplication(
        components.application, 
        serverConfig
      )
  }
}