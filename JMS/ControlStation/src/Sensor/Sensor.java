package Sensor;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import Coda.Coda;
import CodaImpl.CodaCircolare;

public class Sensor {

    public static void main(String[] args){

        int D=5;

        try {

        Hashtable<String,String> p=new Hashtable<String,String>();
        p.put("java.naming.factory.initial","org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        p.put("java.naming.provider.url","tcp://127.0.0.1:61616");
        p.put("topic.control","control");


        //Creazione del contesto
        Context jndiContext = new InitialContext(p);

        //Creazione dela connection con abstract factory
        TopicConnectionFactory tcf= (TopicConnectionFactory) jndiContext.lookup("TopicConnectionFactory");
        TopicConnection tc=tcf.createTopicConnection();

        //Creazione della topic
        Topic command= (Topic) jndiContext.lookup("control");
        TopicSession ts= tc.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

        //Creazione del messaggio
        TextMessage comm= ts.createTextMessage();

        //Creazione della Subscriber
        TopicSubscriber sub=ts.createSubscriber((Topic) command);

        //Creazione della coda
        Coda coda=new CodaCircolare(D);

        //Utilizzo  e set del listener
        MyListener listener=new MyListener(coda);
        sub.setMessageListener(listener);

        //Creazione del Thread che estrae i valori dalla coda e li scrive su un file csv
        TExecutor t=new TExecutor(coda);
        t.run();


        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
    
}
