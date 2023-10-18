package magazzino;


import javax.jms.Queue;

import javax.jms.MapMessage;
import javax.jms.QueueConnection;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;

import javax.jms.JMSException;

import coda.Coda;

public class MagazzinoThread extends Thread{
    
    private Coda coda;
    private MapMessage mm;
    private QueueConnection qconn;

    public MagazzinoThread(Coda c, MapMessage m, QueueConnection qc){

        this.coda=c;
        this.mm=m;
        this.qconn=qc;
    }

    //Ridefiniamo il metodo run
    public void run(){
        //dobbiamo capire il tipo di operazione e il valore eventuale

        //Parsing del messaggio
        try{
            String op=mm.getString("operazione");

            if(op.compareTo("deposita")==0){
                int val=mm.getInt("valore");
                System.out.println("[MAGAZZINO-THREAD] operazione: "+op+" val");

                coda.inserisci(val);
            }else{
                int val=coda.preleva();

                //Adesso ci dobbiamo preoccupare di restituire una risposta
                //Per farlo devo creare uan sessione usando la connessione
                QueueSession qsession = qconn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                //di conseguenza creo il sender
                QueueSender qsender=qsession.createSender((Queue) mm.getJMSReplyTo());
                
                /*
                 * In questo caso sto sfruttando il campo che conterrà l'oggetto remoto
                 * 
                 */

                 MapMessage reply=qsession.createMapMessage();
                 reply.setString("operazione", "risultato"); //per riempimento
                 reply.setInt("valore", val);

                 //quindi invio
                 qsender.send(reply);

                 qsender.close();
                 qsession.close();
                 //ma non chiudo la connessione

            }
        }catch(JMSException e){
            e.printStackTrace();
        }
    }
}
