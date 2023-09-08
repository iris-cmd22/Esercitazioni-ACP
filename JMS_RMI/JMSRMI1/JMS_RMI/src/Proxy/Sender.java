package Proxy;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Sender implements Runnable{

    private String messageToSend;

    Sender(String messageToSend){
        super();
        this.messageToSend=messageToSend;
    }
	
	@Override
	public void run() {
		
        //Hashtable per il naming service
        Hashtable<String,String> properties= new Hashtable<String,String>();
        properties.put("java.naming.factory.initial","org.apache.activemq.jndi.ActiveMQInitialContext");
        properties.put("java.naming.provider.url","tcp://127.0.0.1:61616");
        properties.put("queue.richiesta","risposta");

        try{

            //Creazione Contesto e utilizzo del pattern AbstractFactory
            Context jndiContext=new InitialContext(properties);
            QueueConnectionFactory qcf=(QueueConnectionFactory) jndiContext.lookup("QueueConnectionFactory");
            Queue cons= (Queue) jndiContext.lookup("risposta");
            QueueConnection qc=qcf.createQueueConnection();
            qc.start();

            QueueSession qs=qc.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);
            QueueSender senderCons = qs.createSender(cons);

            TextMessage message = qs.createTextMessage(messageToSend);
            senderCons.send(message);


        }catch(NamingException e){
            e.printStackTrace();
        }catch(JMSException e){
            e.printStackTrace();
        }
		
	}

}