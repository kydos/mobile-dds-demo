package org.opensplice.mobile.ddschat;

import org.omg.dds.core.event.LivelinessChangedEvent;
import org.omg.dds.core.event.RequestedDeadlineMissedEvent;
import org.omg.dds.core.event.RequestedIncompatibleQosEvent;
import org.omg.dds.core.event.SampleLostEvent;
import org.omg.dds.core.event.SampleRejectedEvent;
import org.omg.dds.core.event.SubscriptionMatchedEvent;
import org.omg.dds.sub.DataReaderListener;
import org.opensplice.mobile.demo.idl.ChatMessage;

public abstract class ChatMessageDataListener implements DataReaderListener<ChatMessage> {
	
	@Override
	public void onLivelinessChanged(LivelinessChangedEvent<ChatMessage> arg0) {		}

	@Override
	public void onRequestedDeadlineMissed(
			RequestedDeadlineMissedEvent<ChatMessage> arg0) {		}

	@Override
	public void onRequestedIncompatibleQos(
			RequestedIncompatibleQosEvent<ChatMessage> arg0) {		}

	@Override
	public void onSampleLost(SampleLostEvent<ChatMessage> arg0) {		}

	@Override
	public void onSampleRejected(SampleRejectedEvent<ChatMessage> arg0) {		}

	@Override
	public void onSubscriptionMatched(
			SubscriptionMatchedEvent<ChatMessage> arg0) {		}

}