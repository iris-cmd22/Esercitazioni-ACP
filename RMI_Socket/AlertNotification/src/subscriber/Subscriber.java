package subscriber;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import service.IManager;
import service.ISubscriber;

public class Subscriber {

    public static void main(String[] args){
        if(args.length!=3){
            System.out.println("Inserire i parametri");
            return;
        }

        try{

            ISubscriber sub = new SubscriberImpl(args[2]);
            SubscriberSkeleton skeleton = new SubscriberSkeleton(sub,Integer.parseInt(args[1]));

            //Comunicazione rmiRegistry
            Registry rmiRegistry = LocateRegistry.getRegistry();
            IManager stub = (IManager) rmiRegistry.lookup("myManager");
            System.out.println("[SUBSCRIBE] Mi iscrivo al manager...");
            stub.subscribe(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
            skeleton.runSkeleton();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
