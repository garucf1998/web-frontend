package com.example.demo.service;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;

import javax.jms.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import enity.BenhNhan;
@Repository
public class GuiBenhNhan {

	 private final static String WIRE_LEVEL_ENDPOINT = "ssl://b-8ee547aa-31e3-4e46-9cbb-9fdb9d0c84df-1.mq.us-east-2.amazonaws.com:61617";
	 private final static String ACTIVE_MQ_USERNAME = "admin";
	 private final static String ACTIVE_MQ_PASSWORD = "admin20212021";
	 private BenhNhanService control=new BenhNhanService();
	


	public  void sendMessage(BenhNhan bn, String myqueue) throws JMSException {
		 
		final ActiveMQConnectionFactory connectionFactory =
		         createActiveMQConnectionFactory();
				 final PooledConnectionFactory pooledConnectionFactory = createPooledConnectionFactory(connectionFactory);

		// Establish a connection for the producer.
		 final Connection producerConnection = pooledConnectionFactory.createConnection();
		 producerConnection.start();
		
		 // Create a session.
		 final Session producerSession = producerConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		 
		
		 
		 final Destination producerDestination = producerSession.createQueue(myqueue);
		
		 // Create a producer from the session to the queue.
		 final MessageProducer producer = producerSession.createProducer(producerDestination);
		 producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		 
		 Gson gson = new GsonBuilder()
	    		    .setDateFormat("yyyy-MM-dd")
	    		    .create();
			String bntext = gson.toJson(bn);
		 // Create a message.
		 
		 final TextMessage producerMessage = producerSession.createTextMessage(bntext);
		
		 // Send the message.
		 producer.send(producerMessage);
		 System.out.println("Message sent.");
		
		 // Clean up the producer.
		 producer.close();
		 producerSession.close();
		 producerConnection.close();
		 pooledConnectionFactory.stop();
	}

	
	private static PooledConnectionFactory createPooledConnectionFactory(ActiveMQConnectionFactory connectionFactory) {
	 // Create a pooled connection factory.
		 final PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
		 pooledConnectionFactory.setConnectionFactory(connectionFactory);
		 pooledConnectionFactory.setMaxConnections(10);
		return pooledConnectionFactory;
	}
	
	private static ActiveMQConnectionFactory createActiveMQConnectionFactory() {
		 // Create a connection factory.
		 final ActiveMQConnectionFactory connectionFactory =
		         new ActiveMQConnectionFactory(WIRE_LEVEL_ENDPOINT);
		
		 // Pass the username and password.
		 connectionFactory.setUserName(ACTIVE_MQ_USERNAME);
		 connectionFactory.setPassword(ACTIVE_MQ_PASSWORD);
	 return connectionFactory;
	}
		
}
