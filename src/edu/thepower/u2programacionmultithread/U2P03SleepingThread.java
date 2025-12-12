package edu.thepower.u2programacionmultithread;

public class U2P03SleepingThread implements Runnable {

    @Override
    public void run(){

        String nombreThread = "[" + Thread.currentThread().getName() + "]";

        System.out.println(nombreThread + "Iniciando thread");

        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            System.out.println("Weiky Weiky, it's time to school " + nombreThread + " 1**" );
        }

        while(!Thread.interrupted()) {
        }
        System.out.println("Weiky Weiky, it's time to school " + nombreThread + " 2**" );
    }

    public static void main(String[] args){
        Thread t = new Thread(new U2P03SleepingThread(), "SleepingThread" );
        t.start();

        String nombreThread = "[" + Thread.currentThread().getName() + "]";
        System.out.println(nombreThread + "Iniciando thread");

        for(int i = 0 ; i<2; i++){
            System.out.println(nombreThread + "Sleeping 5 seg");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(nombreThread + "Despertando Thread");
            t.interrupt();
        }
    }

}
