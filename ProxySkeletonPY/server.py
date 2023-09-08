from interface import Service
import socket, sys
import multiprocess as mp

#Process function
def proc_fun(c,service):

    #Riceviamo la richiesta
    data = c.recv(1024)

    #Verifichiamo il tipo della richiesta invocando il Service method appropriato
    if str(data.decode()) == "preleva":
        result=service.preleva()

    else:
        id = str(data.decode()).split('-')[1]
        result = service.deposita(id)

    #Mandiamo il messaggio di risposta
    c.send(str(result).encode())

    #Close connection
    c.close()

# Skeleton
class ServiceSkeleton(Service):

    def __init__(self, port, queue):
        self.port = port
        self.queue = queue

    def deposita(self,id):
        pass

    def preleva(self):
        pass

    def run_skeleton(self):

        host = 'localhost' #indirizzo IP a cui il server si legherà

        #Creiamo e facciamo il bind della socket
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM) #socket iPv4 e TCP
        s.bind((host, self.port)) #binding fra l'indirizzo IP e port al socket creato

        s.listen(5) #la socket inizia ad ascoltare le richieste in arrivo con una 
                    #coda max di connessioni in attesa pari a cinque
        print("Socket is listening")

        while True:

            #Stabiliamo una connessione con il clent
            c, addr = s.accept()

            #Iniziamo un nuovo processo per servire la richiesta
            p=mp.Process(target=proc_fun, args=(c,self))
            p.start()

        s.close()


#Service Implementation
class ServiceImpl(ServiceSkeleton):
    
    def deposita(self, data):

        self.queue.put(data)
        print("[SERVER-IMPL] Depositato", data)

        return "deposited"
    
    def preleva(self):

        result = self.queue.get()
        print("[SERVER-IMPL] Prelevato",result)

        return result
        
if __name__=="__main__":

    try:
        PORT = sys.argv[1]
    except IndexError:
        print("Please, specify PORT arg")

    print("Server running ...")

    #Creiamo la Queue
    q=mp.Queue(5)

    #Creiamo il Service e facciamo la run dello Skeleton
    serviceImpl = ServiceImpl(int(PORT),q)
    serviceImpl.run_skeleton()
