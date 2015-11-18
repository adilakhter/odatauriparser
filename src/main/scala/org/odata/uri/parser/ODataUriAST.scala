package org.odata.uri.parser

import scala.util.parsing.input.Positional

  abstract class SourceNode extends Positional

  case class ODataQuery(val serviceUrl: Expression, val resourcePath: Expression, val queryOperatorDef:QueryOperations) extends SourceNode

  abstract class Expression extends SourceNode

  abstract class QueryOperation(val op: String) extends Expression

  case class QueryOperations(queryOperatorDef: Seq[Expression]) extends Expression

  case class Filter(val p: Expression) extends QueryOperation("filter")

  case class Top(val p: Expression) extends QueryOperation("top")

  case class Skip(val p: Expression) extends QueryOperation("skip")

  case class OrderByAsc(val properties: Seq[Property]) extends QueryOperation("orderBy")

  case class OrderByDesc(val properties: Seq[Property]) extends QueryOperation("orderBy")

  case class Select(properties: Seq[Property]) extends QueryOperation("select")

  case class EmptyExp() extends Expression

  case class ResourcePath(name: String, keyPredicate: Expression, childEntity: Expression) extends Expression

  abstract class BinaryExpression(val op: String) extends Expression {
    def left: Expression
    def right: Expression
  }

  abstract class UnaryExpression(val op: String) extends Expression {
    def exp: Expression
  }
  case class NotExp(exp: Expression) extends UnaryExpression("!")

  case class AndExp(left: Expression, right: Expression) extends BinaryExpression("&&")

  case class OrExp(left: Expression, right: Expression) extends BinaryExpression("||")

  case class PlusExp(left: Expression, right: Expression) extends BinaryExpression("+")

  case class MinusExp(left: Expression, right: Expression) extends BinaryExpression("-")

  case class MultiplyExp(left: Expression, right: Expression) extends BinaryExpression("*")

  case class DivideExp(left: Expression, right: Expression) extends BinaryExpression("/")

  case class ModExp(left: Expression, right: Expression) extends BinaryExpression("%")

  case class GreaterThanExp(left: Expression, right: Expression) extends BinaryExpression(">")

  case class LessThanExp(left: Expression, right: Expression) extends BinaryExpression("<")

  case class EqualToExp(left: Expression, right: Expression) extends BinaryExpression("=")

  case class NotEqualToExp(left: Expression, right: Expression) extends BinaryExpression("!=")

  case class GreaterOrEqualToExp(left: Expression, right: Expression) extends BinaryExpression(">=")

  case class LessOrEqualToExp(left: Expression, right: Expression) extends BinaryExpression("<=")

  case class CallExp(base: Expression, args: Seq[Expression]) extends Expression

  case class Property(value: String) extends Expression

  case class Number(value: String) extends Expression

  case class URL(value: String) extends Expression

  case class StringLiteral(value: String) extends Expression

  case class TrueExpr() extends Expression

  case class FalseExpr() extends Expression