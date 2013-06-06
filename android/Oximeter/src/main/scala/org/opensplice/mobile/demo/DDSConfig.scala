package org.opensplice.mobile.demo

object DDSConfig {
  val properties = List(
    ("dds.listeners.useTransportThread", "true"),
    /*
    ("ddsi.writers.heartbeatPeriod", "0.001"),
    ("ddsi.writers.nackResponseDelay", "0"),
    ("ddsi.readers.heartbeatResponseDelay", "0"),     
    */
    ("ddsi.timer.threadPool.size", "1"),
    ("ddsi.receiver.threadPool.size", "0"),
    ("ddsi.dataProcessor.threadPool.size", "0"),
    ("ddsi.acknackProcessor.threadPool.size", "0"),
    ("ddsi.receiver.udp.buffer.size", "512"),
    ("ddsi.participant.leaseDuration", "300000"))

  def setup() {
    properties foreach { p => System.setProperty(p._1, p._2) }
  }

}