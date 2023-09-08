package coda;

public abstract class CodaWrapped implements Coda{
    
    protected Coda coda;

    public CodaWrapped(Coda c){
        coda=c;
    }

    /*Implementiamo i metodi empty/full e getsize 
     * 
     * NOTA: *non* sono implementati insrisci e preleva -astratti-,
     * al fine di 'forzarne' l'implementazione da parte dei concrete decorator
     * (per es. CodaWrapperSynchr);
     * a tal fine, CodaWrapper è una classe astratta
    */

    public boolean empty(){
        return coda.empty();
    }

    public boolean full(){
        return coda.full();
    }

    public int getSize(){
        return coda.getSize();
    }


}
