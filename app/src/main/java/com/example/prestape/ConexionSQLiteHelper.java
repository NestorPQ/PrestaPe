package com.example.prestape;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class ConexionSQLiteHelper extends SQLiteOpenHelper {

    //  Definir la estructura de la(s) tabla(s) SQLite
    //  Constante se declare con "final" (Java)
    final String PRESTAMOS = ""+
            "CREATE TABLE 'prestamos' ("+
            "'idprestamo'       INTEGER NOT NULL,"+
            "'apellidos'        TEXT    NOT NULL,"+
            "'nombres'          TEXT    NOT NULL,"+
            "'telefono'         TEXT    NOT NULL,"+
            "'monto'            INTEGER    NOT NULL,"+
            "'interes'          INTEGER    NOT NULL,"+
            "PRIMARY KEY ('idprestamo'  AUTOINCREMENT)"+
            ")";

    public ConexionSQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        /*
        *   Al iniciar la APP por primera vez, se creará la BD
        * */
        sqLiteDatabase.execSQL(PRESTAMOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS prestamos");
        onCreate(sqLiteDatabase);
    }
}
