package com.ergonzalez.gestiondecpm;



import java.util.ArrayList;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;



public class Principal extends Activity {
	private ListView lista; 
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	try{
	    	
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_principal);
	        

	        
	        MiBaseDatos MBD = new MiBaseDatos(getApplicationContext());
	        ArrayList<Lista_entrada> datos = new ArrayList<Lista_entrada>();      
	        
	        
	        if (MBD.recuperarCONFIGURACIONES().size() > 0){
		        for (int i = 0; i < MBD.recuperarCONFIGURACIONES().size(); i++) {
		        	int img = MBD.recuperarCONFIGURACIONES().get(i).getC0NEXION();
		        	int foto = R.drawable.wifi2;
		        	
		        	if(img == '0'){
		        		foto = R.drawable.wifi2;
		        	}else if (img == '1'){
		        		foto = R.drawable.blue_tooth;
		        	}else if (img == '2'){
		        		foto = R.drawable.datos;
		        	}else if(img == '3'){
		        		foto = R.drawable.modo_avion;
		        	}
		        	
		            datos.add(new Lista_entrada(foto,MBD.recuperarCONFIGURACIONES().get(i).getNOMBRE(), MBD.recuperarCONFIGURACIONES().get(i).getDESCRIPCION()));
		            
		        }
	        }
	        
	        TextView nom = (TextView) findViewById(R.id.empty);
	        if (MBD.recuperarCONFIGURACIONES().size() > 0){      	
	        	nom.setText("");
	        }else{
	        	nom.setText(R.string.NoConfig);
	        }
    	 
        
       
        
	        lista = (ListView) findViewById(R.id.ListView_listado);
	        lista.setAdapter(new Lista_adaptador(this, R.layout.elemento_lista, datos){
				@Override
				public void onEntrada(Object entrada, View view) {
			        if (entrada != null) {
			            TextView texto_superior_entrada = (TextView) view.findViewById(R.id.textView_superior); 
			            if (texto_superior_entrada != null) 
			            	texto_superior_entrada.setText(((Lista_entrada) entrada).get_textoEncima()); 
	
			            TextView texto_inferior_entrada = (TextView) view.findViewById(R.id.textView_inferior); 
			            if (texto_inferior_entrada != null)
			            	texto_inferior_entrada.setText(((Lista_entrada) entrada).get_textoDebajo()); 
	
			            ImageView imagen_entrada = (ImageView) view.findViewById(R.id.imageView_imagen); 
			            if (imagen_entrada != null)
			            	imagen_entrada.setImageResource(((Lista_entrada) entrada).get_idImagen());
			        }
				}
			});
	
	        lista.setOnItemClickListener(new OnItemClickListener() { 
				@Override
				public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
					Lista_entrada elegido = (Lista_entrada) pariente.getItemAtPosition(posicion); 
	
					//finish();
					Intent i = new Intent (Principal.this,nueva_config.class);
					i.putExtra("IDprincipal",(recuperaID(elegido.get_textoEncima())));
					i.putExtra("valorBoolean", true);
					startActivity(i);
				}//onItemClick
	        });//setOnItemClickListener
    	}catch(Exception e){
	   		Log.d("PRINCIPAL ONCREATE", "Excepción: " + e.getMessage());
	   	   }
       try{
	        lista.setOnItemLongClickListener(new OnItemLongClickListener(){
	        	
	        	@Override
				public boolean onItemLongClick(AdapterView<?> pariente, View view, int posicion, long id) {
					Lista_entrada elegido = (Lista_entrada) pariente.getItemAtPosition(posicion); 
	
					System.out.println("Dentro de setOnItemLongClickListener" + elegido.get_textoEncima());
					
					Intent i = new Intent (Principal.this,Opciones.class);
					i.putExtra("IDprincipal",(recuperaID(elegido.get_textoEncima())));
					startActivity(i);

					return false;
				}//onItemClick
	        	
	        	
	        });
       }catch(Exception e){
   		Log.d("PRINCIPAL ONCREATE", "Excepción: " + e.getMessage());
   	   }
   

    }//onCreate


		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			//getMenuInflater().inflate(R.menu.principal, menu);
			
			super.onCreateOptionsMenu(menu);

		       MenuInflater inflater = getMenuInflater();

		       inflater.inflate(R.menu.menu, menu);

		       return true; /** true -> el menú ya está visible */
			
		}//onCreateOptionsMenu
		
		
		public int recuperaID(String nom){
			int codigo = 0;
			
			try{
				MiBaseDatos MBD = new MiBaseDatos(getApplicationContext());
				
				for(int i = 0; i < MBD.recuperarCONFIGURACIONES().size(); i++){
					if (MBD.recuperarCONFIGURACIONES().get(i).getNOMBRE().equalsIgnoreCase(nom) == true){
						 codigo  = MBD.recuperarCONFIGURACIONES().get(i).getID();
					}
				}
				
				
			}catch(Exception e){
	    		Log.d("NUEVA CONFIGURACION RECUPERARID", "Excepción: " + e.getMessage());
	    	}
			return codigo;
		}//comprobarID
		

		public void lanzarNuevaconf(View view){
			
			
			Intent i = new Intent (this,nueva_config.class);
			i.putExtra("valorBoolean", false);
			startActivity(i);
		}//lanzarNuevaconf
		
	
		public void lanzarTag(View view){
			
			Intent i = new Intent (this,leer_borrar.class);
			i.putExtra("valorBoolean", true);
			startActivity(i);
		}//lanzarTag

		
		  public void onResume(){
		        super.onResume();
		        System.out.println("En on REsume");
		        onCreate(null);
		  }//onResume
		  
		  public void onPause(){
		        super.onPause();
		        onCreate(null);
		  }//onResume
		  
		  
		 

		   

		    @Override public boolean onOptionsItemSelected(MenuItem item) {

		             switch (item.getItemId()) {

		             case R.id.acercaDe:
		            	 Intent i = new Intent (this,AcercaDe.class);
		     			 i.putExtra("valorBoolean", true);
		     			 startActivity(i);
		                 break;
		             case R.id.config:
		            	 //lanzarConfiguracion(null);
		            	 break;

		             }

		             return true; /** true -> consumimos el item, no se propaga*/

		    }
		  
		  
		  

    }
	
	
	
