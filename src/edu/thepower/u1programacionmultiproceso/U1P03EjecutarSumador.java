package edu.thepower.u1programacionmultiproceso;

import java.io.IOException;
import java.util.Random;

public class U1P03EjecutarSumador {

    private static final int PROCESOS = 5;

    private static final String JAVA = "java";
    private static  final  String CP ="-cp";
    private static final String  CLASSPATH="C:\\PSP\\Programacion-de-servicios-y-procesos";
    private static final String CLASE = "edu.thepower.u1programacionmultiproceso.U1P03Sumador";

    public static void main(String[] args) {
        Random r = new Random();

        for (int i = 0; i < PROCESOS; i++) {
            ProcessBuilder pb = new ProcessBuilder(JAVA, CP, CLASSPATH, CLASE, String.valueOf(r.nextInt(0,100)), String.valueOf(r.nextInt(0,100)));

            try {
                pb.redirectOutput(ProcessBuilder.Redirect.INHERIT); //Tenemos la salida Standar
                pb.redirectError(ProcessBuilder.Redirect.INHERIT); //Tenemos la salida Error
                pb.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        System.out.println("Programa finalizado");
    }
}
//TAREA PARA CASA EJECUTAR 5 PROCESOS EN PARALELO INDEPENDIENTE QUE SUMAN 2 NUMEROS ALEATORIOS
