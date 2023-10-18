from interface import Service
import socket, sys
import multiprocess as mp
import pandas as pd
from scipy import stats

#Process function
def proc_fun(c, service):

    #Ricevi la richiesta
    data = c.recv(1024)

    #Controlliamo il tipo di richiesta e invochiamo il servizio appropriato
    #NOTE: Usiamo in perchè Java aggiunge un carattere extra e questo non permette di realizzare match completo
    if "get_mean" in str(data.decode()):

        result = service.get_mean()

    elif "forecast" in str(data.decode()):

        year = str(data.decode()).split('-')[1]
        result = service.forecast(year)

    #Mandiamo la risposta
    #NOTE: è richiesta l'aggounta di una "\n" alla fien della stringa
    string_to_send = (str(result)+"\n")
    c.send(string_to_send.encode())

    #Adesso possiamo chiudere la connessione
    c.close()

#Skeleton
class ServiceSkeleton(Service):

    def __init__(self,port):
        self.port=port

    def forecast(self, year):
        pass

    def get_mean(self):
        pass

    def run_skeleton(self):

        host='localhost'

        #Creiamo e facciamo la binde della socket
        s= socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.bind((host,self.port))

        s.listen(5)
        print("Socket is listening")

        while True:

            #Stabiliamo una connessione con il client
            c, addr =s.accept()

            #Inizia un nuovo processi per servire la richiesta
            p =mp.Process(target=proc_fun, args=(c,self))
            p.start()

        s.close()

#Service Implementation
class ServiceImpl(ServiceSkeleton):

    def forecast(self,year):

        #self.queue.put(year)

        print("[SERVER-IMPL] calcoliamo la regressione...")

        nyc =pd.read_csv('USH00305801-tmax-1-1-1895-2022.csv', skiprows=4)
        nyc.columns = ['Date', 'Temperature', 'Anomaly']
        nyc.Date = nyc.Date.floordiv(100) #remove 01 from Date
        linear_regression = stats.linregress(x=nyc.Date, y=nyc.Temperature)
        prediction = linear_regression.slope * int(year) + linear_regression.intercept

        print("[SERVER-IMPL] Forecast year: "+year+" prediction: "+str(prediction))

        return prediction
    
    def get_mean(self):

        #result = self.queue.get()
        
        print("[SERVER-IMPL] compute mean...")

        nyc = pd.read_csv('USH00305801-tmax-1-1-1895-2022.csv', skiprows = 4)
        nyc.columns = ['Date', 'Temperature', 'Anomaly']
        nyc.Date = nyc.Date.floordiv(100) #togliamo01 dalla date
        pd.set_option('precison', 2)
        mean = nyc.Temperature.describe().mean()

        print("[SERVER-IMPL] get_mean: ", mean)

        return mean



if __name__ == "__main__":

    try:
        PORT = sys.argv[1]
    except IndexError:
        print("Per favore specifica il PORT arg")
    
    print("Server running ...")

    #Creiamo il Service e runniamo lo Skeleton
    serviceImpl = ServiceImpl(int(PORT))
    serviceImpl.run_skeleton()