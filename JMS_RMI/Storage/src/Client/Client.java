package Client;

import java.util.Hashtable;
import java.util.Random;

import javax.jms.*;
import javax.naming.*;

public class Client{

    public static void main(String[] args){

        if(args.length!=1){
            System.out.println("Inserisci comando!");
            return;
        }

        int NREQS=5;

        try {

            //Comunicazione JMS: creazione Hashtable
            Hashtable<String,String> p= new Hashtable<String,String>();
            p.put("java.naming.factory.initial","org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            p.put("java.naming.provider.url","tcp://127.0.0.1:61616");

            //Inserisco topic nelle Hashtable
            p.put("topic.math","math");
            p.put("topic.text","text");


            //Creazione e set del contestos
            Context jndiContext= new InitialContext(p);

            //Creiamo la connessione per la Topic
            TopicConnectionFactory tcf = (TopicConnectionFactory) jndiContext.lookup("TopicConnectionFactory");
            TopicConnection tc= tcf.createTopicConnection();

            //Creazione delle Topic
            Topic math=(Topic) jndiContext.lookup("math");
            TopicSession mathsess= tc.createTopicSession(false,Session.AUTO_ACKNOWLEDGE);

            Topic text=(Topic) jndiContext.lookup("text");
            TopicSession textsess=tc.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            //Nel client la Topic deve essere Publisher
            TopicPublisher mp=mathsess.createPublisher(math);
            TopicPublisher tp=textsess.createPublisher(text);

            
            if(args[0].equalsIgnoreCase("math")){

                System.out.println("[CLIENT] Generando richieste math...");
                for(int i=0;i<NREQS;i++){

                    Random r=new Random();

                    //Creazione del messaggio per la topic Math
                    MapMessage  mess=mathsess.createMapMessage();
                    mess.setInt("int1", r.nextInt(101));
                    mess.setInt("int2", r.nextInt(101));
                    mp.send(mess);

                }

            }else if(args[0].equalsIgnoreCase("text")){

                System.out.println("[CLIENT] Generando richieste text...");
                for(int i=0;i<NREQS;i++){

                    Random r=new Random();

                    //Creazione del messaggio per la topic text
                    MapMessage mess=textsess.createMapMessage();
                    mess.setString("string", "save#");
                    mess.setInt("result", r.nextInt(101));
                    tp.send(mess);

                }

            }

            tc.close();

        } catch (JMSException | NamingException e) {
            e.printStackTrace();
        }
        
    }
}