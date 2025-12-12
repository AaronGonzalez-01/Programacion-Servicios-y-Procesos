package edu.thepower.u4serviciosred;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
/*
public class U4P04FTPClient {
    public static void main(String[] args) {
        FTPClient ftp = new FTPClient();
        // 1.conectarse
        try {
            ftp.connect("eu-central-1.sftpcloud.io");
            System.out.println("Conectado con el servidor");
            //2. acceder
            if(ftp.login("9d57e91764ea4e28830d04cbb7a2e23a", "vgkHH3w4MuqcR5KmPItAyRKt9HmP1TKu")){
                System.out.println("Acceso correcto");
                // 3.activar modo pasivo ( cliente a servidor) y dato modo binario
                ftp.enterLocalPassiveMode();
                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                // 4.enviar archivo
                InputStream is = new FileInputStream("resources/archivo.txt");
                String nombreArchivoRemoto = "archivo_remoto.txt";
                if(ftp.storeFile(nombreArchivoRemoto, is)){
                    System.out.println("Se ha creado el archivo remoto");
                } else{
                    System.out.println("No se ha creado el archivo remoto");
                }
                //5.listado de contenido del serfivor FTP
                FTPFile[] array = ftp.listFiles();
                for(FTPFile f : array){
                    System.out.println(f.getName());
                }
            } else {
                System.out.println("Error al conectar con el servidor");
            }
            //6.descarga de archivo
            OutputStream os = new FileOutputStream("resources/archivo_descargado.txt");
            if(ftp.retrieveFile(nombreArchivoRemoto, os)){
                System.out.println("Se ha creado el archivo remoto");
            } else {
                System.out.println("No se ha creado el archivo remoto");
            }

        } catch (IOException e) {
            System.err.println("Error al iniciar conexion con el servidor" + e.getMessage());
        }
    }
}
*/