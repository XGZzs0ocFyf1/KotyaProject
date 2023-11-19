package example.custom

import zio.json._

import java.sql.Timestamp
import java.time.{Instant, LocalDateTime}


object CustomEncoders extends App {


  sealed trait Fruit extends Product with Serializable

  case class Banana(
    curvature: Double,
    numbers: Seq[Int],
    gathered: Timestamp
  ) extends Fruit

  case class Apple(poison: Boolean) extends Fruit
  case class User(id: Long, email: String, name: Option[String])
  object User{
    implicit val decoder: JsonDecoder[User] = DeriveJsonDecoder.gen[User]
    implicit val encoder: JsonEncoder[User] = DeriveJsonEncoder.gen[User]
  }

  case class ShoppingCart(
    user: User,
    fruits: List[Fruit]
  )

  object ShoppingCart{
    implicit val decoder: JsonDecoder[ShoppingCart] = DeriveJsonDecoder.gen[ShoppingCart]
    implicit val encoder: JsonEncoder[ShoppingCart] = DeriveJsonEncoder.gen[ShoppingCart]
  }


  object Fruit {
    implicit val awesomeDecoder: JsonDecoder[Timestamp] = JsonDecoder[Long].map(x => Timestamp.from(Instant.ofEpochMilli(x)))
    implicit val awesomeEncoder: JsonEncoder[Timestamp] = JsonEncoder[Int].contramap(x => x.getNanos)

    implicit val decoder: JsonDecoder[Fruit] = DeriveJsonDecoder.gen[Fruit]
    implicit val encoder: JsonEncoder[Fruit] = DeriveJsonEncoder.gen[Fruit]
  }

  val theJson = s"""{ "Banana":{ "curvature": 0.5 , "numbers": [1,2,3], "gathered": ${System.currentTimeMillis()}  }}}"""
  val shoppingCart =
    s"""{
       |"user": {
       |  "id": 1,
       |
       |  "email": "mailMe@gmail.com"
       |},
       |"fruits": [
       |    {
       |    "Banana": { "curvature": 0.5 , "numbers": [1,2,3], "gathered": ${System.currentTimeMillis()}  }
       |    }
       |  ]
       |
       |
       |
       |}""".stripMargin

  val x = theJson.fromJson[Fruit]

  val cart = shoppingCart.fromJson[ShoppingCart]

  val anotherCart = ShoppingCart(
    user = User(123, "customEmail", Option("Test User")),
    fruits = List(
      Banana(0.4, Seq(1, 2, 3, 4, 5), Timestamp.valueOf(LocalDateTime.now)),
      Apple(false)
    )
  )

  println(s"cart = ${cart.toJsonPretty}")
  println(s"another cart = ${anotherCart.toJsonPretty}")

}
