package node;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import interface.IDispatcher;

public class Node {

    public static void main(String[] args){

        if(args.length!=2){
            System.out.println("Inserisci i parametri");
            return;
        }else if (Integer.parseInt(args[0])<0 || Integer.parseInt(args[0])>100){
            System.out.println("Dato out of bounds");
            return;
        }

        int NTHREADS=5;
        Thread[] threads = new Thread[NTHREADS];

        for(int i=0;i<NTHREADS;i++){
            NodeThread t =new NodeThread();
            t.start();
            threads[i]=t;
        }

        for(int i=0;i<NTHREADS;i++){
            try{
                threads[i].join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }



    }
    
}
