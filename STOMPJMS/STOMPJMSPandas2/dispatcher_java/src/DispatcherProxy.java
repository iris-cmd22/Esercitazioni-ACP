import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;

public class DispatcherProxy implements IDispatcher{

    private String addr;
    private int port;
    private QueueSession qsession;
    private Queue qresponse;

    public DispatcherProxy(String a, int p, QueueSession qsession, Queue qresponse){
        this.addr = new String(a);
        this.port = p;
        this.qsession=qsession;
        this.qresponse=qresponse;

    }

    public void forecast(int year){
        try{
            Socket s=new Socket(addr, port);
            DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());

            //Per ricevere dati dall'applicazione Pyhton utilizzerenmo una BufferedReader
            BufferedReader dataIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
            dataOut.writeUTF("forecast-"+year);

            String result = dataIn.readLine();

            s.close();

            //rispondo tramite JMS al client Python
            TextMessage message = qsession.createTextMessage("forecast-"+result);
            QueueSender sender = qsession.createSender(qresponse);
            sender.send(message);

        }catch(UnknownHostException u){
            u.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }catch(JMSException e){
            e.printStackTrace();
        }
    }

    public float get_mean(){

        String result=null;

        try{
            
            Socket s=new Socket(addr,port);

            DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());

            //NOTE: Anche qui usiamo un BUfferedReader
            BufferedReader dataIn = new BufferedReader( new InputStreamReader(s.getInputStream()));

            dataOut.writeUTF("get_mean");
            result = new String(dataIn.readLine());

            s.close();

            TextMessage message = qsession.createTextMessage("get_mean-"+result);
            QueueSender sender = qsession.createSender(qresponse);
            sender.send(message);
        }catch(UnknownHostException u){
            u.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }catch(JMSException e){
            e.printStackTrace();
        }

        return Float.valueOf(result);
    }
    
}
