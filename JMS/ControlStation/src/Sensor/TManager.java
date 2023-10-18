package Sensor;

import Coda.Coda;

public class TManager extends Thread{

    String dato;
    Coda coda;

    public TManager(String dato, Coda coda) {
        this.dato=dato;
        this.coda=coda;
    }

    public void run(){

            coda.inserisci(dato);
    }
    
}
