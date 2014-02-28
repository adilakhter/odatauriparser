package org.odata.jpa

import org.odata.uri.parser.{Property, Select, ResourcePath, ODataQuery}
import javax.persistence.EntityManager
import org.odata.jpa.model.{Capability, CapabilityKind}

//TODO: fix design. Its ONLY for the prototype
//TODO:  remove ast. use  JPAQueryTransformer to transform ast to JPQL first
//TODO: wrote iterative code as much as possible so that it is understood by all => fix it.
case class JPAQueryExecutor(val em:EntityManager, val ast: ODataQuery) {

  initContext //initializing context

  val queryBuilder = new StringBuffer
  val criteria: List[String] = List[String]()
  val paramMap: Map[String,String] = Map[String,String]()

  def execute():java.util.List[_] = {
    this
      .buildSelectCriteria
      .buildFromCriteria
      .run
  }

  def run: java.util.List[_] = {
    println("Executing query: " + queryBuilder.toString)
    em.createQuery(queryBuilder.toString).getResultList
  }

  def buildFromCriteria: JPAQueryExecutor = {
    val ODataQuery(_, ResourcePath(queryEntity, _,_), _) = ast
    queryBuilder.append("FROM ")
    queryBuilder.append(queryEntity)
    queryBuilder.append(" ")
    queryBuilder.append("as "+ queryEntity.charAt(0))
    queryBuilder.append(" ")

    this
  }

  def buildSelectCriteria:JPAQueryExecutor ={
    val ODataQuery(_, ResourcePath(queryEntity, _,_), queryOps) = ast //deconstucting queryOps from AST
    val props = queryOps.queryOperatorDef collect {case s:Select => s.properties} // finding out all the properties

    queryBuilder.append("SELECT") //Building Select Queries
    queryBuilder.append(" ")

    props.flatten.foreach{
      case Property(name) => queryBuilder.append(queryEntity.charAt(0)+"."+name + ", ")
    }

    fixTrailingComma //hack todo: use StringUtils

    this
  }

  def initContext = {
    em.getTransaction.begin()

    var typeHttp = new CapabilityKind("transport.endpoint.http")
    var typeFTP = new CapabilityKind("transport.endpoint.http")

    em.persist(typeHttp)
    em.persist(typeFTP)

    def capabilityHttpLive =  new Capability("http://live.sdl.com", typeHttp)
    def capabilityHttpStaging =  new Capability("http://staging.sdl.com", typeHttp)

    em.persist(capabilityHttpLive)
    em.persist(capabilityHttpStaging)

    em.getTransaction().commit()

    em.refresh(typeHttp)

  }

  def fixTrailingComma: Unit = {
    queryBuilder.deleteCharAt(queryBuilder.length()-2)
    queryBuilder.setCharAt(queryBuilder.length()-1, ' ' )
  }

}
