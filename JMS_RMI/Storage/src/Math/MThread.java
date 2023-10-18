package Math;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import StorageServer.IStorage;

public class MThread extends Thread{

    int prod;

    public MThread(int prodotto) {
        this.prod=prodotto;
    }

    //Essendo un Thread riscrivo il metodo run
    public void run(){

        //i. Stampa a video del risultato
        System.out.println("[MATH Execution] Il prodotto dei due interi e'"+prod);

        //ii. Effettuiamo una chiamata a JavaRMI
        Registry rmiregistry;
        try {
            rmiregistry = LocateRegistry.getRegistry();
            IStorage s = (IStorage) rmiregistry.lookup("myStorage");
            s.store("math", prod);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        
        
    }
    
}
