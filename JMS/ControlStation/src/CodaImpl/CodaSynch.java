package CodaImpl;

import Coda.Coda;
import Coda.CodaWrapped;

public class CodaSynch extends CodaWrapped{

    public CodaSynch(Coda c){
        super(c);
    }

    public void inserisci(String i){

        synchronized(coda){
            //Finchè la coda è piena
            while(coda.full()){
                try {
                    coda.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            coda.inserisci(i);
            coda.notifyAll();
        }
    }

    public String preleva(){
        String x;

        synchronized(coda){
            //Finchè la coda non è vuota
            while(coda.empty()){
                try{
                    coda.wait();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
            x=coda.preleva();
            coda.notifyAll();
        }

        return x;
    }
    
}
