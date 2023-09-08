package Client;

import java.util.Hashtable;
import java.util.Random;

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
    
    
	@Override
	public void run() {
		
        Hashtable<String,String> properties = new Hashtable<String,String>();

		properties.put("java.naming.factory.initial","org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        properties.put("java.naming.provider.url","tcp://127.0.0.1:61616");
        properties.put("queue.richiesta","richiesta");

        try{
                //Configuriamo l'initial Context effettuando il lookup
                Context jndiContext = new InitialContext(properties);
                QueueConnectionFactory qcf = (QueueConnectionFactory) jndiContext.lookup("QueueConnectionFactory");
                
                //Creiamo la coda dei messaggi
                Queue prod = (Queue)jndiContext.lookup("richiesta");
                //Creiamo la connessione
                QueueConnection qc=qcf.createQueueConnection();
                
                //Creiamo la sessione  e creiao il sender
                QueueSession qs=qc.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                QueueSender senderProd = qs.createSender(prod);
                TextMessage message = qs.createTextMessage();

                for(int i=0;i<10;i++){

                    if(i%2==0){

                        Random r = new Random();
                        int valore=r.nextInt(50);

                        message.setText("Deposita-"+valore);
                        senderProd.send(message);

                        System.out.println("[SENDER] Inviata la richiesta di deposito: "+valore);

                    }else{

                        message.setText("Preleva");
                        senderProd.send(message);

                        System.out.println("[SENDER] Inviata la richiesta di prelievo");

                    }
                }

        }catch(NamingException e){
            e.printStackTrace();
        }catch(JMSException e){
            e.printStackTrace();
        }
			
	}
}
