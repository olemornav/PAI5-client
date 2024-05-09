package com.example.myapplication;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ReqTask extends AsyncTask<Void, Void, String> {

    private final String inputString;
    private final String stringURL;
    private final int port;
    private final OnRequestListener listener;

    public ReqTask(String inputString, String stringURL, int port, OnRequestListener listener) {
        this.inputString = inputString;
        this.stringURL = stringURL;
        this.port = port;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {

            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[0];
                        }
                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        }
                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory factory = sslContext.getSocketFactory();

            SSLSocket socket = (SSLSocket) factory.createSocket(stringURL, port);

            PrintWriter output = new PrintWriter(socket.getOutputStream()); // read from server
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream())); // send to server

            output.println(inputString);
            output.flush();

            String response = input.readLine(); // read response from server

            output.close();
            input.close();
            socket.close();

            return response;
        } catch (IOException | NoSuchAlgorithmException ioException) {
            return "Error: " + ioException.getMessage();
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (listener != null && !result.contains("Error")) {
            listener.onRequestResult(result);
        } else if (listener != null) {
            listener.onRequestFailure(result);
        }
    }

    public interface OnRequestListener {
        void onRequestResult(String result);
        void onRequestFailure(String errorMessage);
    }
}