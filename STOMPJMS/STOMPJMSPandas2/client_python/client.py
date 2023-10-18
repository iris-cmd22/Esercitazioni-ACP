import stomp, random, time
import pandas as pd
from scipy import stats
from matplotlib import pyplot as plt
import numpy as np

predictions = []

#Listener
class MyListener(stomp.ConnectionListener):

    def __init__(self):
        self.predictions = []

    def on_message(self, frame):

        #Stampiamo la risposta ottenuta
        print('[CLIENT] Received response: "%s"' % frame.body)
        if "forecast" in frame.body:
            prediction = (frame.body).split('-')[1]
            self.predictions.append(prediction)

    def get_predictions(self):
        return self.predictions
    

if __name__ == "__main__":

    #Creiamo la connessione e settiamo auto_content_lenght a False, così da interagire con l'applicazione JMS (usando i TextMessages)
    conn = stomp.Connection([('127.0.0.1', 61613)], auto_content_length=False)#61613 porto di default di ActiveMQ


    #Settiamo il listener
    listener = MyListener()
    conn.set_listener('',listener)

    #Connettiamoci e iscriviamocu alla queue 'response'
    conn.connect(wait=True)
    conn.subscribe(destination='/queue/response', id=1, ack='auto')

    years = []
    #Facciamo la richiesta
    forecast_reqs =30
    for i in range(forecast_reqs):

        if((i%2)==0):
            request = "forecast"

            #generiamo anni randomici fra il 2021 e il 2121
            year = random.randint(2021,2121)
            years.append(year)
            MSG = request + "-" + str(year)

        else:
            MSG = "get_mean"

        #Mandiamo la richiesta alla queue richiesta 'request'
        conn.send('/queue/request', MSG)

        print("[CLIENT] Request: ",MSG)

    while True:
        time.sleep(10)
        print(listener.get_predictions() == forecast_reqs)
        

        #Verifichiamo se le forecast_reqs siano state completate
        if(len(listener.get_predictions())==forecast_reqs):

            #plot
            print("PLT predictions")

            nyc = pd.read_csv('USH00305801-tmax-1-1-1895-2022.csv', skiprows=4)
            nyc.columns = ['Date', 'Teamperature', 'Anomaly']
            nyc.Date = nyc.Date.floordiv(100) #rimuoviamo 01 dalla data

            fig, ax=plt.subplots()
            new_dates = pd.Series(years)
            #facciamo converisione a float
            new_temps = pd.Series(listener.get_predictions(), dtype='float')

            #creaiamo una series con data e temperatura estendendo la serie,
            nan_series = pd.Series(np.nan, index=range(0,len(nyc.Date)))

            #Ho bisogno di nuovi oggetti a cui concatenare questi dati
            newDates = pd.concat([nan_series, new_dates])
            newTemps = pd.concat([nan_series, new_temps])

            #plottiamo 2 scatter plot
            ax.scatter(nyc.Date, nyc.Temperature)
            ax.scatter(newDates, newTemps, alpha=0.25)

            print(nyc.Date.dtype, nyc.Temperature.dtype)
            print(newDates.dtype, newTemps.dtype)

            ax.legend(['Original', 'Predicted'])

            ax.set_xlabel('Years')
            ax.set_ylabel('Temperature [F°]')

            #Salviamo quindi il file in modo da poterlo eventualmente utilizzarlo in una webapp
            plt.savefig('prediction.png')

            break

    conn.disconnect()