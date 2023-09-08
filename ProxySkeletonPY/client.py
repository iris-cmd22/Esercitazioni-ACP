import stomp,random,time

#Listener
class MyListener(stomp.ConnectionListener):

    def on_message(self,frame):

        #Stampiamo la risposta
        print('[CLIENT] Received response: "%s"' %frame.body)

if __name__=="__main__":

    #Creiamo una connessione
    conn= stomp.Connection([('127.0.0.1',61613)])

    #Set del listener
    conn.set_listener('',MyListener())

    #Connettiamo e facciamo subscribe della coda di risposta
    conn.connect(wait=True)
    conn.subscribe(destination='/queue/response',id=1,ack='auto')

    #Facciamo la richiesta
    for i in range(10):

        if((i%2) == 0):

            request="deposita"
            id=random.randint(1,100)
            MSG = request + "-" + str(id)
        else:
            MSG = "preleva"

        # Mandiamo la richiesta sulla cosa di richiesta
        conn.send('/queue/request',MSG)
        print("[CLIENT] Request: ",MSG)

    while True:
        time.sleep(60)
    
    conn.disconnect()