import log
import socket
import time 

#implemento il metrodo di legging sotto l'aspetto comunicativo


class Proxy(log.Logging):

    def __init__(self):
        #definisco la parte di comunicazione da effettuare...

        self.sock = socket.socket( socket.AF_INET , socket.SOCK_STREAM)

        #definisico la saocket da utilizzare

        self.sock.connect(("127.0.0.1", 3000))
        #address la tupla di localhost e il porto!

        print("Socket creata!")



    def log(self, message: str, type: int):

        #invio il messaggio sulla socket!

        print("Messaggio da inviare: " + message + str(type))

        #concateno gia' qui la stringa peer comodita'

        val = message+"-"+str(type)

        print("Messaggio che invio: " + val)

        time.sleep(1)

        self.sock.send(val.encode("utf-8"))

        print("Messaggio inviato!")






        

