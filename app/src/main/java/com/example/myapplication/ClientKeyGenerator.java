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

    public static Boolean assetFileExists(Context context, String filename) {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open(filename);
            inputStream.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static KeyPair generateKeys(Context context) throws Exception {

        String filename = "clientKeys.txt";
        AssetManager assetManager = context.getAssets();
        File file = new File(context.getFilesDir(), filename);

        Boolean exists = assetFileExists(context, filename);

        if (assetFileExists(context, filename)) {
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

                String[] keyParts = content.split("-\n");
                String publicKeyPEM = keyParts[1].trim();
                String privateKeyPEM = keyParts[3].trim();

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
                    writer.write(privateKeyPEM);
                    copyFileToAssets(context, filename);

                } catch (Exception e) {
                    throw new Exception("Error al escribir las claves en el archivo");
                }
                return keyPair;

            } catch (Exception e) {
                throw new Exception("Error en la generación de claves");
            }
        }
    }

    private static void copyFileToAssets(Context context, String filename) throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = context.openFileInput(filename);
        OutputStream outputStream = assetManager.openFd(filename).createOutputStream();

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        inputStream.close();
        outputStream.close();
    }
}