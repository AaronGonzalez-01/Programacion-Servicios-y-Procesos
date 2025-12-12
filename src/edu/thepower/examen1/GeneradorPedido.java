package edu.thepower.examen1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

class Pedido{
    private int idPedido;
    private String nombreCliente;
    private long fechaPedido;
    //Formato de la fecha
    private static SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

    public Pedido(int idPedido, String nombreCliente) {
        this.idPedido = idPedido;
        this.nombreCliente = nombreCliente;
        //La fecha del pedido se asigna al momento en el que es creado
        this.fechaPedido = System.currentTimeMillis();
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    @Override
    public String toString() {
        return "Pedido:" +
                " ID de pedido = " + idPedido +
                " Nombre del cliente: " + nombreCliente + '\'' +
                " Fecha del pedido: " +  formato.format(fechaPedido);
    }
}

//La clase implementa la clase Runnable para poder utilizar el metodo run
public class GeneradorPedido implements Runnable{

    //Se utilizara para contar el numero de pedidos realizados
    private static AtomicInteger ids = new AtomicInteger(0);
    //Se utiliza para guardar los datos de los pedidos
    private static List<Pedido> pedidos = new ArrayList<>();
    //Se utiliza para guardar los clientes y el número de pedidos que han hecho
    private static ConcurrentHashMap<String, Integer> estadisticas = new ConcurrentHashMap<>();
    //Se utiliza para bloquear partes de codigo de manera selectiva
    private static ReentrantLock lock = new ReentrantLock();
    //Se utiliza para generar un número aleatoreo de cliente
    private static Random r = new Random();
    //Se utiliza para contar los pedidos generados por los clientes
    private static AtomicInteger contadorPedidosGenerados = new AtomicInteger(0);

    //Metodo que se ejecuta al iniciar un thread
    @Override
    public void run() {
        //Un bucle que genera 10 pedidos
        for(int i = 0; i < 10; i++){
            //bloquea el codigo
            lock.lock();
            try {
                //Se incrementa el contador en uno
                contadorPedidosGenerados.incrementAndGet();
                //Se asigna un id unico
                int idPedido = ids.incrementAndGet();
                //Se crea el pedido
                Pedido pedido = new Pedido(idPedido, "Cliente " + r.nextInt(10));
                //Se añade a la lista de pedidos
                pedidos.add(pedido);
                //Se añade el cliente y el número de veces que hace un pedido
                estadisticas.put(pedido.getNombreCliente(), estadisticas.getOrDefault(pedido.getNombreCliente(), 0) + 1);
            }finally {
                //Se utiliza un finally para asegurarnos de que de finaliza el bloqueo si o si
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        //Se crea una lista de hilos
        List<Thread> hilos = new ArrayList<>();
        //Se hace un bucle for para crear e iniciar 10 hilos
        for (int i = 0; i < 10; i++){
            //Se añaden los hilos a la lista
            hilos.add(new Thread(new GeneradorPedido(), "Hilo: " + i));
            //Se inician los hilos
            hilos.get(i).start();
        }
        //Se hace un bucle para recorrer la lista de hilos
        for (Thread t : hilos){
            try {
                //Se utiliza el metodo join para que terminen los procesos
                t.join();
            } catch (InterruptedException e) {
                System.out.println("Error al interrupcion" + e.getMessage());
            }
        }
        //Se imprimen los datos de los pedidos
        for (Pedido pedido : pedidos){
            System.out.println(pedido);
        }
        //Se crea un acumualdor para contar las veces que se han hecho un pedido en el mapa de estadisticas
        int acumulador = 0;
        //Bucle que imprime las estadisticas
        for (String p : estadisticas.keySet()){
            acumulador += estadisticas.get(p);
            System.out.println(p + ": " + estadisticas.get(p));
        }

        //Se imprime y se comparan los totales
        System.out.println("Pedidos generados: " + contadorPedidosGenerados);
        System.out.println("Número de pedidos realizados " + acumulador);
    }
}
