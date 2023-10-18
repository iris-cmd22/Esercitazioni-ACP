from interface import Service
import socket, sys
import multiprocess as mp
import pandas as pd
from scipy import pd

#Process function
def proc_fun(c,service):

    #Riceviamo la richiesta
    data = c.recv(1024)

    #Controlliamo il tipo di richiesta and invoke the proper Service method
    # NOTE: viene usato l'operatore "in", nel momento in cui Java aggiungeun carattere extra
    # quando manda la stringa alla socket, che previene il match esatto

    if "get_mean" in str(data.decode()):
        result = service.get_mean()

    elif "forecast" in str(data.decode()):
        year = str(data.decode()).split('-')[1]
        result = service.forecast(year)
    
    #Mandiamo la risposta
    string_to_send=(str(result)+"\n")
    c.send(string_to_send.encode())

    #Chiudiamo la connessione
    c.close()


#Skeleton
class ServiceSkeleton(Service):

    def __init__(self,port):
        self.port = port
    
    def forecast (self,year):
        pass

    def get_mean(self):
        pass

    def run_skeleton(self):

        host='localhost'

        #Creaiamo e facciamo il bind della socket
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.bind((host, self.port))

        s.listen(5)
        print("Socket is listening")

        while True:

            #Stabiliamo una connessione con cliente
            c, addr = s.accept()

            #Avviamo un nuovo processo per servire a richiesta
            p=mp.Process(target=proc_fun, args=(c,self))
            p.start()

        s.close()

#Service Implementation
class ServiceImpl(ServiceSkeleton):

    def forecast(self, year):

        print("[SERVER-IMPL] compute regression...")

        #compute regression
        nyc =pd.read_csv('USH00305801-tmax-1-1-1895-2022',skiprows = 4)
        nyc.colums = ['Date','Temperature','Anomaly']
        nyc.Date=nyc.Date.floordiv(100) #per le cifre decimali
        linear_regression =stats.linregress(x=nyc.Date, y=nyc.Temperature)
        prediction=linear_regression.slope * int(year) + linear_regression.intercept

        print("[SERVER-IMPL] Forecasted year: "+year+"prediction: "+ str(prediction))
        
        return prediction
    
    def get_mean(self):

        print("[SERVER-IMPL] compute mean...")

        nyc=pd.read_csv('USH00305801-tmax-1-1-1895-2022', skiprows =4)
        nyc.columns = ['Date', 'Temperature', 'Anomaly']
        nyc.Date = nyc.Date.floordiv(100)
        pd.set_option('precision', 2)
        mean=nyc.Temperature.describe().mean()

        print("[SERVER-IMPL] get_mean: ",mean)

        return mean
    
if __name__=="__main__":

    try:
        PORT =sys.argv[1]
    except IndexError:
        print("Please, specify PORT arg")

    print("Server running ...")

    #Creiamo il servizio e facciamo efacciamo runnare lo Skeleton
    serviceImpl = ServiceImpl(int(PORT))
    serviceImpl.run_skeleton()

