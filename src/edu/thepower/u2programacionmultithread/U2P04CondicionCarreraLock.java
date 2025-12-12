package edu.thepower.u2programacionmultithread;

import java.util.concurrent.locks.ReentrantLock;

public class U2P04CondicionCarreraLock {
    private static int contador = 0;
    private static ReentrantLock lock = new ReentrantLock();

    private static void cambiarContador(int num) {
        System.out.println("Inicializando cambiarContador");

        lock.lock();
        try {
            contador += num;
        }finally {
            lock.unlock();
        }
        System.out.println("Saliendo cambiarContador");

    }

    public synchronized static int getContador() {
        System.out.println("Inicializando getContador");

        System.out.println("Saliendo getContador");
        return contador;
    }

    public static void main(String[] args) {
        final int INTERACCIONES = 1_000_000;
        final int VALOR = 10;
        Thread incrementador =  new Thread(() -> {
            System.out.println("Iniciando ejecucion incrementador");
            for (int i = 0; i < INTERACCIONES; i++) {
               cambiarContador(VALOR);
            }
            System.out.println("Terminando ejecucion incrementador");

        });

        Thread decrementador =  new Thread(() -> {
            System.out.println("Iniciando ejecucion decrementador");
            for (int i = 0; i < INTERACCIONES; i++) {
                cambiarContador(-VALOR);
            }
            System.out.println("Terminando ejecucion decrementador");

        });

        incrementador.start();
        decrementador.start();

        try {
            incrementador.join();
            decrementador.join();
        } catch (InterruptedException e) {
            System.out.println("Error" + e.getMessage());
        }

        System.out.println("El valor final del contador es: " + getContador());
    }
}
