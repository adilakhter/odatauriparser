package org.odata.jpa.model

import javax.persistence._

@Entity
@Table(name = "CAPABILITY")
class Capability(capabilityValue: String, capabilityType: CapabilityKind) {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "CAPABILITY_ID", updatable = false, nullable = false)
  var id: Int = _

  @Column(name = "VALUE")
  var value: String = capabilityValue

  @ManyToOne
  @JoinColumn(name = "CAPABILITY_KIND_ID")
  var kind: CapabilityKind  = capabilityType

  def this() = this (null, null)

  override def toString = id + " = " + value + " " + kind.capabilityName

  def canEqual(other: Any): Boolean = other.isInstanceOf[Capability]

  override def equals(other: Any): Boolean = other match {
    case that: Capability =>
      (that canEqual this) &&
        id == that.id &&
        value == that.value &&
        kind == that.kind
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(id, value, kind)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}