package edu.thepower.u2programacionmultithread;

import java.util.concurrent.atomic.AtomicInteger;

public class U2P04CondicionCarreraAtomicVars {
    private static AtomicInteger contador = new AtomicInteger(0);

    private static void cambiarContador(int num) {
        contador.addAndGet(num);
    }

    public static int getContador() {
        return contador.get();
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
