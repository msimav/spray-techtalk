import akka.actor.{Props, ActorSystem}
import akka.io.IO
import api.BeerApi
import spray.can.Http

object Main extends App {
  implicit val system = ActorSystem("beer-io")

  // create and start our service actor
  val api = system.actorOf(Props[BeerApi], "api")

  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ! Http.Bind(api, interface = "localhost", port = 8080)
}
