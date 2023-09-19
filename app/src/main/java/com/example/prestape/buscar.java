package com.example.prestape;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class buscar extends AppCompatActivity {

    ConexionSQLiteHelper conexion;
    EditText etidbuscado , etApellido, etNombre, etTelefono, etMonto, etInteres;
    Button btBuscar, btReiniciar, btActualizar, btEliminar, btReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);

        loadUI();

        //  Apertura conexión BD
        conexion = new ConexionSQLiteHelper(getApplicationContext(),"bdprestape", null,1);

        btBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarPrestamo();
            }
        });

        btReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reiniciar();
            }
        });

        btActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preguntarActualizacion();
            }
        });

        btEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preguntarEliminacion();
            }
        });

        btReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), registrar.class));
            }
        });

    }

    private void preguntarActualizacion(){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("PrestaPE");
        dialogo.setMessage("¿Está seguro de actualizar este prestamo?");
        dialogo.setCancelable(false);

        dialogo.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                actualizarPrestamo();
            }
        });

        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogo.show();
    }

    private void preguntarEliminacion(){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("PrestaPE");
        dialogo.setMessage("¿Está seguro de eliminar este prestamo?");
        dialogo.setCancelable(false);

        dialogo.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                eliminarPrestamo();
            }
        });

        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogo.show();

    }

    private void eliminarPrestamo(){
        SQLiteDatabase db = conexion.getWritableDatabase();

        String[] campoCriterio = {etidbuscado.getText().toString()};

        int filaseliminadas = db.delete("prestamos", "idprestamo=?", campoCriterio);

        if (filaseliminadas > 0){
            notificar("Prestamo eliminado con éxito");
            reiniciar();
        }else {
            notificar("Error al eliminar el préstamo");
        }
    }
    private void actualizarPrestamo(){
        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("apellidos",etApellido.getText().toString());
        valores.put("nombres",etNombre.getText().toString());
        valores.put("telefono",etTelefono.getText().toString());
        valores.put("monto",etMonto.getText().toString());
        valores.put("interes",etInteres.getText().toString());

        String[] campoCriterio = {etidbuscado.getText().toString()};

        int filasActualizadas  = db.update("prestamos", valores , "idprestamo=?", campoCriterio );

        if (filasActualizadas > 0){
            notificar("Datos Actualizados con éxito");
            reiniciar();

        }else {
            notificar("Error al actualizar los datos");
        }
    }

    private void buscarPrestamo(){
        //PASO 1: Permiso
        SQLiteDatabase db = conexion.getReadableDatabase();

        //PASO 2: Arreglo con los datos a buscar
        String[] campoCriterio = { etidbuscado.getText().toString() };

        //PASO 3: Campos a obtener (retorno)
        String[] campos = {"apellidos","nombres","telefono","monto","interes"};

        //PASO 4: Excepciones
        try {

            //PASO 5: Ejecutar la consulta
            Cursor cursor = db.query("prestamos",campos, "idprestamo=?", campoCriterio, null,null,null);
            cursor.moveToFirst();

            //PASO 6: cursor envia información cajas
            etApellido.setText(cursor.getString(0));
            etNombre.setText(cursor.getString(1));
            etTelefono.setText(cursor.getString(2));
            etMonto.setText(cursor.getString(3));
            etInteres.setText(cursor.getString(4));

            //PASO 7: Cerrar el cursor
            cursor.close();


            // Ocultar el teclado virtual
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(etidbuscado.getWindowToken(), 0);


        }catch (Exception e){
            Toast.makeText(this, "NO EXISTE EL ID " , Toast.LENGTH_LONG).show();
            reiniciar();
        }


    }
    private void reiniciar(){
            try {
                // Tu código para buscar el préstamo
                etidbuscado.setText(null);
                etApellido.setText(null);
                etNombre.setText(null);
                etTelefono.setText(null);
                etMonto.setText(null);
                etInteres.setText(null);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }


    }

    private void  reiniciarId(){
        etidbuscado.setText(null);
    }

    private void notificar(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
    }
    private void loadUI(){
        etApellido = findViewById(R.id.etBuscarApellidos);
        etNombre = findViewById(R.id.etBuscarNombres);
        etTelefono = findViewById(R.id.etBuscarTelefono);
        etMonto = findViewById(R.id.etBuscarMonto);
        etInteres = findViewById(R.id.etBuscarInteres);

        etidbuscado = findViewById(R.id.etIDBuscado);


        btBuscar = findViewById(R.id.botonbuscar);
        btReiniciar = findViewById(R.id.btReiniciar);
        btActualizar = findViewById(R.id.ActualizarPrestamos);
        btEliminar = findViewById(R.id.EliminarPrestamos);
        btReg = findViewById(R.id.btvolverReg);
    }
}