package edu.thepower.u1programacionmultiproceso;

import java.io.IOException;

public class U1P01EjecutarProceso {
    public static void main(String[] args){
        U1P01EjecutarProceso p = new U1P01EjecutarProceso();

        /*No hace falta escribir la extención ".exe", lo busca automaticamente*/
        p.launcher("notepad");

        p.launcher("C:/Program Files/FileZilla FTP Client/filezilla");
    }
    public void launcher(String programa){
        /*Clase para ejecutar programas*/
        ProcessBuilder pb = new ProcessBuilder(programa);

        try {
            pb.start();
        } catch (IOException e) {
            /*utilizamos "err" para mostrar información de error*/
            System.err.println("Error al iniciar el proceso " + programa);
            e.printStackTrace();
        }
    }
}
