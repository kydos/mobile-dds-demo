package org.opensplice.mobile.demo

import android.app.Application
import dds.demo._
import dds.config.DefaultEntities.{defaultDomainParticipant, defaultPolicyFactory}
import dds.prelude._
import dds._

class OximeterEndpoints(did: String) {
  	        
  private val ot =Topic[Oximetry](AppConfig.oximeterTopic)  
  private val pt = Topic[Pleth](AppConfig.plethTopic)  
  
  implicit val sub = Subscriber(SubscriberQos().withPolicy(Partition(did)))
  val oximetryDR = DataReader(ot, AppConfig.oximeterDRQoS)
  val plethDR = DataReader(pt, AppConfig.plethDRQos)  
  
  def close() {
    dds.config.DefaultEntities.defaultDomainParticipant.close()    
  }
}


class OximeterApp extends Application {
  	
	var endpoints: OximeterEndpoints = null
	
	def createEndpoints(did: String) {
	  endpoints = new OximeterEndpoints(did)
	}
	
	def closeEndpoints() = endpoints.close()
	
	override def onCreate() {
	  DDSConfig.setup()		 
	}
	
	override def onTerminate() { 
	  endpoints.close()
	}
}