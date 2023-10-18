import time
import stomp
import sys


class ListnerChecker(stomp.ConnectionListener):

    def __init__(self, param):
        self.p = param

    def on_message(self, frame):
        
        message = (frame.body).split("-")[0]

        print("Valore arrivato: " + message)

        if message == PARAM:
            
            print("Scrivo su error.txt: "+ message)

            with open("error.txt", mode="a") as file:
                file.write("\n"+ message)


            file.close()

if __name__ == "__main__":

    try:
        PARAM = sys.argv[1]

    except IndexError:
        print("Aggiun gi fatal o error!")

    #ricezione stomp

    conn = stomp.Connection([("127.0.0.1", 61613)])

    #listner
    listner = ListnerChecker(PARAM)
    conn.set_listener("", listner)

    conn.connect()

    conn.subscribe("/queue/error", id=2, ack="auto")

    while True:
        time.sleep(10)
