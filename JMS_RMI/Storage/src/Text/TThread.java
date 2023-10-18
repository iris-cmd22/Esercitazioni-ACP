package Text;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import StorageServer.IStorage;

public class TThread extends Thread{

    int result;

    public TThread(int result) {
        this.result=result;
    }

    public void run(){

        Registry rmiregistry;
        try {
            rmiregistry = LocateRegistry.getRegistry();
            IStorage stub = (IStorage) rmiregistry.lookup("myStorage");

            stub.store("text", result);

            
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        
    }
    
}
