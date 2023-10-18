package Text;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public class MyListener implements MessageListener{

    @Override
    public void onMessage(Message message) {
        
        MapMessage mess= (MapMessage) message;

        try {
            String text = mess.getString("string");
            int result = mess.getInt("result");

            System.out.println("[TEXT Listener] Elaborando "+text+result+" ...");

             TThread t = new TThread(result);
             t.start();


        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
    
}
