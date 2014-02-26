package core

import akka.actor.{Props, Actor}
import model.Beer
import scala.util.Random

object BeerActor {
  sealed trait Request
  case class Get(name: String) extends Request
  case object GetAll extends Request
  case object GetRandom extends Request
  case class Put(name: String, beer: Beer) extends Request

  def props: Props = Props[BeerActor]
}

class BeerActor extends Actor {
  import BeerActor._

  var beers = Map.empty[String, Beer]

  def receive: Receive = {
    case Get(name) =>
      val beer = beers get name
      sender ! beer

    case GetRandom =>
      val random = Random.shuffle(beers).map(_._2).headOption
      sender ! random

    case GetAll =>
      val beerList = (beers.values map { _.name }).toList
      sender ! beerList

    case Put(name, beer) =>
      beers += (name -> beer)
      sender ! beer

  }
}
