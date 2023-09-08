package client;

import java.util.Hashtable;
import java.util.Random;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class Client{

    public static void main(String[] args){

        Hashtable<String,String> p= new Hashtable<String,String>();

        p.put("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory"); //nome della proprietà che fa riferimento al servizio dei nomi nel factory
        p.put("java.naming.provider.url", "tcp://127.0.0.1:61616");

        p.put("queue.Richiesta", "Richiesta");
        p.put("queue.Risposta", "Risposta");

        
        
        try{
            //Occupiamoci del contesto
            Context ctx;
            ctx= new InitialContext(p);

            QueueConnectionFactory qconnf=(QueueConnectionFactory)ctx.lookup("QueueConnection");

            Queue queueRequest=(Queue) ctx.lookup("Richiesta");
            Queue queueResponse=(Queue) ctx.lookup("Risposta");

            QueueConnection qconn=qconnf.createQueueConnection();
            qconn.start();

            QueueSession qsession=qconn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            
            //RECEIVER
            QueueReceiver qreceiver=qsession.createReceiver(queueResponse);
            ClientListener listener= new ClientListener();

            //SENDER
            QueueSender qsender =qsession.createSender(queueRequest);
            //Creo il messaggio impostando l'header
            MapMessage message  = qsession.createMapMessage();

            for(int i=0;i<10;i++){
                if(Math.random()<0.5){
                    //al 50% faccio deposito altrimenti prelevo

                    //DEPOSITO
                    message.setString("operazione", "deposita");
                    Random r = new Random();
                    int randomvalue = r.nextInt(100);

                    message.setInt("valore",randomvalue);

                    //Settiamo JMSReplyTo
                    message.setJMSReplyTo(queueResponse);

                    //invio del messaggio
                    qsender.send(message);
                    System.out.println("[CLIENT] inviato messaggio");
                }else{

                    //PRELEVO
                    message.setString("operazione", "preleva");
                    message.setJMSReplyTo(queueResponse);
                    qsender.send(message);
                }
            }

        }catch(NamingException e ){
            e.printStackTrace();
        }catch(JMSException e){
            e.printStackTrace();
        }
        
    }
}