package magazzino;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.QueueConnection;
import coda.Coda;

public class MagazzinoListener implements MessageListener{

    //Per scehdulare in nuovo thread avrò bisogno di operare sulla coda e di avere
    //un oggetto connection che mi permetta di creare la sessione
    //Se creassi la sessioen fuori dal thread avrei problemo di concorrenza
    private Coda coda;
    private QueueConnection qconn;

    public MagazzinoListener(Coda coda,QueueConnection qconn){
        this.coda=coda;
        this.qconn=qconn;
    }

    @Override
    public void onMessage(Message message){
        //prendo il messaggio e lo asegno al thread di riferimento
        MapMessage mm= (MapMessage) message;

        //Creazione del Thread e avvio
        MagazzinoThread mt= new MagazzinoThread(coda,mm,qconn);
        mt.start();

        //il thread riceverà e invierà la risposta

    }
}