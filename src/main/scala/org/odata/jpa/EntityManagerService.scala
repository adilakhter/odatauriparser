package org.odata.jpa

import javax.persistence.{EntityManager, Persistence, EntityManagerFactory}


object EntityManagerService {

  def getEntityManagerFactory: EntityManagerFactory =  Persistence.createEntityManagerFactory("cdcapabilities-ds")

  def getEntityManager:EntityManager = getEntityManagerFactory.createEntityManager();
}
