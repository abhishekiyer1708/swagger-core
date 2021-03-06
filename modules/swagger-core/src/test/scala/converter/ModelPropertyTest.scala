package converter

import converter.models._

import com.wordnik.swagger.converter._
import com.wordnik.swagger.model._

import com.wordnik.swagger.core.util._
import com.wordnik.swagger.annotations.ApiModelProperty

import java.util.Date

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import scala.beans.BeanProperty

@RunWith(classOf[JUnitRunner])
class ModelPropertyTest extends FlatSpec with Matchers {
  it should "extract properties" in {
    val models = ModelConverters.readAll(classOf[Family])

    models.size should be (3)
    val person = models.filter(m => m.name == "Person").head
    val employer = person.properties("employer")

    employer.`type` should be ("List")
    val employerItems = employer.items.getOrElse(fail("no items found"))
    employerItems.ref should be (Some("Employer"))

    val awards = person.properties("awards")
    awards.`type` should be ("List")
    val awardItems = awards.items.getOrElse(fail("no items found"))
    awardItems.`type` should be ("string")
  }

  it should "extract a primitive array" in {
    val models = ModelConverters.readAll(classOf[ModelWithPrimitiveArray])

    models.size should be (1)
    val props = models.filter(m => m.name == "ModelWithPrimitiveArray").head
    val longArray = props.properties("longArray")

    longArray.`type` should be ("Array")
    val longArrayItems = longArray.items.getOrElse(fail("no items found"))
    longArrayItems.`type` should be ("long")

    val intArray = props.properties("intArray")
    intArray.`type` should be ("Array")
    val awardItems = intArray.items.getOrElse(fail("no items found"))
    awardItems.`type` should be ("int")
  }

  it should "read a model property" in {
    val models = ModelConverters.readAll(classOf[IsModelTest])

    models.size should be (1)
    val props = models.filter(m => m.name == "IsModelTest").head
  }

  it should "read a scala object" in {
    val models = ModelConverters.readAll(classOf[Pet])

    models.size should be (1)
    val props = models.filter(m => m.name == "Pet").head

    println(JsonSerializer.asJson(props))
  }
}

case class Family (membersSince: Date, members: List[Person])

class Pet {
  var name: String = _
  var age: Int = 0
  var birthday: java.util.Date = _
}

case class Person (
  firstname: String, 
  lastname: String, 
  middlename: Option[String], 
  age: Int, 
  birthday: Date,
  employer: List[Employer],
  awards: List[String])

case class Employer (
  name: String,
  size: Int)

case class IsModelTest (
  is_happy: Boolean,
  name: String)