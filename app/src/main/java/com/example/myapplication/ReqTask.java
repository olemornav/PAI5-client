package com.example.myapplication;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStreamReader;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

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

            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
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
        } catch (IOException ioException) {
            return "Error: " + ioException.getMessage();
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