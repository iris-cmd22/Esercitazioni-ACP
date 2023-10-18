import sys,stomp,time
#ERROR CHECKER
#Implementa la ricezione sulla STOMP Queue error e prevede come parametro 
#di avvio (da terminale) una stringa tra fatal o exception.

#Alla ricezione di ciascun messaggio
class MyListener(stomp.ConnectionListener):

    def __init__(self,stringa):
        self.stringa=stringa

    def on_message(self, frame):
        #il listener STOMP di Error Checker estrae il contenuto del messaggio
        message = (frame.body).split("-")[0]
        #stampa a video il messaggio appena ricevuto
        print("Messaggio ricevuto: "+message) 

        #verifica se esso contiene la stringa ricevuta in input
        if message == self.stringa:

            # in caso affermativo, scrive su file (error.txt)
            print("Scrivo su error.txt")
            with open("error.txt", mode='a') as file:
                file.write("\n"+message)
            file.close()


if __name__=="__main__":

    #prevede come parametro di avvio (da terminale) una stringa tra fatal o exception
    try:
        STRING=sys.argv[1] #prendo la prima stringa presa da terminale

    except IndexError:
        print("Scrivi in input 'fatal' o 'error'")

    conn=stomp.Connection({("127.0.0.1",61613)}) #STOMP connection

    #Creazione del listener in ascolto sulla queue Error
    listener=MyListener(STRING) 
    conn.set_listener("",listener)
    conn.connect()
    conn.subscribe("/queue/error",id=2,ack="auto") #ricezione sulla STOMP Queue error

    while True:
        time.sleep(10)