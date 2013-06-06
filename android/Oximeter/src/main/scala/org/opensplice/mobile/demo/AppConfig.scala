package org.opensplice.mobile.demo

import dds.config.DefaultEntities._
import dds.prelude._
import dds._

object AppConfig {
  
  val oximeterTopic= "Oximetry"
  val plethTopic = "Pleth"
  val deviceInfoTopic = "DeviceInfo"
    
  val timeSeriesTitle = "Real-Time Pleth"
    
  val refreshPeriod = 250 //msecs
  
  val plethHistory = (3000/13).toInt 
  
  val deviceId = "O-1"
    
  val oximeterDRQoS = DataReaderQos()
      
  val plethDRQos = DataReaderQos().withPolicy(History.KeepLast(plethHistory))
      
}