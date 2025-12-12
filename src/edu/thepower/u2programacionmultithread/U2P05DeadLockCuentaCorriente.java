package edu.thepower.u2programacionmultithread;

class CuentaCorriente{
    private float saldo;

    public CuentaCorriente(float saldo) {
        this.saldo = saldo;
    }

    public void retirar (float importe){
        if (saldo >= importe){
            saldo -= importe;
        }
    }

    public void ingresar (float importe){
        saldo += importe;
    }

    public float getSaldo (){
        return saldo;
    }
}

public class U2P05DeadLockCuentaCorriente {
    // Crear dos cuentas corrientes y dos threads que hagan transferencias entre cuentas.
    // Evitar deadlock, pues el hilo 1 transfiere a la cuenta 2 y el hilo 2 transfiere a la cuenta 1
    // El monitor bloquea por clase o por objeto

    public static void transferencia(CuentaCorriente origen, CuentaCorriente destino, float importe){

        // Verificamos que el HashCode de cuenta 1 sea de la cuenta origen o de la cuenta destino
        CuentaCorriente cuenta1 = origen.hashCode() < destino.hashCode() ? origen : destino;
        CuentaCorriente cuenta2 = origen.hashCode() < destino.hashCode() ? destino : origen;

        synchronized (cuenta1){
            synchronized (cuenta2){
                origen.retirar(importe);
                destino.ingresar(importe);
            }
        }
    }

    public static void main(String[] args) {
        // Creamos las cuentas corrientes y les damos salgo de 100k
        CuentaCorriente cc1 = new CuentaCorriente(100_000);
        CuentaCorriente cc2 = new CuentaCorriente(100_000);

        // Creamos un hilo que haga una transferencia
        Thread t1 = new Thread(() -> {
            for (int i=0; i<1_000; i++)
                transferencia(cc1, cc2, 10);
        });

        Thread t2 = new Thread(() -> {
            for (int i=0; i<1_000; i++)
                transferencia(cc2, cc1, 20);
        });

        // Ejecutamos los hijos
        t1.start();
        t2.start();

        try {
            // Controlamos que los hilos hayan terminado con el .join()
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Saldo cc1 " + cc1.getSaldo());
        System.out.println("Saldo cc2 " + cc2.getSaldo());
        System.out.println("Salto total de las cuentas: " + (cc1.getSaldo()+ cc2.getSaldo()));
    }
}
