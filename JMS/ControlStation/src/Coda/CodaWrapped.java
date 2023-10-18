package Coda;

public abstract class CodaWrapped implements Coda{

    protected Coda coda;

    public CodaWrapped(Coda c){
        coda=c;
    }

    public boolean empty(){
        return coda.empty();
    }

    public boolean full(){
        return coda.full();
    }

    public int getSize(){
        return coda.getSize();
    }

    //Non inplementiamo preleva e inserisci a fine di forzarne 
    //l'implementazioen da parte dei concrete decorator
    
}
