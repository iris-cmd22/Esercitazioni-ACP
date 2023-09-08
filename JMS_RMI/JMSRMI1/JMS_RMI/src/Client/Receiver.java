package Client;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Receiver implements Runnable {
	
	@Override
	public void run() {
		
        //Si configurano le proprietà del contesto e del provider
        Hashtable<String,String> properties = new Hashtable<String,String>();
        properties.put("java.naming.factory.initial","org.apache.activemq.jndi.ActiveMQInitialContext");
        properties.put("java.naming.provider.url","tcp://127.0.0.1:61616");
        properties.put("queue.risposta","risposta");

        try{

            //creazione del contesto con il pattern Abstract Factory
            Context jndiContext = new InitialContext(properties);
            QueueConnectionFactory qcf= (QueueConnectionFactory) jndiContext.lookup("QueueConnectionFactory");
            
            //Creazione della coda e della nsua connessione
            Queue cons = (Queue)jndiContext.lookup("risposta");
            QueueConnection qc = qcf.createQueueConnection();
            qc.start();

            //Creazione della sessione e la usiamo per creare il receiver
            QueueSession qs=qc.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);
            QueueReceiver receiver=qs.createReceiver(cons);
            receiver.setMessageListener(new MyListener());

        }catch(NamingException e){
            e.printStackTrace();
        }catch(JMSException e){
            e.printStackTrace();
        }

		
	}

}
