package org.opensplice.mobile.demo

import dds.prelude._
import dds.config.DefaultEntities._
import dds._
import dds.demo.DeviceInfo

import android.app.{ Activity, ListActivity }
import android.os.Bundle
import android.content.Intent
import android.view.{ View, Menu }
import android.widget.{ ArrayAdapter, ListView, EditText }

import scala.collection.JavaConversions._
import android.util.Log

class DiscoveryActivity extends ListActivity {
  var devices: ArrayAdapter[String] = null
  val topic = Topic[DeviceInfo](AppConfig.deviceInfoTopic)
  val drQos = DataReaderQos().withPolicies(Durability.TransientLocal, Reliability.Reliable)
  val dr = DataReader[DeviceInfo](topic, drQos)

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.discovery_main)
    devices = new ArrayAdapter[String](this, R.layout.discovery)
    setListAdapter(devices)

    dr.listen {
      case DataAvailable(_) => this.runOnUiThread(() => {
        devices.clear()
        val samples = dr.read()
        samples foreach (s => { val d = s.getData(); devices.add(d.deviceId) })
      })
    }
  }

  override def onResume() {
    super.onResume()
    devices.clear()
    dr.read.foreach(s => { val d = s.getData(); devices.add(d.deviceId) })
  }
  
  override def onCreateOptionsMenu(menu: Menu) = {
    getMenuInflater().inflate(R.menu.main, menu)
    true
  }

  override def onListItemClick(l: ListView, v: View, position: Int, id: Long) {
    val did: String = devices.getItem(position)
    val app = getApplication().asInstanceOf[OximeterApp]
    app.createEndpoints(did)
    val intent = new Intent(this, classOf[org.opensplice.mobile.demo.MainActivity])
    startActivity(intent)
  }

  override def onDestroy() {
    super.onDestroy()
    dr.deaf()
    dr.close()
    System.exit(0)
  }

  def get[T](id: Int) = findViewById(id).asInstanceOf[T]
}