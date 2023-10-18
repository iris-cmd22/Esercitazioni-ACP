package disk;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;

public class Disk {

    public static void main(String[] args){
        try{
            //Creazione Hashatble per il naming service
            Hashtable<String,String> p = new Hashtable<String,String>();
            p.put("java.naming.factory.initial","org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            p.put("java.naming.provider.url","tcp://127.0.0.1:61616");
            p.put("topic.storage","storage");

            //definizione del contesto iniziale
            Context jndiContext = new InitialContext(p);
            //Creazione della connessione tramite Abstract factory
            TopicConnectionFactory tcf=(TopicConnectionFactory) jndiContext.lookup("TopicConnectionFactory");
            TopicConnection tc = tcf.createTopicConnection();
            tc.start();

            //Definizione del Topic e relatica Session
            Topic storage = (Topic) jndiContext.lookup("storage");
            TopicSession ts= tc.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            
            //Creazione del subscriber 
            TopicSubscriber sub=ts.createSubscriber(storage);

            //Set del Listener sulla topic storage
            MyListener listener = new MyListener();
            sub.setMessageListener(listener);
            System.out.println("[DISK] Pronto!");

        }catch(NamingException e){
            e.printStackTrace();
        }catch(JMSException e){
            e.printStackTrace();
        }
    }
    
}
