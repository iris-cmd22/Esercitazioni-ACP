import java.util.Hashtable;
import javax.jms.*;
import javax.naming.*;

public class Dispatcher {

    /*
     * @param args
     */

     public static void main(String[] args){

        /*
         * 
         */
        Hashtable<String, String> properties = new Hashtable<String,String>();
        properties.put("java.naming.factory.initial","org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        properties.put("java.naming.provider.url","tcp://127.0.0.1:61616");
        properties.put("queue.request","request");
        properties.put("queue.response","response");

        try{

            Context jndiContext = new InitialContext(properties);
            QueueConnectionFactory qcf = (QueueConnectionFactory) jndiContext.lookup("QueueConnectionFactory");

            //creo coda delle richieste
            Queue qrequest = (Queue)jndiContext.lookup("request");
            QueueConnection qc = qcf.createQueueConnection();
            qc.start();

            QueueSession qs=qc.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            QueueReceiver receiver= qs.createReceiver(qrequest);

            //creo la coda delle risposte
            Queue qresponse = (Queue)jndiContext.lookup("response");

            //passo al listener la coda delle risposte
            int port = Integer.valueOf(args[0]);

            DispatcherMsgListener listener = new DispatcherMsgListener(qs,qresponse, port);
            receiver.setMessageListener(listener);

            System.out.println("Dispatcher avviato");


        }catch(JMSException e){
            e.printStackTrace();
        }catch(NamingException e){
            e.printStackTrace();
        }
     }
    


    
}
