package org.opensplice.mobile.ddschat;

import android.app.Application;
import org.omg.dds.core.ServiceEnvironment;
import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.domain.DomainParticipantFactory;
import org.omg.dds.sub.*;
import org.omg.dds.pub.*;
import org.omg.dds.topic.Topic;

import org.opensplice.mobile.demo.idl.*;

public class ChatApplication extends Application {
	
	DataReader<ChatMessage> dr;
	DataWriter <ChatMessage> dw;
	DomainParticipant dp;
	final static int domainID = Config.domainID;
	
	
	@Override
	public void onCreate() {
        super.onCreate();
		System.setProperty(ServiceEnvironment.IMPLEMENTATION_CLASS_NAME_PROPERTY,
	            Config.bootstrap);
	        ServiceEnvironment env = ServiceEnvironment.createInstance(
	            ChatApplication.class.getClassLoader());
	        DomainParticipantFactory dpf = 
	        		DomainParticipantFactory.getInstance(env);
	        
	        dp = dpf.createParticipant(domainID);
	        Topic<ChatMessage> topic = dp.createTopic(Config.chatTopicName,ChatMessage.class);
	        Publisher pub = dp.createPublisher();
	        Subscriber sub = dp.createSubscriber();	        
	        
	        dw = pub.createDataWriter(topic);
	        dr = sub.createDataReader(topic);	        	        		
	}
	
	public DataReader<ChatMessage> reader() {
		return this.dr;
	}
	
	public DataWriter<ChatMessage> writer() {
		return this.dw;
	}


    @Override
    public void onTerminate() {
        super.onTerminate();
        this.dp.close();
    }
}
