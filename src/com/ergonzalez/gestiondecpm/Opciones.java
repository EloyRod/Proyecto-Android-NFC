package com.ergonzalez.gestiondecpm;


import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class Opciones extends Activity {
	final ArrayList mLista = new ArrayList();
	private ListView lvDatos;
	Bundle b;
	int IDOP;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opcion_config);
        
        
        try{
	        ArrayList<String> datos = new ArrayList<String>();
	       
	        mLista.add(getString(R.string.editar));
	        mLista.add(getString(R.string.eliminar));
	        mLista.add(getString(R.string.ejecutar));
	        mLista.add(getString(R.string.grabartag));

	        
	        lvDatos = (ListView) findViewById(R.id.ListView_opciones);
	        
	       
	        final ArrayAdapter mAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mLista);
	        lvDatos.setAdapter(mAdapter);
	        
	        b = getIntent().getExtras();
	        IDOP = b.getInt("IDprincipal");
	       
	        
	        lvDatos.setOnItemClickListener(new OnItemClickListener() { 
				@Override
				public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {					

					if (posicion == 0){											 
					     	
						Intent i = new Intent (Opciones.this,nueva_config.class);
						i.putExtra("IDprincipal",IDOP);
						i.putExtra("valorBoolean", true);
						startActivity(i);
	
					}else if(posicion == 1){
			
				        MiBaseDatos MBD = new MiBaseDatos(getApplicationContext());
				     	MBD.borrarCONFIGURACION(IDOP);
				     	MensajeEliminar();
					}else if(posicion == 2){
						Intent i = new Intent (Opciones.this,Conexiones.class);
						System.out.println("este es: " + IDOP);
						i.putExtra("IDprincipal",IDOP);
						i.putExtra("valorBoolean", true);
						startActivity(i);
					}else if(posicion == 3){
		
						Intent i = new Intent (Opciones.this,nueva_etiqueta.class);
						i.putExtra("grabarTAG",(Integer.toString(IDOP)));
						startActivity(i);
					}
					
					finish();
				}//onItemClick
	        });//setOnItemClickListener
	        
        }catch(Exception e){
       		Log.d("OPCIONES ONCREATE", "Excepción: " + e.getMessage());
        }

	}
	
	
	public void MensajeEliminar(){
		Toast.makeText(this,getString(R.string.borrarokk),Toast.LENGTH_SHORT).show();				
	}
	
}