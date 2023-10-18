import time, stomp, csv
import pandas as pd

count=0

class MyListener(stomp.ConnectionListener):

    def __init__(self, conn):
        self.conn=conn

    #Override del metodo del listener
    def on_message(self,frame):

        #DEBUG: stampo il messaggio che mi arriva
        print('[PRESS ANALYZER] Ricevuto un messaggio: "%s"' % frame.body)

        #Analizziamo il contenuto del messaggio
        global count 
        risp = int(frame.body)
        count=count+1

        #Scrittura su file csv
        with open('press.csv', mode='a', newline='') as pressures:
            writer=csv.writer(pressures)
            writer.writerow([count, risp])


        #Controlliamo ciò che abbiamo scritto sul nostro file csv
        #df=pd.read_csv('press.csv', pressure=['pressures'])
        #df.describe()
        

if __name__=="__main__":

    conn=stomp.Connection([('127.0.0.1',61613)]) #61613 è il port di default utilizzato da ActiveMQ
    conn.set_listener('',MyListener(conn))

    conn.connect(wait=True)
    conn.subscribe(destination='/topic/pressure', id=1, ack='auto')

    time.sleep(60)

    conn.disconnect()