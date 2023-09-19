package com.example.prestape;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class registrar extends AppCompatActivity {

    EditText etApellidos, etNombres, etTelefono, etMonto, etInteres;

    Button btRegistrarPrestamo, btabrirbusqueda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        loadUI();

        btRegistrarPrestamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  1. Validación
                validarCampos();

            }
        });

        btabrirbusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),buscar.class));
            }
        });
    }

    //  1. Validar Campos
    private void validarCampos(){
        String apellidos, nombres, telefono;
        int monto, interes;

        apellidos = etApellidos.getText().toString();
        nombres = etNombres.getText().toString();
        telefono = etTelefono.getText().toString();

        //  Operador ternario
        monto = (etMonto.getText().toString().trim().isEmpty()) ? 0 : Integer.parseInt(etMonto.getText().toString());
        interes = (etInteres.getText().toString().trim().isEmpty()) ? 0 : Integer.parseInt(etInteres.getText().toString());

        if (apellidos.isEmpty() || nombres.isEmpty() || telefono.isEmpty() || monto == 0 || interes == 0){
            notificar("Completar el formulario");
            etApellidos.requestFocus();
        }else{
            //  2.  Preguntar si está seguro
            preguntar();

        }

        /*
        monto = Integer.parseInt(etMonto.getText().toString());
        interes = Integer.parseInt(etInteres.getText().toString());
        */

    }

    private void preguntar(){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("PrestaPE");
        dialogo.setMessage("¿Está seguro de registrar?");
        dialogo.setCancelable(false);

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RegistrarPrestamo();
            }
        });

        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogo.show();
    }

    private void RegistrarPrestamo(){
        //  Enviando datos...
        /* PASO 1: Intancia clase conexión  */
        ConexionSQLiteHelper conexion = new ConexionSQLiteHelper(this, "bdprestape",null, 1);

        /* PASO 2: Permisos (Lectura - Escritura) */
        SQLiteDatabase db = conexion.getWritableDatabase();

        /* PASO 3: Enviar los datos en pareja (K : V) */
        ContentValues parametros = new ContentValues();
        parametros.put("apellidos" , etApellidos.getText().toString());
        parametros.put("nombres" , etNombres.getText().toString());
        parametros.put("telefono" , etTelefono.getText().toString());
        parametros.put("monto" , etMonto.getText().toString());
        parametros.put("interes" , etInteres.getText().toString());

        /* PASO 4: Enviar los datos y capturar el ID obtenido (AI) */
        long idobtenido = db.insert( "prestamos", "idprestamo", parametros);

        /* PASO 5: Reiniciamos la interfaz*/
        notificar("Datos guardados correctamente = " + String.valueOf(idobtenido));
        reiniciar();
        etApellidos.requestFocus();

        // Ocultar el teclado virtual
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(etApellidos.getWindowToken(), 0);



    }

    private void reiniciar(){
        etApellidos.setText(null);
        etNombres.setText(null);
        etTelefono.setText(null);
        etMonto.setText(null);
        etInteres.setText(null);

    }
    private void notificar(String mensaje){
        Toast.makeText(this , mensaje, Toast.LENGTH_LONG ).show();
    }
    private void loadUI(){

        //  Java
        etApellidos = findViewById(R.id.etApellidos);
        etNombres = findViewById(R.id.etNombres);
        etTelefono = findViewById(R.id.etTelefono);
        etMonto = findViewById(R.id.etMonto);
        etInteres = findViewById(R.id.etInteres);

        btRegistrarPrestamo = findViewById(R.id.btnRegistrarPrestamos);
        btabrirbusqueda = findViewById(R.id.btabrirbusqueda);
    }
}
