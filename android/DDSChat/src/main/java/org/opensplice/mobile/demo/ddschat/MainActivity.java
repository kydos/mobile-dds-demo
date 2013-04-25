package org.opensplice.mobile.ddschat;

import java.util.concurrent.TimeoutException;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.*;

import org.omg.dds.core.event.DataAvailableEvent;
import org.omg.dds.sub.DataReader;
import org.omg.dds.sub.Sample;
import org.omg.dds.sub.Sample.Iterator;
import org.omg.dds.sub.SampleState;
import org.opensplice.mobile.demo.idl.ChatMessage;

public class MainActivity extends Activity {
	
	private static final String user = Config.defaultUserName;
	private final Handler handler = new Handler();
	private ArrayAdapter<String> chatMessages;

	public class ChatMessageListener extends ChatMessageDataListener {

		private DataReader<ChatMessage> dr;

		public ChatMessageListener() {
			ChatApplication app = (ChatApplication) getApplication();
			dr = app.reader();
		}

		@Override
		public void onDataAvailable(DataAvailableEvent<ChatMessage> dae) {
			final Iterator<ChatMessage> i = dr.read();

			Log.i(Config.MA_TAG, ">>> DataReaderListener.onDataAvailable");
			if (i.hasNext()) {
				Runnable dispatcher = new Runnable() {
					public void run() {
						while (i.hasNext()) {
							Sample<ChatMessage> s = i.next();

							if (s.getSampleState() == SampleState.NOT_READ) {
								ChatMessage cm = s.getData();
								chatMessages.add(cm.user + " >  " + cm.msg);
							}
						}						
					}
				};
				handler.post(dispatcher);
			}
		}
	
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		chatMessages = new ArrayAdapter<String>(this, R.layout.messages);
		chatMessages.add("Welcome to the DDS Chat Room");
		ListView mview = (ListView) findViewById(R.id.messageList);
		mview.setAdapter(chatMessages);
		ChatApplication app = (ChatApplication) getApplication();

		app.reader().setListener(new ChatMessageListener());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void sendChatMessage(View view) {
		EditText editText = (EditText) findViewById(R.id.message);
		String msg = editText.getText().toString();
		editText.setText("");
		ChatApplication app = (ChatApplication) getApplication();
		try {
			Log.i(Config.MA_TAG, ">>> Sending data " + msg);
			app.writer().write(new ChatMessage(user, msg));
		} catch (TimeoutException te) {
		}
	}

}
