package CodaImpl;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Coda.Coda;
import Coda.CodaWrapped;

public class CodaLock extends CodaWrapped{

    private Lock lock;
    private Condition empty;
    private Condition full;
    
    public CodaLock(Coda c){
        super(c);
        lock=new ReentrantLock();

        //Con i lock non posso creare N condizioni
        empty=lock.newCondition();
        full=lock.newCondition();
    }

    @Override
    public void inserisci(String i) {
        lock.lock();

        try{

            while(coda.full()){
                try{
                    empty.await();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }

            coda.inserisci(i);
            full.signal();

        }finally{
            lock.unlock();
        }

    }

    @Override
    public String preleva() {
        lock.lock();

        String x;

        try{

            while(coda.empty()){
                try{
                    full.await();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }

            x=coda.preleva();
            empty.signal();

        }finally{
            lock.unlock();
        }
        return x;
    }
    
}
