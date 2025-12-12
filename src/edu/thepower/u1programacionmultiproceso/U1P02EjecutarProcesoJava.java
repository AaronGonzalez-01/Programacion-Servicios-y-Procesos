package edu.thepower.u1programacionmultiproceso;

import java.awt.geom.Path2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class U1P02EjecutarProcesoJava {

    private static final String JAVA = "java";
    private static final String VERSION = "-version";

    public static void main(String[] args) {
        ProcessBuilder pb = new ProcessBuilder(JAVA, VERSION);
        // 1. Redirect la salida de la información que hereda del proceso hijo
        // pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        // Muestra los errores (en este caso, el comando version se muestra con los errores)
        // pb.redirectError(ProcessBuilder.Redirect.INHERIT);

        // 2. Usando un BufferReader (canal entre el proceso que se lanza y el nuevo que se ejecuta)
        try {
            pb.redirectErrorStream(true);
            Process p = pb.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

            /*br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }*/
        } catch (IOException e) {
            //  err muestra la información del error
            System.err.println("Error al iniciar el proceso");
            e.printStackTrace();
        }
    }
}
