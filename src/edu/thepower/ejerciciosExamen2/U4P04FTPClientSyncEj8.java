package edu.thepower.ejerciciosExamen2;
/*
Ejercicio 8 – FTPClient: sincronizar carpeta local/remota

Enunciado:
- Utilizar FTPClient (Apache Commons Net) para sincronizar una carpeta local "sync"
  con una carpeta remota "/sync" en un servidor FTP.
- Si un archivo existe solo en local, subirlo al servidor.
- Si un archivo existe solo en remoto, borrarlo del servidor.
- Si existe en ambos pero con distinto tamaño, sobrescribir el remoto subiéndolo.
- Mostrar un resumen final: cuántos archivos se han subido/actualizado, cuántos
  se han borrado en remoto y cuántos se han ignorado (ya sincronizados).
*/

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class U4P04FTPClientSyncEj8 {

    public static void main(String[] args) {
        FTPClient ftp = new FTPClient();
        String server = "eu-central-1.sftpcloud.io"; // ejemplo
        int port = 21;
        String user = "9d57e91764ea4e28830d04cbb7a2e23a";
        String pass = "vgkHH3w4MuqcR5KmPItAyRKt9HmP1TKu";

        String remoteDir = "/sync";
        File localDir = new File("sync");

        int subidos = 0;
        int borrados = 0;
        int ignorados = 0;

        try {
            ftp.connect(server, port);
            System.out.println("Conectado con el servidor FTP");

            if (!ftp.login(user, pass)) {
                System.out.println("Login incorrecto");
                return;
            }

            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);

            // Asegurar directorio remoto
            if (!ftp.changeWorkingDirectory(remoteDir)) {
                if (ftp.makeDirectory(remoteDir)) {
                    System.out.println("Creado directorio remoto: " + remoteDir);
                }
                ftp.changeWorkingDirectory(remoteDir);
            }

            // Mapear archivos remotos
            Map<String, FTPFile> remotos = new HashMap<>();
            FTPFile[] remoteFiles = ftp.listFiles();
            for (FTPFile f : remoteFiles) {
                if (f.isFile()) {
                    remotos.put(f.getName(), f);
                }
            }

            // Mapear archivos locales
            Map<String, File> locales = new HashMap<>();
            File[] localFiles = localDir.listFiles();
            if (localFiles != null) {
                for (File f : localFiles) {
                    if (f.isFile()) {
                        locales.put(f.getName(), f);
                    }
                }
            }

            // Subir nuevos y actualizar cambiados
            for (Map.Entry<String, File> entry : locales.entrySet()) {
                String nombre = entry.getKey();
                File localFile = entry.getValue();
                FTPFile remoto = remotos.get(nombre);
                if (remoto == null) {
                    if (subirArchivo(ftp, localFile, nombre)) {
                        subidos++;
                    }
                } else {
                    if (remoto.getSize() != localFile.length()) {
                        System.out.println("Diferencia de tamaño en " + nombre + " (local=" + localFile.length()
                                + ", remoto=" + remoto.getSize() + "), resubiendo...");
                        if (subirArchivo(ftp, localFile, nombre)) {
                            subidos++;
                        }
                    } else {
                        ignorados++;
                    }
                    remotos.remove(nombre);
                }
            }

            // Borrar remotos sin correspondencia local
            for (String nombre : remotos.keySet()) {
                System.out.println("Borrando remoto sin local: " + nombre);
                if (ftp.deleteFile(nombre)) {
                    borrados++;
                }
            }

            ftp.logout();

            System.out.println("Sincronización terminada.");
            System.out.println("Subidos/actualizados: " + subidos);
            System.out.println("Borrados remotos: " + borrados);
            System.out.println("Ignorados (ya sincronizados): " + ignorados);

        } catch (IOException e) {
            System.err.println("Error FTP: " + e.getMessage());
        } finally {
            try {
                if (ftp.isConnected()) {
                    ftp.disconnect();
                }
            } catch (IOException e) {
                // ignorar
            }
        }
    }

    private static boolean subirArchivo(FTPClient ftp, File localFile, String remoteName) {
        System.out.println("Subiendo: " + remoteName);
        try (InputStream is = new FileInputStream(localFile)) {
            boolean ok = ftp.storeFile(remoteName, is);
            if (ok) {
                System.out.println("Subido correctamente: " + remoteName);
            } else {
                System.out.println("Error al subir: " + remoteName);
            }
            return ok;
        } catch (IOException e) {
            System.out.println("Error al subir " + remoteName + ": " + e.getMessage());
            return false;
        }
    }
}
