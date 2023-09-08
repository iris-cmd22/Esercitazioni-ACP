package dispatcher;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import interfaces.IDispatcher;

public class Dispatcher {
    
    public static void main(String[] args){
        try{
            Registry rmiRegistry = LocateRegistry.getRegistry();
            IDispatcher d = new DispatcherImpl();
            rmiRegistry.rebind("dispatcher0",d);
            System.out.println("[DISPATCHER] Rebind effettuato!");
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }
}
