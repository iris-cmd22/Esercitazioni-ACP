package Proxy;

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

import interfaccia.IService;

public class Receiver implements Runnable{

    private IService servizio;

    public Receiver(IService servizio){
        this.servizio=servizio;
    }
    
	@Override
	public void run() {

        Hashtable<String,String> properties=new Hashtable<String,String>();
        properties.put("java.naming.factory.initial","org.apache.activemq.jndi.ActiveMQInitialContext");
        properties.put("java.naming.provider.url","tcp://127.0.0.1:61616");
        properties.put("queue.richiesta","richiesta");

        try{

            //Creazione del Context iniziale
            Context jndiContex= new InitialContext(properties);
            QueueConnectionFactory qcf=(QueueConnectionFactory) jndiContex.lookup("QueueConnectionFactory");

            //Creazione della queue e quindi della sua connessione e la avviamo
            Queue prod=(Queue)jndiContex.lookup("richiesta");
            QueueConnection qc=qcf.createQueueConnection();
            qc.start();

            //Creazione della sessione, creo quindi il receiver
            QueueSession qs= qc.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);
            QueueReceiver receiver=qs.createReceiver(prod);
            
            //Logica di recezione
            MyListener listener = new MyListener(servizio);
            receiver.setMessageListener(listener);


        }catch(NamingException e){
            e.printStackTrace();
        }catch(JMSException e){
            e.printStackTrace();
        }
		
		
			
	}
}
