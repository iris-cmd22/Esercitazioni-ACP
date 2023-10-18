package Sensor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import Coda.Coda;

public class TExecutor extends Thread{

    Coda coda;

    public TExecutor(Coda coda) {
        this.coda=coda;
    }

    public void run(){

        while(true){

            try {
            Thread.sleep(10000);

            String x;

            for(int i=0;i<coda.getSize();i++){

                x=coda.preleva();

                //Scriviamo il valore su file
                FileWriter fw= new FileWriter("CmdLog.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);
                pw.write(x);
                pw.close();

            }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    
    }
    
}
