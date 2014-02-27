package org.odata.jpa.model

import javax.persistence._
import java.util.List

@Entity
@Table(name = "CAPABILITY_KIND")
class CapabilityKind(name: String) {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "CAPABILITY_KIND_ID", updatable = false, nullable = false)
  var id: Int = _

  @Column(name = "CAPABILITY_NAME")
  var capabilityName = name

  @OneToMany(mappedBy="kind", orphanRemoval = true,cascade=Array(CascadeType.ALL), fetch = FetchType.EAGER )
  var capabilities:java.util.List[Capability] = new java.util.ArrayList[Capability]()

  def this() = this (null)

  override def toString = id + " = " + capabilityName


  def canEqual(other: Any): Boolean = other.isInstanceOf[CapabilityKind]

  override def equals(other: Any): Boolean = other match {
    case that: CapabilityKind =>
      (that canEqual this) &&
        id == that.id &&
        capabilityName == that.capabilityName
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(id, capabilityName)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}