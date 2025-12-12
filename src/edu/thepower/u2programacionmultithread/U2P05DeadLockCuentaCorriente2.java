package edu.thepower.u2programacionmultithread;

class CuentaCorriente2{
    private float saldo;

    public CuentaCorriente2(float saldo) {
        this.saldo = saldo;
    }
    public float getSaldo() {
        return saldo;
    }
    public void retirarSaldo(float importe) {
        if (saldo > importe) {
            saldo -= importe;
        }
    }
    public void ingresarSaldo(float importe) {
        saldo += importe;
    }
}

public class U2P05DeadLockCuentaCorriente2 {
    public static void transferirSaldo(CuentaCorriente2 origen, CuentaCorriente2 destino, float importe) {
        CuentaCorriente2 aux1 = origen.hashCode() < destino.hashCode() ? origen : destino;
        CuentaCorriente2 aux2 = origen.hashCode() < destino.hashCode() ? destino : origen;

        synchronized (aux1) {
            synchronized (aux2) {
                origen.retirarSaldo(importe);
                destino.ingresarSaldo(importe);
            }
        }
    }

    public static void main(String[] args) {
        CuentaCorriente2 cc1 = new CuentaCorriente2(100_000);
        CuentaCorriente2 cc2 = new CuentaCorriente2(100_000);


        Thread t1 = new Thread(() -> {
            for (int i=0; i<1_000; i++)
                transferirSaldo(cc1, cc2, 10);
        });

        Thread t2 = new Thread(() -> {
            for (int i=0; i<1_000; i++)
                transferirSaldo(cc2, cc1, 20);
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupted" + e.getMessage());
        }

        System.out.println("Saldo cuenta 1: " + cc1.getSaldo());
        System.out.println("Saldo cuenta 2: " + cc2.getSaldo());
        System.out.println("Saldo total: " + (cc1.getSaldo() + cc2.getSaldo()));

    }
}
