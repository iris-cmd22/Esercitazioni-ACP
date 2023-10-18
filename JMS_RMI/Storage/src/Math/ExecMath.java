package Math;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.*;



public class ExecMath {

    public static void main(String[] args){

        try{
            //Comunicazione JMS e creazione della Hashtable
            Hashtable<String,String> p= new Hashtable<String,String>();
            p.put("java.naming.factory.initial","org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            p.put("java.naming.provider.url","tcp://127.0.0.1:61616");
            p.put("topic.math","math");

            //Definizione del contesto iniziale
            Context jndiContext = new InitialContext(p);

            //Creazione della connessione con Abstract Factory
            TopicConnectionFactory tcf= (TopicConnectionFactory) jndiContext.lookup("TopicConnectionFactory");
            TopicConnection tc=tcf.createTopicConnection();
            tc.start();

            //Definiamo quindi le Topic 
            Topic math = (Topic) jndiContext.lookup("math");
            TopicSession mathsess = tc.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            //Facciamo la subscribe alla coda
            TopicSubscriber ts=mathsess.createSubscriber(math);

            //Settiamo il Listener legato a questa topic
            MyListener listener = new MyListener();
            ts.setMessageListener(listener);



        }catch(NamingException | JMSException e){
            e.printStackTrace();
        }

    }

    
}