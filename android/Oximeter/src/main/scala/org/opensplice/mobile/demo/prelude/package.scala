package org.opensplice.mobile.demo

import android.os.Message
import android.os.Handler

import scala.language.implicitConversions

package object prelude {
	implicit class HandlerCallback(val lambda: Message => Boolean) extends Handler.Callback {
	  def handleMessage(msg: Message) = lambda(msg)
	} 
	
	implicit class Executor(val lambda: () => Unit) extends java.lang.Runnable {
	  def run() { lambda() }
	}
}