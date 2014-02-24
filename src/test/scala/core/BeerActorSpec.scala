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
      expectMsgType[Failure]
    }

    "fail returning random beer if empty"  in {
      val actorRef = TestActorRef[BeerActor]
      actorRef ! GetRandom
      expectMsgType[Failure]
    }

    "put given beer" in {
      val actorRef = TestActorRef[BeerActor]
      val beer = mock[Beer]

      actorRef ! Put("mock", beer)
      expectMsg(Ok)

      actorRef ! Get("mock")
      expectMsg(Success(beer))
    }

    "return random beer" in {
      val actorRef = TestActorRef[BeerActor]
      val beer1 = mock[Beer]
      val beer2 = mock[Beer]

      actorRef ! Put("mock1", beer1)
      expectMsg(Ok)
      actorRef ! Put("mock2", beer2)
      expectMsg(Ok)

      actorRef ! GetRandom
      expectMsgAnyOf(Success(beer1), Success(beer2))
    }

  }

}
