package org.odata.uri.parser.tests

import org.scalatest.FunSuite
import org.odata.uri.parser._

class FilterTests extends FunSuite {
  val p = new ODataUriParser
  val mainParser = p.filter


  test("Simple $filter operation") {
    val query = "$filter=name eq 'adil'"
    val expectedAST =
      Filter(
        EqualToExp(
          Property("name"),
          StringLiteral("'adil'")
      )
    )
    val actualAST:Expression = p.parseThis(mainParser, query).get

    assert(actualAST == expectedAST)
  }

  test("Parse $filter=Price le 200 and Price gt 3.5") {
    val query = "$filter=Price le 200 and Price gt 3.5"
    val expectedAST =
      Filter(
        AndExp(
          LessOrEqualToExp(
            Property("Price"),
            Number("200")
          )
          , GreaterThanExp(
              Property("Price"),
              Number("3.5")
            )
        )
      )
    val actualAST:Expression = p.parseThis(mainParser, query).get
    assert(actualAST == expectedAST)
  }



  test("Parse $filter= moreThan(Price, 10)") {
    val query = "$filter= moreThan(Price, 10)"
    val expectedAST =
      Filter(
          CallExp(
            Property("moreThan")
          , List(Property("Price"), Number("10"))
          )
      )
    val actualAST:Expression = p.parseThis(mainParser, query).get
    assert(actualAST == expectedAST)
  }

  test("Parse $filter=toupper(CompanyName) eq ‘ALFREDS FUTTERKISTE’") {
    val query = "$filter = toupper(CompanyName) eq 'ALFREDS FUTTERKISTE'"
    val expectedAST =
      Filter(
        EqualToExp(
              CallExp(
                  Property("toupper")
                  , List(Property("CompanyName"))
              )
            , StringLiteral("'ALFREDS FUTTERKISTE'")
        )
      )
    val actualAST:Expression = p.parseThis(mainParser, query).get
    assert(actualAST == expectedAST)
  }




}
