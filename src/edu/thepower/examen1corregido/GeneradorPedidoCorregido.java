package edu.thepower.examen1corregido;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class GeneradorPedidoCorregido {

    static class Pedido{

        private static SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        private static AtomicInteger generadorId;
        private String id;
        private String cliente;
        private long fecha;
        //Código que se inicia una sola vez
        static {
            generadorId = new AtomicInteger(0);
        }

        public Pedido(String cliente){
            this.id = String.valueOf(generadorId.incrementAndGet());
            this.cliente = cliente;
            this.fecha = System.currentTimeMillis();
        }

        @Override
        public String toString() {
            return "Pedido:" +
                    " ID de pedido = " + id +
                    " Nombre del cliente: " + cliente + '\'' +
                    " Fecha del pedido: " +  formato.format(fecha);
        }
    }

    public static void main(String[] args) {
        final int MAX_THREADS = 10;
        final int MAX_PEDIDOS = 10;
        Random r = new Random();
        List<Pedido> pedidos = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        ConcurrentHashMap <String, AtomicInteger> pedidosPorCliente = new ConcurrentHashMap<>();

        for (int i = 0; i < MAX_THREADS; i++) {
            Thread hilo = new Thread(() -> {
                for (int j = 0; j < MAX_PEDIDOS; j++) {
                    String cliente = "Cliente_" + r.nextInt(10);
                    //Se crea y se añade el pedido
                    synchronized (pedidos){
                        pedidos.add(new Pedido(cliente));
                    }
                    //System.out.println(pedido);
                    pedidosPorCliente.computeIfAbsent(cliente, k -> new AtomicInteger()).incrementAndGet();
                }
            });
            //Almacenamiento del hilo y su iniciación
            hilo.start();
            threads.add(hilo);
        }
        System.out.println("Todos los threads generados y ejecutándose");

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Todos los threads han terminado de ejecutarse");

        System.out.println("Pedidos realizados por los clientes: " );
        for (Pedido p : pedidos) {
            System.out.println(p);
        }
        System.out.println("Número total de pedidos: " + pedidos.size());
        System.out.println("*** Cantidad de pedido por cliente: ");

        int acumulador = 0;
        for (String key : pedidosPorCliente.keySet()) {
            acumulador += pedidosPorCliente.get(key).get();
            System.out.println("El cliente " + key + " ha realizado " + pedidosPorCliente.get(key).get());
        }
        System.out.println("El total de pedidos por cliente: " + acumulador);

    }
}
