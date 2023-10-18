package magazzino;

import java.util.Hashtable;
import javax.jms.*;
import javax.naming.*;

import coda.Coda;
import codaimpl.CodaCircolare;
import codaimpl.CodaWrapperSynchr;

public class Magazzino {
    
    public static void main(String[] args){

        /*
         * 1. Attivazione servizio JNDI
         * 2. Prendere il contesto iniziale
         * 3. Contattare il provider ACTIVEMQ
         */
        Hashtable <String,String> p=new Hashtable<String,String>();

        //settiamo il contesto iniziale
        //nome della proprietà che fa riferiemnto al servizio dei nomi del factory
        p.put("java.naming.factory.initial","org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        p.put("java.naming.provider.url","tcp://127.0.0.1:61616");

        /*In questo modo ci connettiamo al servizio di naming offerto da ActiveMQ */

        //Facciamo quindi il binding tra il nome del contesto JNDI e il contesto reale Active MQ
        //settiamo i nomi delle code
        p.put("queue.Richiesta","Richiesta");
        //per l'altra coda useremo l'header JMSReplyTo

        //Creazione del contesto e gestione delle ecczioni relative a naming,contesto,sessione e connessione
        try{
            Context ctx= new InitialContext(p);

            //prendiamo gli ADMISTRATED OBJECTS
            QueueConnectionFactory qconnf=(QueueConnectionFactory)ctx.lookup("QueueConnectionFactory");

            /*
             * Magazzino attende sulla coda 'Richiesta'
             * MagazzinoThread invia messaggi di risposta sulla coda 'Risposta'
             */

             //definiamo la destinazione
             Queue queueRequest=(Queue) ctx.lookup("Richiesta");

             //Creiamo la connection e avviamola
             QueueConnection qconn = qconnf.createQueueConnection();
             qconn.start();

             //Creiamo la sessione
             QueueSession qsession=qconn.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);
            //Creo il receiver
            QueueReceiver qreceiver = qsession.createReceiver(queueRequest);

            /*Per quanto riguarda la coda di risposta non facciamo il lookup
             * ma utiliziamo l'header ReplyTo che viene settato dal client
             * 
             * Per implementare una comunicazione asincrona possiamo utilizzare 
             * un listener che estenda il message listenere di JMS e faccia l'override di
             * onMessage
             * 
             * Receive asincrona: il costruttore di MagazzinoListener riceve coda e qconn
             */

             //DEFINIAMO LE CODE 
             Coda coda=new CodaWrapperSynchr(new CodaCircolare(10));

             //Creo il listener
             MagazzinoListener l=new MagazzinoListener(coda,qconn);
             qreceiver.setMessageListener(l);


        }catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }


    }
}
