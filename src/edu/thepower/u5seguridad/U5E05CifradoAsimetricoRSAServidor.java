package edu.thepower.u5seguridad;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.logging.Logger;

public class U5E05CifradoAsimetricoRSAServidor {
    private static final Logger LOG = Logger.getLogger(U5E05CifradoAsimetricoRSAServidor.class.getName());
    private static String ALMACEN_CLAVES = "resources/servidor.jks";
    private static String ARCHIVO_CIFRADO = "salida.bin";
    private static char[] STORE_PASS = "changeit".toCharArray();
    private static String KEY_ALIAS = "servidor";
    private static char[] KEY_PASS = "changeit".toCharArray();

    public static void main(String[] args) {
        try {
            // 1. Acceder al almacén de claves (servidor.jks)
            KeyStore ks = KeyStore.getInstance("JKS");
            try (FileInputStream fis = new FileInputStream(ALMACEN_CLAVES)) {
                ks.load(fis, STORE_PASS);
            } catch (CertificateException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            LOG.info("Se ha accedido al almacén de claves.");

            // 2. Obtener clave privada
            Key key = ks.getKey(KEY_ALIAS, KEY_PASS);
            if (!(key instanceof PrivateKey)) {
                throw new IllegalStateException("La clave recuperada no es una clave privada.");
            }
            key = (PrivateKey) key;
            LOG.info("Clave privada recuperada.");

            // 3. Recuperamos el mensaje cifrado en disco
            byte[] textoCifrado;
            try (FileInputStream fis = new FileInputStream(ARCHIVO_CIFRADO)) {
                textoCifrado = fis.readAllBytes();
            }
            LOG.info("Mensaje cifrado leído.");

            // 4. Desciframos el mensaje usando la clave privada
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] textoPlano = cipher.doFinal(textoCifrado);
            LOG.info("Mensaje descifrado.");

            // 5. Mostramos el mensaje descrifrado al usuario
            String texto = new String(textoPlano);
            System.out.println("Texto descifrado: \n" + texto);

        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
}