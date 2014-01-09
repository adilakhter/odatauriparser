package org.odata.uri.parser.tests

import org.scalatest.FunSuite
import org.odata.uri.parser._

class OdataUriParseTests extends FunSuite{
  val p = new ODataUriParser
  val mainParser = p.oDataQuery

  //http://odata.io/odata.svc/Customers?$filter=indexof(CompanyName, 'lfreds') eq 1

  test("Parse /Customers?$filter=indexof(CompanyName, ‘lfreds’) eq 1"){

    val uri = "http://odata.io/odata.svc/Customers?$filter=indexof(CompanyName, 'lfreds') eq 1"
    val actual = p.parseThis(mainParser,uri).get
    println(uri + "=>" + actual)
    val expectedAst=
      ODataQuery(
        URL("http://odata.io/odata.svc"),
        ResourcePath("Customers",EmptyExp(),EmptyExp()),
        QueryOperations(
          List(
            Filter(
              EqualToExp(
                  CallExp(Property("indexof"),List(Property("CompanyName"), StringLiteral("'lfreds'")))
                , Number("1"))
            )
          )
        )
      )
    assert(actual == expectedAst)
  }


  test("Parse /Customers?$filter=concat(City, Country) eq 'Berlin, Germany'"){
    val uri = "http://odata.io/odata.svc/Customers?$filter=concat(City, Country) eq 'Berlin, Germany'"
    val actual = p.parseThis(mainParser,uri).get
    println(uri + "=>" + actual)
    val expectedAst=
      ODataQuery(
        URL("http://odata.io/odata.svc"),
        ResourcePath("Customers",EmptyExp(),EmptyExp()),
        QueryOperations(
          List(
            Filter(
              EqualToExp(
                  CallExp(Property("concat")
                , List(Property("City")
                , Property("Country")))
                , StringLiteral("'Berlin, Germany'"))))))

    assert(actual == expectedAst)
  }

  test("Parse /Products?$filter=not isSoldAt(ams)"){
    val uri = "http://odata.io/odata.svc/Products?$filter=not isSoldAt(ams)"
    val actual = p.parseThis(mainParser,uri).get
    println(uri + "=>" + actual)
    val expectedAst=
      ODataQuery(
        URL("http://odata.io/odata.svc"),
        ResourcePath("Products",EmptyExp(),EmptyExp()),
        QueryOperations(
          List(
            Filter(
              NotExp(CallExp(Property("isSoldAt"),List(Property("ams"))))))));

    assert(actual == expectedAst)
  }





  test("Parse /Products?$skip=2&$top=2"){

    val uri = "http://services.odata.org/OData/OData.svc/Products?$skip=2&$top=2"
    val actual = p.parseThis(p.oDataQuery,uri).get
    val expectedAst= ODataQuery(URL("http://services.odata.org/OData/OData.svc"),ResourcePath("Products",EmptyExp(),EmptyExp()),QueryOperations(List(Skip(Number("2")), Top(Number("2")))))

    assert(actual == expectedAst)
  }

  test("Parse /Customers?$top=2&$filter=concat(City, Country) eq 'Berlin, Germany'"){

    val uri = "http://odata.io/odata.svc/Schema(231)/Customer?$top=2&$filter=concat(City, Country) eq 'Berlin, Germany'"
    val actual = p.parseThis(mainParser,uri).get
    println(uri + "=>" + actual)
    val expectedAst=
      ODataQuery(
        URL("http://odata.io/odata.svc"),
        ResourcePath("Schema",Number("231"),ResourcePath("Customer",EmptyExp(),EmptyExp())),
        QueryOperations(
          List(Top(Number("2")),
          Filter(
            EqualToExp(
              CallExp(
                  Property("concat")
                , List(Property("City"), Property("Country"))
              )
              , StringLiteral("'Berlin, Germany'"))))))


    assert(actual == expectedAst)

  }

  test("Parse /Products?$select=Name"){

    val uri = "http://services.odata.org/OData.svc/Products?$select=Name,Price"
    val actual = p.parseThis(mainParser,uri).get
    val expectedAst =
      ODataQuery(
        URL("http://services.odata.org/OData.svc"),
        ResourcePath("Products",EmptyExp(),EmptyExp()),
        QueryOperations(List(Select(List(Property("Name"), Property("Price"))))))

    assert(actual == expectedAst)
  }


}
