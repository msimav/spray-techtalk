package core

import akka.actor.Actor
import model.Beer
import scala.util.Random

object BeerActor {
  sealed trait Request
  case class Get(name: String) extends Request
  case object GetRandom extends Request
  case class Put(name: String, beer: Beer) extends Request

  sealed trait Response
  case object Ok extends Response
  case class Success(beer: Beer) extends Response
  case class Failure(reason: String) extends Response
}

class BeerActor extends Actor {
  import BeerActor._

  var beers = Map.empty[String, Beer]

  def receive: Receive = {
    case Get(name) =>
      val beer = beers get name
      val r = beer map { Success(_) } getOrElse Failure(s"Beer $name does not exist")
      sender ! r

    case GetRandom =>
      val random = Random.shuffle(beers).headOption
      val r = random map { (x: (String, Beer)) => Success(x._2) } getOrElse Failure("There is no beer :(")
      sender ! r

    case Put(name, beer) =>
      beers += (name -> beer)
      sender ! Ok

  }
}
