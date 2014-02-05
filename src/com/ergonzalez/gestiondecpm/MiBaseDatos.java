package com.ergonzalez.gestiondecpm;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class MiBaseDatos extends SQLiteOpenHelper {

	private static final int VERSION_BASEDATOS = 1;

    // Nombre de nuestro archivo de base de datos
    private static final String NOMBRE_BASEDATOS = "mibasedatos.db";
    
    // Sentencia SQL para la creación de una tabla
    private static final String TABLA_CONFIGURACIONES = "CREATE TABLE configuraciones" +  
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +  "nombre TEXT, descripcion TEXT, conexion INT, tipo INT)";


    // CONSTRUCTOR de la clase
    public MiBaseDatos(Context context) {
        super(context, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLA_CONFIGURACIONES);
    }//onCreate

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_CONFIGURACIONES);
        onCreate(db);
    }//onUpgrade
    
    
    public void insertarCONFIGUARCION(int id, String nom, String des, int conx, int tipo) {
    	try{
    		SQLiteDatabase db = getWritableDatabase();
            if(db != null){
                ContentValues valores = new ContentValues();
                //valores.put("_id", id);
                valores.put("nombre", nom);
                valores.put("descripcion",des);
                valores.put("conexion", conx);
                valores.put("tipo", tipo);
                db.insert("configuraciones", null, valores);
                db.close();   
            }
    	}
    	catch (Exception e){
    		Log.d("INSERTAR CONFIGURACION", "Excepción: " + e.getMessage());
    	}
    	finally

    	{

    	// código que se ejecuta siempre, se produzca o no excepción;
    		// CharSequence texto = "Paso por Insertar Contacto ";
    		 //Toast t = Toast.makeText(this,texto,Toast.LENGTH_SHORT);
    		// t.show();

    	}
        
    }//insertarConfiguracion
    
    public void modificarCONFIGURACION(int id, String nom, String des, int conx, int tipo){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        //valores.put("_id", id);
        valores.put("nombre", nom);
        valores.put("descripcion",des);
        valores.put("conexion", conx);
        valores.put("tipo", tipo);
        db.update("configuraciones", valores, "_id=" + id, null);
        db.close();   
    }//modificarConfiguracion
    
    public void borrarCONFIGURACION(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("configuraciones", "_id="+id, null);
        db.close();  
    }//borrarConfiguracion
    
    public boolean comprobarNOMBRE(String nom){
    	SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"_id", "nombre", "descripcion", "conexion", "tipo"};
        Cursor c = db.query("configuraciones", valores_recuperar, "nombre=" + nom, 
                null, null, null, null, null);
        if(c != null) {
            return false;
        }else  	return true;
    }//comprobarNOMBRE
     
    
    
    public Configuraciones recuperarCONFIGURACION(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"_id", "nombre", "descripcion", "conexion", "tipo"};
        Cursor c = db.query("configuraciones", valores_recuperar, "_id=" + id, 
                null, null, null, null, null);
        if(c != null) {
            c.moveToFirst();
        }
        Configuraciones configuraciones = new Configuraciones (c.getInt(0), c.getString(1), 
                c.getString(2), c.getInt(3), c.getInt(4));
        db.close();
        c.close();
        return configuraciones;
    }//recuperarConfiguarciones
    
    
    public List<Configuraciones> recuperarCONFIGURACIONES() {
    	
    	
        SQLiteDatabase db = getReadableDatabase();
        List<Configuraciones> lista_contactos = new ArrayList<Configuraciones>();
        
        try{
        String[] valores_recuperar = {"_id", "nombre", "descripcion", "conexion", "tipo"};
        Cursor c = db.query("configuraciones", valores_recuperar,null, 
                null, null, null, null,null);
        c.moveToFirst();
        do {
            Configuraciones configuracion = new Configuraciones(c.getInt(0), c.getString(1), 
                    c.getString(2), c.getInt(3), c.getInt(4));
            lista_contactos.add(configuracion);
        } while (c.moveToNext());
        db.close();
        c.close();
    	}catch(Exception e){
    		Log.d("BD RECUPERARCONTACTOSS", "Excepción: " + e.getMessage());}
        return lista_contactos;
    } 
    
}

