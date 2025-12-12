package edu.thepower.u2programacionmultithread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class U2P07ThreadPool {
    public static void main(String[] args) {
        final int MAX_POOL_SIZE = 10;
        Map<String, AtomicInteger> procesosPorThread = new ConcurrentHashMap<>();
        ExecutorService pool = Executors.newFixedThreadPool(MAX_POOL_SIZE);
        for (int i = 0; i < 50; i++) {
            pool.submit(() -> {
                procesosPorThread.computeIfAbsent(Thread.currentThread().getName(), k -> new AtomicInteger()).incrementAndGet();
                System.out.println("[" + Thread.currentThread().getName() + "] Saludos!!!");
            });
        }
        pool.shutdown();
        try {
            if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        }catch (InterruptedException e){
            pool.shutdownNow();
        }
        procesosPorThread.forEach((key, value) -> {
            System.out.println("El thread " + key + " se ha ejecutado: " + value.get() + " veces");
        });
        System.out.println("Total de ejecuciones por thread: " + procesosPorThread.values().stream().mapToInt(v -> v.get()).sum());
    }
}
