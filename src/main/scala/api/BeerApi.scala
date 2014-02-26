package api

import core.BeerActor
import model.Beer
import spray.routing.HttpServiceActor
import spray.json._
import akka.util.Timeout
import scala.concurrent.duration._
import akka.pattern._
import spray.httpx.SprayJsonSupport._

class BeerApi extends HttpServiceActor with DefaultJsonProtocol {
  import BeerActor._
  import context.dispatcher

  val core = context.actorOf(BeerActor.props, "core")
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
        entity(as[Beer]) { beer =>
          complete {
            (core ? Put(beer.name, beer)).mapTo[Beer]
          }
        }
      }
    } ~
    path("beer" / Segment) { beer =>
      get {
        complete {
          (core ? Get(beer)).mapTo[Option[Beer]]
        }
      }
    } ~
    (get & path("beer" / "random")) {
      complete {
        (core ? GetRandom).mapTo[Option[Beer]]
      }
    }
  }

}
