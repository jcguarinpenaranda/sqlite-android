package com.otherwise_studios.sqliteconnection;

import android.app.Activity;
import android.content.ContentValues;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText et_codigo, et_nombre, et_facultad;
    SQLManager sqlmanager;
    RelativeLayout relativeLayout;
    SQLiteDatabase db;
    int initialColor;
    int otherColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_codigo = (EditText) findViewById(R.id.et_codigo);
        et_nombre = (EditText) findViewById(R.id.et_nombre);
        et_facultad = (EditText) findViewById(R.id.et_facultad);

        initialColor = Color.WHITE;
        otherColor = Color.GRAY;

        relativeLayout= (RelativeLayout) findViewById(R.id.RelativeLayout);
        relativeLayout.setBackgroundColor(initialColor);

        sqlmanager = new SQLManager(getApplicationContext(),"basededatosmoviles",null,1);
        db = sqlmanager.getWritableDatabase();
       // db.execSQL("create table estudiante(codigo int primary key,nombre text,facultad text)");
    }


    public void guardar(View view){

        db = sqlmanager.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("codigo", et_codigo.getText().toString());
        values.put("nombre", et_nombre.getText().toString());
        values.put("facultad", et_facultad.getText().toString());

        db.insert("estudiante", null, values);
        db.close();

        Toast.makeText(getApplicationContext(), "Estudiante "+et_nombre.getText().toString()+" agregado exitosamente.", Toast.LENGTH_SHORT).show();

        et_codigo.setText("");
        et_nombre.setText("");
        et_facultad.setText("");
    }

    public void recuperarPorCodigo(View view){
        db = sqlmanager.getWritableDatabase();
        Cursor cursor = db.rawQuery("select nombre,facultad from estudiante where codigo=" + et_codigo.getText().toString(), null);
        if(cursor.moveToFirst()){
            et_nombre.setText(cursor.getString(cursor.getColumnIndex("nombre")));
            et_facultad.setText(cursor.getString(cursor.getColumnIndex("facultad")));
        }else {
            Toast.makeText(getApplicationContext(),"No se han encontrado resultados",Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public void recuperarPorFacultad(View view){
        db = sqlmanager.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from estudiante where facultad like '" + et_facultad.getText().toString() + "'", null);
        if(cursor.moveToFirst()){
            et_nombre.setText(cursor.getString(cursor.getColumnIndex("nombre")));
            et_codigo.setText(cursor.getString(cursor.getColumnIndex("codigo")));
        }else{
            Toast.makeText(getApplicationContext(),"No se han encontrado resultados",Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public void borrarPorCodigo(View view){
        db = sqlmanager.getWritableDatabase();
        int cursor = db.delete("estudiante","codigo="+et_codigo.getText().toString(),null);
        if(cursor>0){
            Toast.makeText(getApplicationContext(),"Se ha borrado el estudiante",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"Estudiante no existe",Toast.LENGTH_SHORT).show();
        }
        et_codigo.setText("");
        et_nombre.setText("");
        et_facultad.setText("");
        db.close();
    }

    public  void editar(View view){
        db = sqlmanager.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", et_nombre.getText().toString());
        values.put("facultad", et_facultad.getText().toString());

        int c = db.update("estudiante", values, "codigo="+et_codigo.getText().toString(), null);
        if(c>0){
            Toast.makeText(getApplicationContext(),"Estudiante actualizado",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"Estudiante no existe",Toast.LENGTH_SHORT).show();
        }
        et_codigo.setText("");
        et_nombre.setText("");
        et_facultad.setText("");
        db.close();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen for landscape and portrait
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            relativeLayout.setBackgroundColor(otherColor);
           // Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            //Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            relativeLayout.setBackgroundColor(initialColor);

        }
    }
}
