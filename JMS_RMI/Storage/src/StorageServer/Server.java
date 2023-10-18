package StorageServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server{

    public static void main(String[] args){

        try {
            Registry rmiregistry= LocateRegistry.getRegistry();
            IStorage stub= new ServerImpl();
            rmiregistry.rebind("myStorage", stub);

            System.out.println("Storage registrato");

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
        
}
