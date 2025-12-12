package edu.thepower.u2programacionmultithread;

public class U2P05DeadLock {
    // Objetos genéricos
    private static Object obj1 = new Object();
    private static Object obj2 = new Object();

    public static void main(String[] args) {
        // Thread t1 Bloquea los objetos en este orden: obj1 > obj2
        Thread t1 = new Thread(() -> {
            synchronized (obj1){
                System.out.println("t1: Dentro del bloque obj1");
                synchronized (obj2){
                    System.out.println("t2: Dentro del bloque obj2");
                }
            };
        });
        // Thread t2 Bloquea los objetos en este orden: obj2 > obj1
        Thread t2 = new Thread(() -> {
            synchronized (obj2){
                System.out.println("t2: Dentro del bloque obj2");
                synchronized (obj1){
                    System.out.println("t1: Dentro del bloque obj1");
                }
            };
        });

        t1.start();
        t2.start();
    }
}