package edu.thepower.u5seguridad;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.sql.SQLOutput;
import java.util.logging.Logger;

public class U5E05CifradoAsimetricoRSACliente {
    private static final Logger LOG = Logger.getLogger(U5E05CifradoAsimetricoRSACliente.class.getName());
    private static final String NOMBRE_CERTIFICADO = "resources/servidor.crt";
    private static final String ARCHIVO_CIFRADO = "salida.bin";

    public static void main() {
        try {
            // 1. Acceder al certificado que tenemos guardado en servidor.crt
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate cert;
            try(FileInputStream fis = new FileInputStream(NOMBRE_CERTIFICADO)) {
                cert = cf.generateCertificate(fis);
            }
            LOG.info("Se ha accedido al certificado");

            // 2. extraer clave publica del certificado
            PublicKey pk = cert.getPublicKey();
            LOG.info("Clave publica obtenida");

            // 3. creacion de mensaje a cifrar
            String texto = "Tengo mucha hambre";
            byte[] textoPlano = texto.getBytes();

            // 4. cifrado del mensaje
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pk);
            byte[] textoCifrado = cipher.doFinal(textoPlano);
            LOG.info("Mensaje cifrado");

            // 5. Guardamos en el disco el mensaje cifrado
            try (FileOutputStream fos = new FileOutputStream(ARCHIVO_CIFRADO)) {
                fos.write(textoCifrado);
            }
            LOG.info("Cifrado guardado");
            System.out.println("El mensaje a sido guardado " + ARCHIVO_CIFRADO);
        } catch (CertificateException | IOException e){
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

}
