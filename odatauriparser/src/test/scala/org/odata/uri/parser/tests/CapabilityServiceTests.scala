package org.odata.uri.parser.tests

import org.scalatest.FunSuite
import org.odata.uri.parser._
import org.odata.jpa.EntityManagerService
import org.odata.jpa.model.{Capability, CapabilityKind}
import org.slf4j.{Logger, LoggerFactory}

class CapabilityServiceTests extends FunSuite{

  def LOG:Logger = LoggerFactory.getLogger(classOf[CapabilityServiceTests])

  test("Basic capability tests") {
    val em = EntityManagerService.getEntityManager

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

    LOG.error(em.createQuery("From CapabilityKind", classOf[CapabilityKind] ).getResultList().toString)
    LOG.error(em.createQuery("From Capability", classOf[Capability] ).getResultList().toString)
    LOG.error("Capabilities: {}" + typeHttp.capabilities)

    em.close()

  }
}
