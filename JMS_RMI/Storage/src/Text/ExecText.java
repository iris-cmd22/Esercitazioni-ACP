package Text;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ExecText {
    
    public static void main(String[] args){

        try{

            Hashtable<String,String> p= new Hashtable<String,String>();
            p.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            p.put("java.naming.provider.url","tcp://127.0.0.1:61616");
            p.put("topic.text","text");

            //Inizializzazione e set del contesto
            Context jndiContext = new InitialContext(p);

            //Creazione della connessione con Abstract Factory
            TopicConnectionFactory tcf= (TopicConnectionFactory) jndiContext.lookup("TopicConnectionFactory");
            TopicConnection tc= tcf.createTopicConnection();
            tc.start();

            //Creazione della Topic
            Topic text = (Topic) jndiContext.lookup("text");
            TopicSession textsess = tc.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            //Creazione della Subscriber
            TopicSubscriber ts = textsess.createSubscriber(text);

            //Set del Message Listener sulla topic scelta
            MyListener listener = new MyListener();
            ts.setMessageListener(listener);

        }catch(NamingException | JMSException e){
            e.printStackTrace();
        }

        


    }
}
