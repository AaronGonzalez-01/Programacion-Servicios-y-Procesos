package edu.thepower.u2programacionmultithread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class U2P04CondicionCarreraSemaforo implements Runnable {
    private static long tiempoPrueba = System.currentTimeMillis() + 100;
    private static Semaphore semaforo = new Semaphore(5, true);
    private static AtomicInteger contador = new AtomicInteger(0);
    private static Map<String, Integer> mapa = new HashMap<>();
    private static ReentrantLock lock = new ReentrantLock();
    private static int contadorSemaforo = 0;

    @Override
    public void run() {
        String nombre = "[" + Thread.currentThread().getName() + "] ";
        while (System.currentTimeMillis() < tiempoPrueba) {
            try {
                semaforo.acquire();
                lock.lock();
                try {
                    contadorSemaforo++;
                    mapa.put(nombre, mapa.getOrDefault(nombre, 0) + 1);
                }finally {
                    lock.unlock();
                }
                //mapa.computeIfAbsent(nombre, k -> new AtomicInteger()).incrementAndGet();
                System.out.println("Valor insertado en el mapa " + mapa.get(nombre));
                System.out.println(nombre + "Adquirido semaforo número: " + contador.incrementAndGet());
            } catch (InterruptedException e) {
                System.out.println("Error: " + e.getMessage());
            }
            if (contador.get() > 5) {
                throw new RuntimeException("Semaforo sobrepasado");
            }
            contador.decrementAndGet();
            semaforo.release();
            System.out.println(nombre + "Semaforo liberado");
        }
    }

    public static void main(String[] args) {
        List<Thread> lista = new ArrayList<>();


        for (int i = 0; i < 10; i++) {
            lista.add(new Thread(new U2P04CondicionCarreraSemaforo(), "Thread " + i));
            lista.get(i).start();
        }
        for (Thread t : lista) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Uso del semáforo por los threads: ");
        int acumulador = 0;
        for(String nombre : mapa.keySet()) {
            acumulador += mapa.get(nombre);
            System.out.println("El thread " + nombre + "ha usado el semaforo " + mapa.get(nombre) + " veces");
        }
        System.out.println("Número total del uso del semáforo por los threads: " + contadorSemaforo);
        System.out.println("Acumulador del uso: " + acumulador);
    }
}
