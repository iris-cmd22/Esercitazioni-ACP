package generator;

import java.util.Random;

import interfaces.IReading;

public class Reading implements IReading{
    private String tipo;
    private int valore;
    private final static long serialVersionUID=10;

    //Occupiamoci del costruttore
    public Reading(){
        Random r=new Random();
        valore =r.nextInt(50);
        if(r.nextInt(100)%2==0){
            tipo="temperatura";
        }else{
            tipo="pressione";
        }
    }

    //Getter and Setters
    @Override
    public int getValore() {
        return valore;
    }
    @Override
    public void setValore(int valore) {
        this.valore = valore;
    }
    @Override
    public String getTipo() {
        return tipo;
    }
    @Override
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    
    
    
}
