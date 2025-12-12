package edu.thepower.examen1corregido;

public class PSPT1P1Corregido2 {

    interface Dispositivo {
        void ejecutar(String doc);
    }

    static class Impresora implements Dispositivo {
        public synchronized void imprimir(String doc) {
            System.out.println(Thread.currentThread().getName() + " imprime: " + doc);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " interrumpido");
            }
        }

        @Override
        public void ejecutar(String doc) {
            imprimir(doc);
        }
    }

    static class Scanner implements Dispositivo {
        public synchronized void scan(String doc) {
            System.out.println(Thread.currentThread().getName() + " escanea: " + doc);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " interrumpido");
            }
        }

        @Override
        public void ejecutar(String doc) {
            scan(doc);
        }
    }

    public static void main(String[] args) {

        Impresora impresora = new Impresora();
        Scanner scanner = new Scanner();

        Thread tA = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " accede a impresora...");
            ejecutarTrabajos(impresora, scanner, "Documento A");

        }, "Tarea-A");

        Thread tB = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " accede a scanner...");
            ejecutarTrabajos(scanner, impresora, "Documento B");
        }, "Tarea-B");

        tA.start();
        tB.start();

    }

    public static void ejecutarTrabajos(Dispositivo d1, Dispositivo d2, String doc) {
        Dispositivo auxiliar1 = d1.hashCode() > d2.hashCode() ? d1 : d2;
        Dispositivo auxiliar2 = d2.hashCode() > d1.hashCode() ? d2 : d1;
        synchronized (auxiliar1) {
            synchronized (auxiliar2) {
                auxiliar1.ejecutar(doc);
                auxiliar2.ejecutar(doc);
            }
        }
    }
}
