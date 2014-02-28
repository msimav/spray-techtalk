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
  implicit val beerFormat = jsonFormat3(Beer)
  implicit val timeout = Timeout(3 seconds)

  override def receive: Receive = runRoute {
    path("beer") {
      get {
        parameter('query ?) { query =>
          complete {
            query match {
              case Some(name) => (core ? Search(name)).mapTo[Option[Beer]]
              case None => (core ? GetAll).mapTo[Iterable[Beer]]
            }
          }
        }
      } ~
      post {
        entity(as[Beer]) { beer =>
          complete {
            (core ? Add(beer)).mapTo[Beer]
          }
        }
      }
    } ~
    path("beer" / IntNumber) { id =>
      get {
        complete {
          (core ? Get(id)).mapTo[Option[Beer]]
        }
      } ~
      put {
        entity(as[Beer]) { beer =>
          complete {
            (core ? Update(id, beer)).mapTo[Option[Beer]]
          }
        }
      } ~
      delete {
        complete {
          (core ? Delete(id)).mapTo[Option[Beer]]
        }
      }
    }
  }

}
