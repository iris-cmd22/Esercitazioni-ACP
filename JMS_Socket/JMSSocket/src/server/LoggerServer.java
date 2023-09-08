package server;

public class LoggerServer {
    
    public static void main(String[] args){
        if(args.length!=1){
            System.out.println("Inserisci il port");
            return;
        }
        LoggerImpl logger=new LoggerImpl(Integer.parseInt(args[0]));
        logger.runSkeleton();
        System.out.println("[LOGGER] Pronto!");
    }
}
