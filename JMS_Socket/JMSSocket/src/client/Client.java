package client;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Client{

    public static void main(String[] args){

        try{

            if(args.length!=2){
                System.out.println("Dammi i parametri!");
                return;
            }else if(Integer.parseInt(args[0])<0 || Integer.parseInt(args[0])>100){
                System.out.println("Dato out of bounds!");
                return;
            }

            Hashtable<String,String> p = new Hashtable<String,String>();
            p.put("java.naming.factory.initial","org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            p.put("java.naming.provider.url","tcp://127.0.0.1:61616");
            p.put("topic.storage","storage");

            //Definizione del contesto e utilizzo di Abstract factory per la creazione di una connessione
            Context jdniContext = new InitialContext(p);
            TopicConnectionFactory tcf= (TopicConnectionFactory) jdniContext.lookup("TopicConnectionFactory");
            TopicConnection tc =tcf.createTopicConnection();
            tc.start();

            //Creaione del topic e della Session
            Topic storage= (Topic) jdniContext.lookup("storage");
            TopicSession ts=tc.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            
            //Creazione del messaggio
            MapMessage mess=ts.createMapMessage();
            mess.setInt("dato", Integer.parseInt(args[0]));
            mess.setInt("porta",Integer.parseInt(args[1]));
            
            //Creazione del publisher e invio del messaggio
            TopicPublisher tp = ts.createPublisher(storage);
            tp.send(mess);
            System.out.println("[CLIENT] Messaggio inviato!");

            //Chidi connessione
            tc.close();


        }catch(NamingException e){
            e.printStackTrace();
        }catch(JMSException e){
            e.printStackTrace();
        }
    }
}