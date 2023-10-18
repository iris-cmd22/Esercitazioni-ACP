import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.jms.JMSException;


public class DispatcherMsgListener  implements MessageListener{
    
    private QueueSession qsession;
    private Queue qresponse;
    private int port;

    public DispatcherMsgListener(QueueSession qsession,Queue qresponse, int port ){
 
        this.qsession = qsession;
        this.qresponse = qresponse;
        this.port= port;
    }

    @Override
    public void onMessage(Message arg0){

        TextMessage msg = (TextMessage)arg0;
        try{
            String message =msg.getText();
            IDispatcher dispatcher = new DispatcherProxy("localhost", port, qsession, qresponse);

            if(message.equalsIgnoreCase("get_mean")){

                //invochiamo il DispatcherProxy e i suoi servizi
                System.out.println("[DispatcherMsgListener] Ricevuta richiesta di get_mean");

                dispatcher.get_mean();
            }else{
                //Deposito
                String[] splitted = message.split("-");
                Integer year_to_predict = Integer.valueOf(splitted[1]);
                System.out.println("[DispatcherMsgListener] Ricevuta richiesta di forecast per l'anno "+ year_to_predict);

                //Invoca il DispatcherProxy per il servizio di deposito
                dispatcher.forecast(year_to_predict);

            }
        }catch(JMSException e){
            e.printStackTrace();
        }
    }
}
