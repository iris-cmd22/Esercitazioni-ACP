package Client;

import java.util.Hashtable;
import java.util.Random;

import javax.naming.*;
import javax.jms.*;

public class Client{
    public static void main(String[] args){

        int N_RICHIESTE=20;

        try{

            if(args.length ==0){
                System.out.println("Inserisci comando!");
                return;
            }

            Hashtable<String,String> p = new Hashtable<String,String>();
            p.put("java.naming.factory.initial","org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            p.put("java.naming.provider.url","tcp://127.0.0.1:61616");
            
            p.put("topic.data","data");

            //Definizione del contesto
            Context jndiContext = new InitialContext(p);
            
            //Creazione della connessione con AbstractFactory
            TopicConnectionFactory tcf = (TopicConnectionFactory) jndiContext.lookup("TopicConnectionFactory");
            TopicConnection tc = tcf.createTopicConnection();
            tc.start();

            //Definizione della Topic e della relativa Session
            Topic data = (Topic) jndiContext.lookup("data");
            TopicSession ts=tc.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

            //Creazione del publisher
            TopicPublisher tp = ts.createPublisher(data);

            //Adesso che abbiamo creato la nostra Topic creiamo il MapMessage
            MapMessage mess=ts.createMapMessage();

            //Generiamo 20 richieste
            for(int i=0;i<N_RICHIESTE;i++){

                Random r=new Random();

                if(args[0].equalsIgnoreCase("temperature")){

                    mess.setString("type", "temperature");
                    mess.setInt("value", r.nextInt(101));
                    tp.send(mess);

                    Thread.sleep(2000);

                }else if(args[0].equalsIgnoreCase("pressure")){

                    mess.setString("type", "pressure");
                    mess.setInt("value", r.nextInt(51)+1000);
                    tp.send(mess);

                    Thread.sleep(2000);

                }else{
                    System.out.println("Richiesta non valida!");
                    return;
                }
            }

            //Chiudiamo la connessione
            tc.close();
            


        }catch(NamingException e){
            e.printStackTrace();
        }catch(JMSException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    
    }
}