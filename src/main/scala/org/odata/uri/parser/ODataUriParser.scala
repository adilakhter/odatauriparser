package org.odata.uri.parser

import scala.util.parsing.combinator.{PackratParsers, JavaTokenParsers}

class ODataUriParser extends JavaTokenParsers with PackratParsers {

  lazy val oDataQuery: PackratParser[SourceNode]={
    serviceURL ~ resourcePath ~ opt(queryOperationDef) ^^ {
      case uri ~ path ~ None => ODataQuery(uri, path, QueryOperations(Seq.empty))
      case uri ~ path ~ Some(exp) => ODataQuery(uri, path,QueryOperations(exp))
    }
  }

  lazy val queryOperationDef: PackratParser[Seq[Expression]] =
    "?" ~> repsep(filter | select | top | skip | orderBy, "&")

  lazy val filter: PackratParser[Expression] =
    "$filter" ~> "=" ~> predicate ^^ Filter

  lazy val top: PackratParser[Expression] =
    "$top" ~> "=" ~> number ^^ Top

  lazy val skip: PackratParser[Expression] =
    "$skip" ~> "=" ~> number ^^ Skip

  lazy val orderBy: PackratParser[Expression] = (
      "$orderby" ~> "="~> propertyList <~ "asc" ^^ OrderByAsc
    | "$orderby" ~> "="~> propertyList <~ "desc" ^^ OrderByDesc
    | "$orderby" ~> "="~> propertyList ^^ OrderByAsc
  )

  lazy val select: PackratParser[Expression] =
    "$select"~>"="~> propertyList ^^ Select


  lazy val serviceURL: PackratParser[Expression] =
    """^.*.svc""".r ^^ {
      case s => URL(s)
    }

  lazy val resourcePath: PackratParser[Expression] =(
      "/" ~> idn ~ ("(" ~> predicate <~ ")") ~ opt(resourcePath) ^^ {
          case Property(name) ~ keyPredicate ~ None => ResourcePath(name, keyPredicate, EmptyExp())
          case Property(name) ~ keyPredicate ~ Some(expr) => ResourcePath(name, keyPredicate, expr)
      }
    | "/" ~> idn~ opt(resourcePath) ^^ {
          case Property(e)~None => ResourcePath(e, EmptyExp(), EmptyExp())
          case Property(e)~Some(expr) => ResourcePath(e, EmptyExp(), expr)
      }
  )

  lazy val predicate: PackratParser[Expression] =
    predicate ~ ("and" ~> relExpression) ^^ {case l ~ r => AndExp(l, r)} |
    predicate ~ ("or" ~> relExpression) ^^ {case l ~ r => OrExp(l, r)} |
    relExpression

  lazy val relExpression: PackratParser[Expression] =
    relExpression ~ ("gt" ~> expression) ^^ {case l ~ r => GreaterThanExp(l, r)} |
    relExpression ~ ("lt" ~> expression) ^^ {case l ~ r => LessThanExp(l, r)} |
    relExpression ~ ("eq" ~> expression) ^^ {case l ~ r => EqualToExp(l, r)} |
    relExpression ~ ("ne" ~> expression) ^^ {case l ~ r => NotEqualToExp(l, r)} |
    relExpression ~ ("ge" ~> expression) ^^ {case l ~ r => GreaterOrEqualToExp(l, r)} |
    relExpression ~ ("le" ~> expression) ^^ {case l ~ r => LessOrEqualToExp(l, r)} |
    expression

  lazy val expression: PackratParser[Expression] =
    expression ~ ("add" ~> termExpression) ^^ {case l ~ r => PlusExp(l, r)} |
    expression ~ ("sub" ~> termExpression) ^^ {case l ~ r => MinusExp(l, r)} |
    termExpression

  lazy val termExpression: PackratParser[Expression] =
    termExpression ~ ("mul" ~> factor) ^^ {case a ~ b => MultiplyExp(a, b)} |
    termExpression ~ ("div" ~> factor) ^^ {case a ~ b => DivideExp(a, b)} |
    termExpression ~ ("mod" ~> factor) ^^ {case a ~ b => ModExp(a, b)} |
    factor

  lazy val factor: PackratParser[Expression] =
    ("not" ~> factor) ^^ NotExp |
    factor ~ ("(" ~> expressionList <~ ")") ^^ {case id ~ param => CallExp(id, param)} |
    number |
    boolean |
    string |
    idn |
    "(" ~> predicate <~ ")"


  lazy val expressionList: PackratParser[Seq[Expression]] = repsep(predicate, ",")

  lazy val propertyList: PackratParser[Seq[Property]] = repsep(idn, ",")

  lazy val idn: PackratParser[Property] = ident ^^ Property

  lazy val number: PackratParser[Number] = floatingPointNumber ^^ Number

  lazy val string: PackratParser[StringLiteral] = ("\'" + """([^"\p{Cntrl}\\]|\\[\\/bfnrt]|\\u[a-fA-F0-9]{4})*""" + "\'").r ^^ StringLiteral | stringLiteral ^^ StringLiteral

  lazy val boolean: PackratParser[Expression] = "true" ^^^ TrueExpr() | "false" ^^^ FalseExpr()

  def parseThis[T](p: PackratParser[T], s: String): ParseResult[T] = {
    this.parseAll(p, s)
  }


}
