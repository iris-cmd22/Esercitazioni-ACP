package Sensor;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import Coda.Coda;

public class MyListener implements MessageListener{

    Coda coda;

    public MyListener(Coda coda) {
        this.coda=coda;
    }

    @Override
    public void onMessage(Message message) {

        TextMessage comm= (TextMessage) message;

        try {
            String dato=comm.getStringProperty("command");
        

            //Generazione di un Thread per l'inserimento nella coda
            TManager t = new TManager(dato,coda);
            t.start();

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }


    
}
