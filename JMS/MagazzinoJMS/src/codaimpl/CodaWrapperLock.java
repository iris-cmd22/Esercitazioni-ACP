package codaimpl;

import java.util.concurrent.locks.*;
import coda.*;

public class CodaWrapperLock extends CodaWrapped{

    private Lock lock;
    private Condition empty;
    private Condition full;

    //Costruttore della classe
    public CodaWrapperLock(Coda c){

        super(c);
        lock=new ReentrantLock();

        //a differenza di synchonized, con i lock
        //e' possibile creare N condizioni
        empty = lock.newCondition();
        full = lock.newCondition();
    }

    public void inserisci(int i){
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

    public int preleva(){

        int x=0;
        lock.lock();

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
        