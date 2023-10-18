import stomp, time

#INFO FILTER
#Implementa la ricezione sulla STOMP Queue info. 

# Alla ricezione di ciascun messaggio, 
class MyListener(stomp.ConnectionListener):

    def on_message(self, frame):
        message = (frame.body).split("-")# il listener STOMP di Info Filter estrae il contenuto del messaggio
        print("[INFO FILTER] Messaggio ricevuto: ",message) #oltre che visualizzarla a video

        if(message=='1'): #se esso contiene il valore 1

            #allora scrive il contenuto del messaggio sul file info.txt
            print("[INFO FILTER] Scrivo il messaggio su file ...")
            with open("info.txt",mode='a') as file: #uso l'alias file
                file.write("\n"+message)
            file.close()


if __name__=="__main__":

    conn=stomp.connect("127.0.0.1",61613) #localhost,STOMP

    #creazione del listener
    listener=MyListener()
    conn.set_listener("",listener)
    conn.connect()
    conn.subscribe("/queue/info",id=1,ack="auto") #Implementa la ricezione sulla STOMP Queue info.

    while(True):
        time.sleep(1)