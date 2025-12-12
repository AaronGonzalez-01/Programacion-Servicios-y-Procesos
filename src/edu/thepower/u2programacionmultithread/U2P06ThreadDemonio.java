package edu.thepower.u2programacionmultithread;


public class U2P06ThreadDemonio {
    public static void main(String[] args) {
        long tiempo =  System.currentTimeMillis() + 10000;
        Thread h1 = new Thread(() -> {
            while (tiempo > System.currentTimeMillis()) {
                System.out.println("Thread 1");
                try {
                   Thread.sleep(1000);
                } catch (InterruptedException ignored){
                   System.out.println(Thread.currentThread().getName() + ": interrupted");
                }
            }
        });
        Thread h2 = new Thread(() -> {
            while (tiempo > System.currentTimeMillis()) {
                System.out.println("Thread 2");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored){
                    System.out.println(Thread.currentThread().getName() + ": interrupted");
                }
            }
        });
        Thread heartbeat = new Thread(() -> {
            while (true) {
                System.out.println("Beat beat");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored){}
            }
        });
        heartbeat.setDaemon(true);
        System.out.println("Iniciando threads");
        h1.start();
        h2.start();
        heartbeat.start();
        System.out.println("Ejecutando threads");
    }
}
