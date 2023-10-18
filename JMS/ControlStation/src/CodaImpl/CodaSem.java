package CodaImpl;

import java.util.concurrent.Semaphore;

import Coda.Coda;
import Coda.CodaWrapped;

public class CodaSem extends CodaWrapped{
    
    private Semaphore postDisp;
    private Semaphore elemDisp;

    public CodaSem(Coda c) {
        super(c);

        postDisp = new Semaphore(coda.getSize());
        elemDisp = new Semaphore(0);
    }


    @Override
    public void inserisci(String i) {

        try {
            postDisp.acquire();

            try{
                synchronized(coda){
                coda.inserisci(i);
                }
            }finally{
                elemDisp.release();
            }
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String preleva() {

        String x="";
        try {
            elemDisp.acquire();

            try{
                synchronized(coda){
                    x= coda.preleva();
                }
            }finally{
                postDisp.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return x;

    }

    
}
