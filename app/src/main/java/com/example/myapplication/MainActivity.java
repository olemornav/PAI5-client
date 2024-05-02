package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    // Setup Server information
    protected static String server = "192.168.1.133";
    protected static int port = 7070;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Capturamos el boton de Enviar
        View button = findViewById(R.id.button_send);

        // Llama al listener del boton Enviar
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });


    }

    // Creación de un cuadro de dialogo para confirmar pedido
    private void showDialog() throws Resources.NotFoundException {
        EditText camas = (EditText) findViewById(R.id.numCamas);
        String numCamas = camas.getText().toString();
        EditText mesas =  (EditText) findViewById(R.id.numMesas);
        String numMesas = mesas.getText().toString();
        EditText sillas =  (EditText) findViewById(R.id.numSillas);
        String numSillas = sillas.getText().toString();
        EditText sillones = (EditText) findViewById(R.id.numSillones);
        String numSillones = sillones.getText().toString();
        EditText cliente = (EditText) findViewById(R.id.editTextNumber2);
        String numCliente = cliente.getText().toString();

        if (false) {
            // Mostramos un mensaje emergente;
            Toast.makeText(getApplicationContext(), "Selecciona al menos un elemento", Toast.LENGTH_SHORT).show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Enviar")
                    .setMessage("Se va a proceder al envio")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                // Catch ok button and send information
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    // 1. Extraer los datos de la vista

                                    // 2. Firmar los datos

                                    // 3. Enviar los datos


                                    //TODO: poner condiciones
                                    if (true){
                                        Toast.makeText(MainActivity.this, "Petición OK", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(MainActivity.this, "Petición INCORRECTA", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }

                    )
                    .

                            setNegativeButton(android.R.string.no, null)

                    .

                            show();
        }
    }


}
