package edu.thepower.u2programacionmultithread;

//Creacion de thread implementando interface runbable
class ThreadImplement implements Runnable {

    @Override
    public void run() {
        System.out.println("El nombre del thread es: " + Thread.currentThread().getName());
        System.out.println("El ID del thread es: " + Thread.currentThread().threadId());
    }

}

//Declaración de clase Thread mediante de la herencia de la clase padre thread
public class U2P01CreacionThread extends Thread{

    //Este es el codigo que se ejecuta cuando lanzo el thread
    @Override
    public void run() {
        System.out.println("El nombre del thread es: " + Thread.currentThread().getName());
        System.out.println("El ID del thread es: " + Thread.currentThread().threadId());
    }

    public static void main(String[] args) {

        //Declaracion de thread mediante lambda
        Thread t1 = new Thread(()->{
            System.out.println("El nombre del thread es: " + Thread.currentThread().getName());
            System.out.println("El ID del thread es: " + Thread.currentThread().threadId());
        }, "Thread lambda");

        Thread t2 = new Thread(new U2P01CreacionThread(), "Thread Herencia");

        for (int i = 0; i<=5; i++) {
            Thread t3 = new Thread(new ThreadImplement(), "Thread Implements" + i);
            t3.start();
        }

        t1.start();
        t2.start();

    }

}
