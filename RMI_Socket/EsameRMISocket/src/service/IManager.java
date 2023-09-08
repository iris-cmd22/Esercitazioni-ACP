package service;

import java.rmi.RemoteException;

public interface IManager {
    
    public void sendNotification(AlertNotification alert) throws RemoteException;
    public void subscribe(int componentID, int porta) throws RemoteException;
}
