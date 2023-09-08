package Client;

public class Client {
	
	public static void main(String[] args){
		
		Sender sender = new Sender();
		Receiver receiver = new Receiver();
		Thread t1 = new Thread(sender);
		Thread t2 = new Thread(receiver);
		t1.start();
		t2.start();

		System.out.println("[CLIENT] Avvio thread...");

		
	}
	
	
}
