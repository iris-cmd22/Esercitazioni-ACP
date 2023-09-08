import stomp, random, time
import pandas as pd
from scipy import stats
from matplotlib import pyplot as plt
import numpy as np

predictions = []

#Listener
class MyListener(stomp.ConnectionListener):

    def __init__(self):
        self.predictons=[]

    def on_message(self, frame):

        #Stampo la risposta ottenuta
        print('[CLIENT] Received response: "%s' % frame.body)
        if "forecast" in frame.body:
            predictions = (frame.body).split('-')[1]
            self.predictons.append(prediction)
    
    def get_predictions(self):
        return self.predictons
    
if __name__ == "__main__":

    #Creiamo una connessione e settiamo auto_content_lenght a false per interagire 
    # con una applicazione JMS, usando i messaggi
    conn=stomp.Connection([('127.0.0.1',61613)], auto_content_length=False)

    #Set the listener
    listener=MyListener()
    conn.set_listener('',listener)

    #Connetti e sottoscrivi alla coda di risposte
    conn.connect(wait=True)
    conn.subscribe(destination='/queue/response',id=1,ack='auto')

    years=[]
    #Facciamo la richiesta
    forecast_reqs=30
    for i in range(forecast_reqs):

        request="forecast"

        #generiamo anni random fra il 2021 e il 2121
        year=random.randint(2021,2121)
        years.append(year)
        MSG=request+"-"+str(year)


        #mandiamo la richiesta sulla coda delle richieste
        conn.send('/queue/request',MSG)

        print("[CLIENT] Request: ", MSG)

    while True:
        time.sleep(10)
        print(listener.get_predictions())

        #controlliamo se tutte le forecast_reqs sono complete
        if(len(listener.get_predictions())== forecast_reqs):

            #plot
            print("PLT predictions")

            nyc=pd.read_csv('USH00305801-tmax-1-1-1895-2022.csv',skiprows=4)
            nyc.columns=['Date','Temperature','Anomaly']
            nyc.Date=nyc.Date.floordiv(100)

            fig, ax=plt.subplots()
            new_dates=pd.Series(years)
            new_temps=pd.Series(listener.get_predictions(), dtype='float')

            #creiamo series di data e temperatura estendendo le series,
            #new_dates e new_temps Series con una NaN Series  con la 
            #lunghezza della Series originale

            nan_series=pd.Series(np.nan, index=range(0,len(nyc.Date)))

            ## I need new objects, concat does not work in-place
            newDates = pd.concat([nan_series, new_dates])
            newTemps = pd.concat([nan_series, new_temps])

            ## plot 2 scatter plots with the prediction one with alpha value
            ax.scatter(nyc.Date, nyc.Temperature); # plot temp vs date
            ax.scatter(newDates, newTemps, alpha=0.25)

            print(nyc.Date.dtype, nyc.Temperature.dtype)
            print(newDates.dtype, newTemps.dtype)

            ax.legend(['Original', 'Predicted'])
            
            ax.set_xlabel('Years')          
            ax.set_ylabel('Temperature [F°]')         

            #plt.show()

            ## mi conviene salvare il file in locale se volessi
            ## presentarlo in una webapp
            plt.savefig('prediction.png')
        
            break

        conn.disconnect()