package Math;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public class MyListener implements MessageListener{

    @Override
    public void onMessage(Message message) {
        
        MapMessage m=(MapMessage) message;
        int prodotto=0;

        try {
            int num1=m.getInt("int1");
            int num2=m.getInt("int2");

            System.out.println("\n[MATH Listener] Moltiplicando "+num1+ " e "+num2+" ...");

            prodotto=num1*num2;

        } catch (JMSException e) {
            e.printStackTrace();
        }

        //Chiamo un thread che possa occuparsi di questa richiesta

        MThread mt=new MThread(prodotto);
        mt.start();
        
    }

    
    
}
