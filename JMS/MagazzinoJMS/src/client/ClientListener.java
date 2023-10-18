package client;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public class ClientListener implements MessageListener{
    
    public void onMessage(Message message){
        MapMessage msg= (MapMessage) message;

        try{
            System.out.println("[CLIENT-Listener] messaggio ricevuto - valore: "+msg.getInt("valore"));
        }catch(JMSException e){
            e.printStackTrace();
        }
    }
}
