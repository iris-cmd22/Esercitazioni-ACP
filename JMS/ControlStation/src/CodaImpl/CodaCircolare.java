package CodaImpl;

import Coda.Coda;

public class CodaCircolare implements Coda{

    private String data[];
    private int size;
    private int elem;
    private int tail;
    private int head;

    public CodaCircolare(int s) {
        this.data = new String[s];
        this.size = s;
        this.elem = 0;
        tail= head=0;
    }

    @Override
    public boolean empty() {
        if(elem==0){
            return true;
        }
        return false;
    }

    @Override
    public boolean full() {
        if(elem==size){
            return true;
        }
        return false;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void inserisci(String i) {

        data[head%size]=i;

        try {
            Thread.sleep(101+(int)(Math.random()*100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    
        elem=elem+1;
        System.out.println("[CODA] Inserito "+i+" (tot= "+elem+")");
        tail=tail+1;
    }

    @Override
    public String preleva() {
        String x=data[head%size];

        try {
            Thread.sleep(101+(int)(Math.random()*400));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        elem=elem-1;
        System.out.println("[CODA] prelevato "+x+" (tot= "+elem+")");
        head=head+1;
        return x;
        
    
    }
    
}
