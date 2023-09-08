package interfaces;

import java.io.Serializable;

public interface IReading extends Serializable{
    
    void setTipo(String s);
    String getTipo();
    void setValore(int valore);
    int getValore();
}
