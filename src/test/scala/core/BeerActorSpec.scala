package core

import akka.testkit.{TestActorRef, ImplicitSender, TestKit}
import akka.actor.ActorSystem
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import model.Beer
import scala.util.Random

class BeerActorSpec extends TestKit(ActorSystem("test-system"))
                    with ImplicitSender
                    with WordSpecLike
                    with Matchers
                    with BeforeAndAfterAll {
  import core.BeerActor._

  override def afterAll = {
    system.shutdown()
  }

  trait ActorInTest {
    val actorRef = TestActorRef(BeerActor.props)
  }

  "BeerActor" when {

    "empty" must {

      "fail returning beer"  in new ActorInTest {
        actorRef ! Get(3)
        expectMsg(None)
      }

      "fail searching beer" in new ActorInTest {
        actorRef ! Search(Random.nextString(10))
        val iterable = expectMsgType[Option[Beer]]
        iterable shouldBe empty
      }

      "fail update" in new ActorInTest {
        actorRef ! Update(3, genBeer)
        expectMsg(Option.empty[Beer])
      }

      "fail delete" in new ActorInTest {
        actorRef ! Delete(3)
        expectMsg(Option.empty[Beer])
      }

      "add given beer" in new ActorInTest {
        val beer = genBeer

        actorRef ! Add(beer)
        val msg = expectMsgType[Beer]
        msg.id should not be empty

        val id = msg.id.get
        actorRef ! Get(id)
        expectMsg(Some(beer.copy(id = Some(id))))
      }

    }

    "have data" must {

      "return list of all beers" in new ActorInTest {
        val beer1 = genBeer
        val beer2 = genBeer

        actorRef ! Add(beer1)
        val msg1 = expectMsgType[Beer]
        msg1.id should not be empty

        actorRef ! Add(beer2)
        val msg2 = expectMsgType[Beer]
        msg2.id should not be empty

        actorRef ! GetAll
        val list = expectMsgType[Iterable[Beer]]
        list should contain theSameElementsAs List(msg1, msg2)
      }

      "search beer" in new ActorInTest {
        val beer = genBeer

        actorRef ! Add(beer)
        val r1 = expectMsgType[Beer]

        actorRef ! Search(beer.name)
        val r2 = expectMsgType[Option[Beer]]
        r2 should not be empty

        r1 should === (r2.get)
      }

      "update beer" in new ActorInTest {
        val beer1 = genBeer
        val beer2 = genBeer

        actorRef ! Add(beer1)
        val msg1 = expectMsgType[Beer]
        msg1.id should not be empty

        actorRef ! Update(msg1.id.get, beer2)
        val r2 = expectMsgType[Option[Beer]]
        r2 should not be empty
      }

      "delete beer" in new ActorInTest {
        val beer = genBeer

        actorRef ! Add(beer)
        val msg = expectMsgType[Beer]
        msg.id should not be empty

        val id = msg.id.get
        actorRef ! Delete(id)
        val r = expectMsgType[Option[Beer]]
        r should not be empty

        r.get should === (beer.copy(id = Some(id)))
      }
    }

  }

  private def genBeer = Beer(None, Random.nextString(10), Random.nextString(10))

}
