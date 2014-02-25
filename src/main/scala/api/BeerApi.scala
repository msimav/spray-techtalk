package api

import core.BeerActor
import model.Beer
import spray.routing.HttpServiceActor
import spray.json._
import spray.httpx.SprayJsonSupport._
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

class BeerApi extends HttpServiceActor with DefaultJsonProtocol {
  import BeerActor._
  import context.dispatcher

  val core = context actorOf (Props[BeerActor], "core")
  implicit val beerFormat = jsonFormat2(Beer)
  implicit val timeout = Timeout(3 seconds)

  override def receive: Receive = runRoute {
    path("beer") {
      get {
        complete {
          (core ? GetAll).mapTo[List[String]]
        }
      } ~
      post {
        complete("")
      }
    } ~
    path("beer" / Segment) { beer =>
      get {
        complete {
          (core ? Get(beer)).mapTo[Option[Beer]]
        }
      }
    }
  }

}
