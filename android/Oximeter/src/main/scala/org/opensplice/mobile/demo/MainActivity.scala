package org.opensplice.mobile.demo

import android.app.Activity
import android.os.{ Bundle, Message, Handler }
import android.view.Menu
import android.widget.TextView
import android.util.Log
import android.graphics._

import scala.language.implicitConversions
import org.opensplice.mobile.demo.prelude._
import scala.collection.JavaConversions._
import org.omg.dds.sub.Sample

import com.androidplot.Plot

import com.androidplot.xy._
import com.androidplot.series._
import com.androidplot.xy._

import java.text._
import java.util.Arrays
import java.util.Date

class MainActivity extends Activity {

  var did: TextView = null
  var spO2: TextView = null
  var ppm: TextView = null
  var bpm: TextView = null
  var pleth: TextView = null

  var dynamicPlot: XYPlot = null
  var plethSeries: OximeterTimeSeries = null
  val handler = new Handler()

  val activityUpdater = new Runnable {
    def run() {

      plethSeries.refresh()
      val app = getApplication().asInstanceOf[OximeterApp]
      val odr = app.endpoints.oximetryDR
      val oximetry = odr.read()
      oximetry.find(_ => true) map { s =>
        val d = s.getData()
        did.setText(d.deviceId)
        spO2.setText(d.spO2.toString)
        bpm.setText(d.bpm.toString)
        pleth.setText(d.pleth.toString)
        ppm.setText(d.rr.toString)

      }
    }
  }

  val scheduler = new Thread(() => {
    var interrupted = false
    while (!interrupted) {
      interrupted =
        try {
          handler.post(activityUpdater)
          Thread.sleep(AppConfig.refreshPeriod)
          false
        } catch {
          case ie: InterruptedException => {
            true
          }

        }
    }
  })

  import scala.collection.JavaConversions._
  import scala.collection.JavaConverters._

  override def onCreate(savedInstanceState: Bundle) {

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    dynamicPlot = get[XYPlot](R.id.XYPlot)
    dynamicPlot.setTitle("Real-Time Pleth")
    // only display whole numbers in domain labels
    dynamicPlot.getGraphWidget().setDomainValueFormat(new DecimalFormat("0"));

    val app = getApplication().asInstanceOf[OximeterApp]
    val pdr = app.endpoints.plethDR
    plethSeries = new OximeterTimeSeries(AppConfig.timeSeriesTitle, pdr, dynamicPlot)

    dynamicPlot.addSeries(plethSeries,
      new LineAndPointFormatter(Color.rgb(0x33, 0x66, 0x99),
        null, //Color.rgb(0x33, 0x66, 0x99), 
        Color.rgb(0x66, 0x99, 0xff)))

    dynamicPlot.setDomainStepMode(XYStepMode.SUBDIVIDE);

    did = get[TextView](R.id.deviceId)
    spO2 = get[TextView](R.id.spO2)
    ppm = get[TextView](R.id.bpm)
    bpm = get[TextView](R.id.bpm)
    pleth = get[TextView](R.id.pleth)

    scheduler.start()
  }

  override def onCreateOptionsMenu(menu: Menu) = {
    super.onCreateOptionsMenu(menu)
    getMenuInflater().inflate(R.menu.main, menu)
    true
  }

  override def onDestroy() {    
    super.onDestroy()
    scheduler.interrupt()   
  }

  def get[T](id: Int) = findViewById(id).asInstanceOf[T]

}