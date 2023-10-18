package Extractor;

import javax.jms.*;

public class MyListener implements MessageListener{

    TopicPublisher temp;
    TopicSession ts;
    TopicPublisher press;
    TopicSession tp;

    public MyListener(TopicPublisher temp, TopicSession tsess, TopicPublisher press, TopicSession psess){
        this.temp=temp;
        this.ts=tsess;
        this.press=press;
        this.tp=tsess;
    }

    @Override
    public void onMessage(Message message){

        MapMessage m= (MapMessage) message;
        System.out.println("[EXTRACTOR-LISTENER] Messaggio Ricevuto!");

        try{

            //Unmarshalling del MapMessage
            String type=m.getString("type");
            int value=m.getInt("value");

            if(type.equalsIgnoreCase("temperature")){

                TextMessage mess=ts.createTextMessage();
                mess.setText(String.valueOf(value));
                temp.send(mess);
                System.out.println("[EXTRACTOR] Messaggio inviato alla Topic temp!");
                

            }else if(type.equalsIgnoreCase("pressure")){

                TextMessage mess=tp.createTextMessage();
                mess.setText(String.valueOf(value));
                press.send(mess);
                System.out.println("[EXTRACTOR] Messaggio inviato alla Topic press!");

                
            }else{
                System.out.println("[EXTRACTOR] Richiesta non valida!");
            }


        }catch(Exception e){
            e.printStackTrace();
        }

    }
    
}
