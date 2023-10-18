package Extractor;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Extractor {

    public static void main(String[] args){

        try{

            Hashtable<String,String> p = new Hashtable<String,String>();
            p.put("java.naming.factory.initial","org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            p.put("java.naming.provider.url","tcp://127.0.0.1:61616");
            p.put("topic.data","data");
            p.put("topic.temp","temperature");
            p.put("topic.press","pressure");

            //Definizione del contesto
            Context jndiContext = new InitialContext(p);
            
            //Creazione della connessione con AbstractFactory
            TopicConnectionFactory tcf = (TopicConnectionFactory) jndiContext.lookup("TopicConnectionFactory");
            TopicConnection tc = tcf.createTopicConnection();
            tc.start();

            //Definizione della Topic data e della relativa Session
            Topic data = (Topic) jndiContext.lookup("data");
            TopicSession ts=tc.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            //Creazione del subscriber
            TopicSubscriber sub=ts.createSubscriber(data);

            //Definizione della Topic temperature della relativa Session
            Topic temp = (Topic) jndiContext.lookup("temp");
            TopicSession tst=tc.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            
            //Definizione della Topic pressure della relativa Session
            Topic press = (Topic) jndiContext.lookup("press");
            TopicSession tsp=tc.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            //Creazione del publisher
            TopicPublisher tt = tst.createPublisher(temp);
            TopicPublisher tp =tsp.createPublisher(press);

            //Set e chiamata del Listener
            MyListener listener = new MyListener(tt,tst,tp,tsp);
            sub.setMessageListener(listener); //Ascolta la Topic data
            System.out.println("[EXTRACTOR] Pronto!");


        }catch(NamingException e){
            e.printStackTrace();
        }catch(JMSException e){
            e.printStackTrace();
        }

    }
    
}
