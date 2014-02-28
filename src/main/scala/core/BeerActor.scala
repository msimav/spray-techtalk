package core

import akka.actor.{Props, Actor}
import model.Beer
import scala.util.Random

object BeerActor {
  sealed trait Request
  case object GetAll extends Request
  case class Get(id: Int) extends Request
  case class Search(name: String) extends Request
  case class Add(beer: Beer) extends Request
  case class Update(id: Int, beer: Beer) extends Request
  case class Delete(id: Int) extends Request

  def props: Props = Props[BeerActor]
}

class BeerActor extends Actor {
  import BeerActor._

  var beers = Map.empty[Int, Beer]

  def receive: Receive = {
    case GetAll =>
      sender ! beers.values

    case Get(id) =>
      val beer = beers get id
      sender ! beer

    case Search(name) =>
      val beer = beers.values.filter(_.name == name).headOption
      sender ! beer

    case Add(b) =>
      val id = nextId
      val beer = b.copy(id = Some(id))
      beers += (id -> beer)
      sender ! beer

    case Update(id, b) if beers contains id =>
      val beer = b.copy(id = Some(id))
      beers += (id -> beer)
      sender ! Some(beer)

    case Update(_, _) =>
      sender ! Option.empty[Beer]

    case Delete(id) =>
      val beer = beers get id
      beers -= id
      sender ! beer

  }

  private def nextId: Int = {
    val n = Random.nextInt(Int.MaxValue)
    if (beers contains n) nextId
    else n
  }
}
