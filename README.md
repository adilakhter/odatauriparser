# A Parser for ODATA URI

This project builds a parser for subset of ODATA URI. Main objective is to parse  a URI such as `http://odata.io/odata.svc/Schema(231)/Customer?$top=2&$filter=concat(City, Country) eq 'Berlin, Germany'`
and builds an AST as follows:

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


It uses Scala Parser Combinators to parse the URI string and builds an AST for that for further processing.

For more example, see unit tests included in the project (e.g., see `OdataUriParseTests.scala`).
