package com.example.myapplication;

import android.content.Context;
import android.util.Base64;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class ClientKeyGenerator {

    public static KeyPair generateKeys(Context context) throws Exception {
        File file = new File(context.getFilesDir(), "clientKeys.txt");

        if (file.exists()) {
            try {
                Path path = Paths.get(file.getAbsolutePath());
                String fileContent = new String(Files.readAllBytes(path));

                String[] keyParts = fileContent.split("-");
                String publicKeyStr = keyParts[0].trim();
                String privateKeyStr = keyParts[1].trim();

                byte[] publicKeyBytes = Base64.decode(publicKeyStr, Base64.DEFAULT);
                byte[] privateKeyBytes = Base64.decode(privateKeyStr, Base64.DEFAULT);

                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
                PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

                return new KeyPair(publicKey, privateKey);

            } catch (IOException e) {
                throw new IOException("Error al leer las claves del archivo");
            }

        } else {
            try {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048);
                KeyPair keyPair = keyPairGenerator.generateKeyPair();

                PublicKey publicKey = keyPair.getPublic();
                PrivateKey privateKey = keyPair.getPrivate();

                String publicKeyStr = Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT);
                String privateKeyStr = Base64.encodeToString(privateKey.getEncoded(), Base64.DEFAULT);

                try (FileWriter writer = new FileWriter(file.getAbsolutePath())) {
                    writer.write(publicKeyStr);
                    writer.write("-");
                    writer.write(privateKeyStr);
                } catch (Exception e) {
                    throw new Exception("Error al escribir las claves en el archivo");
                }
                return keyPair;

            } catch (Exception e) {
                throw new Exception("Error en la generación de claves");
            }
        }
    }
}