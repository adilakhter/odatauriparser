package org.odata.uri.parser

object Main {

  def main(args: Array[String]) {
    val uri = "http://odata.io/odata.svc/Schema(231)/Customer?$top=2&$filter=concat(City, Country) eq 'Berlin, Germany'"

    val parser = new ODataUriParser
    val mainParser = parser.oDataQuery

    println(parser.parseThis(mainParser, uri))

  }

}
