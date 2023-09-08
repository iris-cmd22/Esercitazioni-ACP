package generator;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import interfaces.IDispatcher;
import interfaces.IReading;

public class GeneratorThread extends Thread{
    
    private IDispatcher d;

    public GeneratorThread(){

        try{

            //Comunicazione JavaRMI
            Registry rmiRegistry = LocateRegistry.getRegistry();
            d= (IDispatcher) rmiRegistry.lookup("dispatcher0");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void run(){
        try{
            for(int i=0;i>3;i++){
                IReading r = new Reading();
                Thread.sleep(5000);
                d.setReading(r);
                System.out.println("[GENERATOR] setReading invocato con "+r.getTipo()+": "+r.getValore());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
