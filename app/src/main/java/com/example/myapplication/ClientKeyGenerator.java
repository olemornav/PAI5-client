package com.example.myapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class ClientKeyGenerator {


    public static KeyPair generateKeys(Context context) throws Exception {

        String filename = "clientKeys.txt";
        AssetManager assetManager = context.getAssets();

        try {
            InputStream inputStream = assetManager.open(filename);
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            inputStream.close();
            String content = stringBuilder.toString();

            String[] keyParts = content.split("\n\n");
            String publicKeyPEM = keyParts[0].trim();
            String privateKeyPEM = keyParts[1].trim();

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decode(publicKeyPEM, Base64.DEFAULT)));
            PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(privateKeyPEM, Base64.DEFAULT)));

            return new KeyPair(publicKey, privateKey);

        } catch (IOException e) {
            throw new IOException("Error al leer las claves del archivo");
        }
    }

    private KeyPair generateKeyPair(Context context) throws Exception { // The keys were generated this way
        try {
            String filename = "clientKeys.txt";
            File file = new File(context.getFilesDir(), filename);

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            String publicKeyPEM = Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT);
            String privateKeyPEM = Base64.encodeToString(privateKey.getEncoded(), Base64.DEFAULT);

            try (FileWriter writer = new FileWriter(file.getAbsolutePath())) {
                writer.write(publicKeyPEM);
                writer.write("\n");
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