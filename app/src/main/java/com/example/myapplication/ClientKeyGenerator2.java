package com.example.myapplication;

import android.content.Context;
import android.content.res.AssetManager;
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

public class ClientKeyGenerator2 { // El original

    public static KeyPair generateKeys(Context context) throws Exception {
        File file = new File(context.getFilesDir(), "clientKeys.pem");

        if (file.exists()) {
            try {
                Path path = Paths.get(file.getAbsolutePath());
                String fileContent = new String(Files.readAllBytes(path));

                String[] keyParts = fileContent.split("-\n");
                String publicKeyPEM = keyParts[1].replace("-----END PUBLIC KEY----", "").trim();
                String privateKeyPEM = keyParts[3].replace("-----END PRIVATE KEY----", "").trim();

                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decode(publicKeyPEM, Base64.DEFAULT)));
                PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(privateKeyPEM, Base64.DEFAULT)));

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

                String publicKeyPEM = "-----BEGIN PUBLIC KEY-----\n" + Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT);
                String privateKeyPEM = "-----BEGIN PRIVATE KEY-----\n" + Base64.encodeToString(privateKey.getEncoded(), Base64.DEFAULT);

                try (FileWriter writer = new FileWriter(file.getAbsolutePath())) {
                    writer.write(publicKeyPEM);
                    writer.write("-");
                    writer.write(privateKeyPEM);
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