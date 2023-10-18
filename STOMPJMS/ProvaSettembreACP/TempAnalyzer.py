import time, stomp
from multiprocessing import Queue
from matplotlib import pyplot as plt
import numpy as np

count = 0
#Creazione della queue
q=Queue()

class MyListener(stomp.ConnectionListener):

    def __init__(self, conn):
        self.conn=conn

    def on_message(self,frame):

        #DEBUG: stampo il messaggio che mi arriva
        print('[TEMP ANALYZER] Ricevuto un messaggio: "%s"' % frame.body)

        #Analizziamo il contenuto del messaggio e castiamo a intero
        global count
        global q
        risp = int(frame.body)
        q.put([risp])
        count=count+1
        print('count:'+str(count))



if __name__=="__main__":

    conn=stomp.Connection([('127.0.0.1',61613)]) #61613 è il port di default utilizzato da ActiveMQ
    conn.set_listener('',MyListener(conn))

    conn.connect(wait=True)
    conn.subscribe(destination='/topic/temperature', id=1, ack='auto')


    while True:
        #Ogni 20 genero il grafico
        if(count>=19):
                
                data=[]
                while not q.empty():
                     data.append(q.get()[0])
                
                #Generazione lineplots
                plt.figure()

                #Popolamento plot con la queue
                plt.plot(range(len(data)),data)

                plt.title('Temperatures Line Plot') #Add a Title
                plt.show()

                #pulisco queue
                while not q.empty():
                    q.get()

                #Azzero il counter
                count=0     

    time.sleep(160)

    conn.disconnect()