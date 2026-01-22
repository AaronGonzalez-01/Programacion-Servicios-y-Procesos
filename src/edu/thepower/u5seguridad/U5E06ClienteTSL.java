package edu.thepower.u5seguridad;

import javax.net.ssl.*;
import java.io.*;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.SQLOutput;

public class U5E06ClienteTSL {
    private static final String TRUST_STORE = "resources/cliente-trustore.jks";
    private static final char[] TRUST_STORE_PASSWORD = "changeit".toCharArray();
    private static final int PORT = 8443;
    private static final String HOST = "localhost";

    public static void main(String[] args) {
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            try (FileInputStream fis = new FileInputStream(TRUST_STORE)) {
                ks.load(fis, TRUST_STORE_PASSWORD);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (CertificateException e) {
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);
            //3.Crear SSLContext
            SSLContext ssc = SSLContext.getInstance("TLS");
            ssc.init(null, tmf.getTrustManagers(), null);

            //4. Crear SSLSocketFactory
            SSLSocketFactory ssf = ssc.getSocketFactory();
            try(
                    SSLSocket sc = (SSLSocket) ssf.createSocket(HOST, PORT);
                    BufferedReader br = new BufferedReader(new InputStreamReader(sc.getInputStream()));
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(sc.getOutputStream()), true)
            ){
                SSLSession session = sc.getSession();
                System.out.println("protocolo negociado: " + session.getProtocol());
                System.out.println("cifrado: " + session.getCipherSuite());

                Certificate[] certs = session.getPeerCertificates();
                if(certs != null && certs.length > 0 && certs[0] instanceof X509Certificate x509){
                    System.out.println("Subject: " + x509.getSubjectX500Principal());
                    System.out.println("Issuer: " + x509.getIssuerX500Principal());
                    System.out.println("Validez: " + x509.getNotAfter());
                }

                pw.println("Hola, me llamo Aaron");
                System.out.println(br.readLine());

            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

}