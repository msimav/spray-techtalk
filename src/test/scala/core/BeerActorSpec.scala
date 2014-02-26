package core

import akka.testkit.{TestActorRef, ImplicitSender, TestKit}
import akka.actor.ActorSystem
import org.scalatest.WordSpecLike
import org.scalatest.mock.MockitoSugar
import model.Beer

class BeerActorSpec extends TestKit(ActorSystem("test-system"))
with ImplicitSender
with WordSpecLike
with MockitoSugar {
  import core.BeerActor._

  "BeerActor" must {

    "fail returning beer if empty"  in {
      val actorRef = TestActorRef[BeerActor]
      actorRef ! Get("bomonti")
      expectMsg(None)
    }

    "fail returning random beer if empty"  in {
      val actorRef = TestActorRef[BeerActor]
      actorRef ! GetRandom
      expectMsg(None)
    }

    "put given beer" in {
      val actorRef = TestActorRef[BeerActor]
      val beer = mock[Beer]

      actorRef ! Put("mock", beer)
      expectMsg(beer)

      actorRef ! Get("mock")
      expectMsg(Some(beer))
    }

    "return random beer" in {
      val actorRef = TestActorRef[BeerActor]
      val beer1 = mock[Beer]
      val beer2 = mock[Beer]

      actorRef ! Put("mock1", beer1)
      expectMsg(beer1)
      actorRef ! Put("mock2", beer2)
      expectMsg(beer2)

      actorRef ! GetRandom
      expectMsgAnyOf(Some(beer1), Some(beer2))
    }

    "return list of all beers" in {
      val actorRef = TestActorRef[BeerActor]
      val beer1 = mock[Beer]
      val beer2 = mock[Beer]

      actorRef ! Put("mock1", beer1)
      expectMsg(beer1)
      actorRef ! Put("mock2", beer2)
      expectMsg(beer2)

      actorRef ! GetAll
      expectMsg(List(beer1.name, beer2.name))
    }

  }

}
