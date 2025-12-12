package edu.thepower.examen1;

public class PSPT1P1Corregido {

    //La clase impresora tiene un metodo el cual "imprime" el nombre del thread
    static class Impresora {
        //El metodo utiliza el modificador syncronized el cual bloquea la clase
        public synchronized void imprimir(String doc) {
            //Se muestra por consola el nombre del thread y la función que hace
            System.out.println(Thread.currentThread().getName() + " imprime: " +
                    doc);
            //Se utiliza el try para recibir un InterruptedException en caso de error
            try {
                //El metodo sleep para el hilo una cierta cantidad de tiempo en milisegundos
                Thread.sleep(50);
            } catch (InterruptedException ignored) {

            }
        }
    }

    //La clase scanner tiene un metodo el cual "escanea" el nombre del thread
    static class Scanner {
        //El metodo utiliza el modificador syncronized el cual bloquea la clase
        public synchronized void scan(String doc) {
            //Se muestra por consola el nombre del thread y la función que hace
            System.out.println(Thread.currentThread().getName() + " escanea: " + doc);
            //Se utiliza el try para recibir un InterruptedException en caso de error
            try {
                //El metodo sleep para el hilo una cierta cantidad de tiempo en milisegundos
                Thread.sleep(50);
            } catch (InterruptedException ignored){

            }
        }
    }
    public static void main(String[] args) {

        //Se crea un objeto Impresora
        Impresora impresora = new Impresora();
        //Se crea un objeto Scanner
        Scanner scanner = new Scanner();

        //Se crea un hilo que utiliza primero el metodo de impresora y luego el de scanner
        Thread tA = new Thread(() -> {
            synchronized (impresora) {
                System.out.println(Thread.currentThread().getName() + " acceso a impresora...");
                impresora.imprimir("Documento A");
                synchronized (scanner) {
                    scanner.scan("Documento A");
                }
            }
        }, "Tarea-A");

        //Se crea un hilo que utiliza primero el metodo de scanner y luego el de impresora
        Thread tB = new Thread(() -> {
            synchronized (impresora) {
                System.out.println(Thread.currentThread().getName() + " acceso a impresora...");
                impresora.imprimir("Documento B");
                synchronized (scanner) {
                    scanner.scan("Documento B");
                }
            }
        }, "Tarea-B");

        //Se inicializan los hilos
        tA.start();
        tB.start();

        /*
        El codigo falla o se que da pillado porque los hilos quieren acceder a los metodos imprimir y scanner.
        El hilo tA bloquea el metodo imprimir, y el hilo tB bloquea el metodo scanner
         */

        /*
        La solución al fallo es asegurarse que se accedan a los metodos en el mismo orden
         */
    }
}