from multiprocessing import Process, Queue
import socket
import time
import stomp
import ILogging #import dell'interfaccia formale

#Definizione del produttoe
def Prod(self,val,queue:Queue):
    print("[Produttore-Server] Inserisco il dato nella coda")
    queue.put(val)

    print("[Produttore-Server] Inserimento avvenuto!")

#Definizione del consumatore
def Cons(queue:Queue):
    #Quando un nuovo dato è disponibile nella coda, il processo consumatore, 
    # preleva la stringa e la inserisce in un messaggio STOMP
    conn = stomp.Connection([("127.0.0.1",61613)])
    conn.connect()

    while True:
        time.sleep(10) #Ogni 10 sec invio il messaggio sulla QUEUE STOMP
        mess=queue.get()
        print("Invio il messaggio: ", mess)

        #Il messaggio viene scritto nella STOMP Queue error se il messaggio 
        #contiene il tipo pari a 2, nella STOMP Queue info negli altri casi
        if mess.split("-")[1]=="2":
            conn.send("/queue/error",mess)
        else:
            conn.send("/queue/info", mess)

class LoggingSkeleton(ILogging.ILogging):

    def __init__(self,queue:Queue) ->None:
        super().__init__()

        self.queue=queue
        #creazione di una socket(IPv4  TCP)
        self.sock=socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock.bind(("127.0.0.1",3000)) #bind al localhost, port 

        print("[LoggingServer] In attesa di richieste...")
        self.sock.listen(1)

    def runSkeleton(self):
        conn, addr = self.sock.accept()
        print("[LoggingServer] Connessione accettata")

        while True:
            #Recezione del messaggio
            mess = conn.recv(1024)
            print("Messaggio arrivato: "+mess.decode())

            #il messaggio contiene un campo tipo messagio string e un campo tipo numerico
            messaggioLog = (mess.decode()).split("-")[0]
            tipo=(mess.decode()).split("-")[1]
            print(messaggioLog + tipo)

            self.log(messaggioLog, tipo)

    

class LoggingImpl(LoggingSkeleton):

    #il metodo log avvia un processo produttore che inserisce in una coda
    #una stringa che concatena la stringa messaggioLog + il tipo int 
    def log(self, message:str, tipo: int):
        val= message+"-"+str(tipo)
        print("Avvio processo con messaggio: "+val)
        prod=Process(target=Prod, args=(self,val,self.queue))
        prod.start()


if __name__=="__main__":

    print("Server pronto!")
    q = Queue(3)
    skeleton= LoggingImpl(queue=q) 
    # I dati inseriti dalla coda, sono consumati da un processo consumatore 
    # avviato al lancio del Logging Server
    cons=Process(target=Cons, args=(q, ))
    cons.start()
    skeleton.runSkeleton()
