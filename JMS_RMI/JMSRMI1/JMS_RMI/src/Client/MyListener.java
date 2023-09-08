package Client;

import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.MessageListener;
import javax.jms.JMSException;

public class MyListener implements MessageListener {
	
	@Override
	public void onMessage(Message arg0) {
		
		TextMessage msg = (TextMessage)arg0;

		try{

                System.out.println("[LISTENER] Ricevuto messaggio: "+ msg.getText());
		}catch(JMSException e){
			e.printStackTrace();
		}
	}

}
