package Proxy;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import interfaccia.IService;

public class Proxy {
    
    public static void main(String[] args) {

        try{

            Registry rmiRrgistry = LocateRegistry.getRegistry();
            IService service = (IService) rmiRrgistry.lookup("servizio");

            Receiver receiver= new Receiver(service);
            Thread t=new Thread(receiver);  
            t.start();

            System.out.println("[PROXY] Avviato thread per gestione delle richieste");
        }catch(RemoteException e){
            e.printStackTrace();
        }catch(NotBoundException e){
            e.printStackTrace();
        }
	}
}
