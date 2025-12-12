package edu.thepower.u1programacionmultiproceso;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class U1P04EjecutarContarVocal {

    private static final String[] VOCALES = {"a","e","i","o","u"};
    private static final String JAVA = "java";
    private static  final  String CP ="-cp";
    private static final String  CLASSPATH="C:\\PSP\\Programacion-de-servicios-y-procesos";
    private static final String CLASE = "edu.thepower.u1programacionmultiproceso.U1P04ContarVocal";
    private static  final  String ARCHIVO = "./resources/vocales.txt";
    private static final String SALIDA = "./salida/";
    private static final String EXTENCIONTXT = ".txt";

    public static  void main(String[] args) {

        List<Process> procesos = new ArrayList<Process>();

        File f = new File(SALIDA);
        if (f.mkdir()) {
            System.out.println("Directorio creado correctamente");
        } else {
            System.out.println("Directorio ya existe");
        }

        for (int i = 0; i < VOCALES.length; i++) {
            ProcessBuilder pb = new ProcessBuilder(JAVA, CP, CLASSPATH, CLASE, VOCALES[i], ARCHIVO);

            pb.redirectOutput(new File(SALIDA + VOCALES[i] + EXTENCIONTXT));
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);

            try {
                procesos.add(pb.start());
            } catch (IOException e) {
                System.out.println("Error al iniciar el archivo" + e.getMessage());
            }
        }

        System.out.println("Salida finalizada correctamente");

        for (Process p : procesos) {
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        int contadorTotal = 0;

        for (int i = 0; i < VOCALES.length; i++) {

            try {
                BufferedReader br = new BufferedReader(new FileReader(SALIDA + VOCALES[i] + EXTENCIONTXT));
                int numero = Integer.parseInt(br.readLine());
                contadorTotal += numero;
                System.out.println(VOCALES[i] + ": " + numero);
                br.close();
            }catch (NumberFormatException e) {
                System.out.println("El archivo" +SALIDA + VOCALES[i] + EXTENCIONTXT+ " no contiene un número: " + e.getMessage());
            }catch (FileNotFoundException e) {
                System.err.println("Error al encontrar el archivo " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Error de lectura " + e.getMessage());
            }
        }
        System.out.println("Total de vocales contadas "+contadorTotal);
    }
}
