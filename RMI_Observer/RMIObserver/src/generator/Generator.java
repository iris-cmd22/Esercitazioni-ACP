package generator;

public class Generator{

    public static void main(String[] args){
        
        int NTHREADS=3;

        try{
            GeneratorThread[] threads = new GeneratorThread[NTHREADS];

            for(int i=0;i<NTHREADS;i++){
                GeneratorThread t= new GeneratorThread();
                t.start();
                threads[i]=t;
            }

            //Join dei thread
            for(int i=0;i<NTHREADS;i++){
                threads[i].join();
            }


        }catch(Exception e){
            e.printStackTrace();
        }
    }


}