package service;

import java.io.Serializable;
import java.util.Random;

public class AlertNotification implements Serializable{

    private int componentID;
    private int criticality;

    public AlertNotification(){
        Random r = new Random();
        componentID= (int) (r.nextInt(5));
        criticality= (int) (r.nextInt(3));
    }

    public int getComponentID(){
        return componentID;
    }

    public void setComponentID(int componentID){
        this.componentID=componentID;
    }

    public int getCriticality() {
        return criticality;
    }

    public void setCriticality(int criticality) {
        this.criticality = criticality;
    }

    
}
