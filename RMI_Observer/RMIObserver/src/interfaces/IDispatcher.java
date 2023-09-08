package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IDispatcher extends Remote{

    void attach(String tipo, IObserver observer) throws RemoteException;
    void setReading(IReading reading) throws RemoteException;
    int getReading() throws RemoteException;
    
}