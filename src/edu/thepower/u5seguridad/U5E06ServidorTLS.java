package edu.thepower.u5seguridad;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;

public class U5E06ServidorTLS {
    private static final String NOMBRE_ALMACEN = "resources/servidor.jks";
    private static final char[] PASS_ALMACEN = "changeit".toCharArray();
    private static final String NOMBRE_CERT = "resources/servidor.crt";
    private static final char[] PASS_CLAVE =  "changeit".toCharArray();

    public static void main(String[] args) {
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            try (FileInputStream fis = new FileInputStream(NOMBRE_ALMACEN)) {
                ks.load(fis, PASS_ALMACEN);
            }
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, PASS_CLAVE);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, null);

            SSLServerSocketFactory sslSocketFactory = sslContext.getServerSocketFactory();
            try(SSLServerSocket serverSocket = (SSLServerSocket) sslSocketFactory.createServerSocket(8443)) {
                System.out.println("Iniciando servidor, esperando conexiones en puerto 8443");
                try (SSLSocket socket = (SSLSocket) serverSocket.accept()) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                    String linea = br.readLine();
                    pw.println("Devuelto por el servidor: " + linea.toUpperCase());
                }
            }

        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (IOException | KeyStoreException | UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }
}
