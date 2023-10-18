import socket
import time
import ILogging

class Proxy(ILogging.ILogging):

    def __init__(self):

        #Creazione di una socket IPv4,TCP
        self.sock=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
        self.sock.connect(("127.0.0.1",3000))
        print("[Service-Proxy]Creazione socket avvenuta!")

    def log(self,messaggio,tipo):

        #La richiesta è caratterizzata da 
        # 1) messaggioLog (String), ossia il messaggio della entry di log, 
        # 2) tipo (int), ossia la tipologia di entry di log (0 = DEBUG, 1 = INFO, 2 = ERROR).
        print("[Service-Proxy] Log da inviare con messaggio", messaggio, " e tipo ", str(tipo))
        stringa = messaggio+"-"+str(tipo) #Uscirà per esempio fatal-2
        time.sleep(1) #sleep di un secondo (Il Client attende 1 secondo tra le invocazioni)
        self.sock.send(stringa.encode("utf-8"))#mandiamo una stringa UTF-8
        print("[Service-Proxy] log inviato!")