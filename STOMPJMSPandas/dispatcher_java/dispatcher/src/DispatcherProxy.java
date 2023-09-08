import java.io.*;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;

import java.net.*;

public class DispatcherProxy implements IDispatcher{

    private String addr;
    private int port;
    private QueueSession qsession;
    private Queue qresponse;

    public DispatcherProxy(String a, int p, QueueSession qsession, Queue qresponse){

        this.addr = new String(a);
        this.port=p;
        this.qsession= qsession;
        this.qresponse= qresponse;
    }

    public void forecast(int year){
        try{

            Socket s = new Socket (addr,port);
            DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());

            //Usiamo un BufferReader è usato per ricevere dati da un'applicazione Python
            //nel momento in cui permette di usare il metodo readLine
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
        }catch (IOException e ){
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}
    }

    public float get_mean(){
        String result = null;

        try{

            Socket s =new Socket(addr,port);
            DataOutputStream dataOut=new DataOutputStream(s.getOutputStream());

            BufferedReader dataIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
            dataOut.writeUTF("get_mean");
            result = new String(dataIn.readLine());
            s.close();

            //rispondo tramite JMS al client Python
            TextMessage message=qsession.createTextMessage("get_mean-"+result);
            QueueSender sender=qsession.createSender(qresponse);
            sender.send(message);

        }catch(UnknownHostException u ){
            u.printStackTrace();
        }catch (IOException e ){
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}

        return Float.valueOf(result);
    }
}