package StorageServer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServerImpl extends UnicastRemoteObject implements IStorage{

    private Lock l;

    //Implementiamo un costruttore per l'interfaccia
    public ServerImpl() throws RemoteException{
        super();
        l= new ReentrantLock();
    }

    @Override
    public void store(String reqType, int result) throws RemoteException{
        
        l.lock();

        try {

            //Scrittura su file Java
            FileWriter fw = new FileWriter("results.text", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw= new PrintWriter(bw);
            pw.write(reqType+": "+result+"\n");
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            l.unlock();
        }
        
        
    }


    
}
