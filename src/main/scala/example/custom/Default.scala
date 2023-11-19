package example.custom

import zio.json._
object Default extends App {


  sealed trait Fruit                   extends Product with Serializable
  case class Banana(curvature: Double) extends Fruit
  case class Apple(poison: Boolean)    extends Fruit

  object Fruit {
    implicit val decoder: JsonDecoder[Fruit] =
      DeriveJsonDecoder.gen[Fruit]

    implicit val encoder: JsonEncoder[Fruit] =
      DeriveJsonEncoder.gen[Fruit]
  }

  val json1         = """{ "Banana":{ "curvature":0.5 }}"""
  val json2         = """{ "Apple": { "poison": false }}"""
  val malformedJson = """{ "Banana":{ "curvature": true }}"""

  json1.fromJson[Fruit]
  json2.fromJson[Fruit]
  val x = malformedJson.fromJson[Fruit]

 val res =  List(Apple(false), Banana(0.4)).toJsonPretty
  println(x)
  println(res)
}
