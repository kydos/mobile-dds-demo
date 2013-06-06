package org.opensplice.mobile.demo

import com.androidplot.series.XYSeries
import org.omg.dds.sub.DataReader
import dds.demo.Pleth
import scala.collection.JavaConversions._
import com.androidplot.xy._
import android.util.Log

class OximeterTimeSeries(val title: String, val dr: DataReader[Pleth], val plot: XYPlot) extends  XYSeries {
  
	private var data = new Array[Float](AppConfig.plethHistory)
	
	var mySize = 0	
	override def getTitle = title
	
	override def size = mySize
	
	override def getX(index: Int): Number = index	  	   	
	
	override def getY(index: Int): Number = {
	  data(index)
	}
	
	def refresh(): Array[Float] = { 
	  var index = 0
	  dr.read.foreach { s => {
	    data(index) = s.getData().pleth
	    index += 1
	    } 	  	
	  }
	  mySize = index
	  plot.redraw()
	  data
	}	
}