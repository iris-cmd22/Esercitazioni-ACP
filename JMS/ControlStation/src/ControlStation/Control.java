package ControlStation;

import java.util.Hashtable;
import java.util.Random;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Control{

    public static void main(String[] args){

        int N=20;

        //Comunicazione JMS
        Hashtable<String,String> p= new Hashtable<String,String>();
        p.put("java.naming.factory.initial","org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        p.put("java.naming.provider.url","tcp://127.0.0.1:61616");
        p.put("topic.control","control");

        //Creazione e setting del contesto iniziale con AbstractFactory
        try {
            Context jndiContext = new InitialContext(p);

            //Creazione della Queue Connection con pattern Abstract Factorys
            TopicConnectionFactory tcf=(TopicConnectionFactory) jndiContext.lookup("control");
            TopicConnection tc=tcf.createTopicConnection();

            //Creazione delle queue
            Topic control = (Topic) jndiContext.lookup("control");
            TopicSession tsess= tc.createTopicSession(false,Session.AUTO_ACKNOWLEDGE);

            //Creazione del messaggio 
            TextMessage mess =tsess.createTextMessage();

            //Scegliamo randomicamente il comando inviato
            String comm;
            Random r = new Random();
            int c=r.nextInt(2);

            for(int i=0;i<N;i++){
                if(c==0){
                    comm="startSensor";
                }else if(c==1){
                    comm="stopSensor";
                }else{
                    comm="read";
                }
                mess.setStringProperty("command",comm);
            }
            
            //Creazione dell Queue Publisher
            TopicPublisher tp=tsess.createPublisher(control);
            tp.send(mess);
                                     


        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        
    }
}