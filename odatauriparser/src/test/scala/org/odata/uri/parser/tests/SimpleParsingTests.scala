package org.odata.uri.parser.tests

import org.scalatest.FunSuite
import org.odata.uri.parser._

class SimpleParsingTests extends FunSuite{
  val p = new ODataUriParser
  val mainParser = p.oDataQuery

  test("Basic URI with Resource Path") {
    val uri = "http://odata.io/odata.svc/Product"
    val expectedAst=
      ODataQuery(
        URL("http://odata.io/odata.svc"),
        ResourcePath("Product",EmptyExp(),EmptyExp()),
        QueryOperations(List())
      )
    assert(p.parseThis(mainParser,uri).get == expectedAst)
  }

  test("Basic URI with Resource Path with multiple Entity") {
    val uri = "http://odata.io/odata.svc/Schema/Product"
    val expectedAst=
      ODataQuery(
          URL("http://odata.io/odata.svc")
        , ResourcePath(
            "Schema"
            , EmptyExp()
            , ResourcePath(
                "Product"
               , EmptyExp()
               , EmptyExp()
              )
        )
        , QueryOperations(List())      //No additional query
      )
    assert(p.parseThis(mainParser,uri).get == expectedAst)
  }
}
