package edu.thepower.u2programacionmultithread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FabricaCochesConcurrente {

    static class Coche {
        private final int id;
        private final int tipo; // atributo aleatorio entre 0 y 9

        public Coche(int id, int tipo) {
            this.id = id;
            this.tipo = tipo;
        }

        public int getId() {
            return id;
        }

        public int getTipo() {
            return tipo;
        }
    }

    private final AtomicInteger idGenerator = new AtomicInteger(0);
    private final List<Coche> coches = new ArrayList<>();
    private final ConcurrentHashMap<Integer, Integer> estadisticas = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public void crearCoches(int cantidadTotal, int numHilos) throws InterruptedException {
        List<Thread> hilos = new ArrayList<>();
        int cantidadPorHilo = cantidadTotal / numHilos;
        int resto = cantidadTotal % numHilos;

        for (int i = 0; i < numHilos; i++) {
            int cantidadHilo = (i == numHilos - 1) ? cantidadPorHilo + resto : cantidadPorHilo;

            Thread hilo = new Thread(() -> {
                for (int j = 0; j < cantidadHilo; j++) {
                    int id = idGenerator.incrementAndGet();
                    int tipo = random.nextInt(10); // número aleatorio entre 0 y 9
                    Coche coche = new Coche(id, tipo);

                    synchronized (coches) {
                        coches.add(coche);
                    }

                    estadisticas.merge(tipo, 1, Integer::sum);
                }
            });

            hilos.add(hilo);
            hilo.start();
        }

        // Esperar a que todos los hilos terminen
        for (Thread hilo : hilos) {
            hilo.join();
        }
    }

    // Obtener estadísticas de ocurrencias por tipo de coche
    public ConcurrentHashMap<Integer, Integer> getEstadisticas() {
        return estadisticas;
    }

    // Obtener la lista de todos los coches creados
    public List<Coche> getCoches() {
        return coches;
    }

    public static void main(String[] args) throws InterruptedException {
        FabricaCochesConcurrente fabrica = new FabricaCochesConcurrente();
        fabrica.crearCoches(100, 5);

        System.out.println("Total de coches creados: " + fabrica.getCoches().size());
        System.out.println("Estadísticas por tipo:");
        fabrica.getEstadisticas().forEach((tipo, cantidad) -> 
            System.out.println("Tipo " + tipo + ": " + cantidad + " veces"));
    }
}
