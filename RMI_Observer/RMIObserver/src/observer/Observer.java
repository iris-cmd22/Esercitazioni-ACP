package observer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import interfaces.IDispatcher;
import interfaces.IObserver;

public class Observer {

    public static void main(String[] args){
        if(args.length!= 2 ){
            System.out.println("Inserisci i parametri");
            return;
        }

        try{
            Registry rmRegistry = LocateRegistry.getRegistry();
            IDispatcher stub = (IDispatcher) rmRegistry.lookup("dispatcher0");
            IObserver o = new ObserverImpl(args[1]);
            stub.attach(args[0],o);
            System.out.println("[OBSERVER] Attach effettuato!");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
