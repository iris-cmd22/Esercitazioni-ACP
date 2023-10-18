import random
import proxy

if __name__=="__main__":

    Proxy = proxy.Proxy()
    #Il Client genera 10 entry di log, invocando il metodo log per ogni entry
    for _ in range(0,10):
        #Per ciascuna entry, tipo è generato in maniera casuale 
        # scegliendo un interno tra 0 e 3 (estremi inclusi)
        tipo=random.randint(0,3)

        #messaggioLog è generato in maniera casuale scegliendo 
        # tra success o checking se il tipo è 0 o 1, 
        if(tipo==0 or tipo==1):
            rand=random.randint(0,50)
            
            if(rand%2==0):
                messaggioLog="success"
                Proxy.log(messaggio=messaggioLog, tipo=tipo)

            else:
                messaggioLog="checking"
                Proxy.log(messaggio=messaggioLog,tipo=tipo)
        
        # tra fatal o exception se il tipo è pari a 2
        elif(tipo==2):
            rand=random.randint(0,50)

            if(rand%2==0):
                messaggioLog="fatal"
                Proxy.log(messaggio=messaggioLog, tipo=tipo)

            else:
                messaggioLog="exception"
                Proxy.log(messaggio=messaggioLog,tipo=tipo)

            

