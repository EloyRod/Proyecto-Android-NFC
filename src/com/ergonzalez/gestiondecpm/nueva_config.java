package com.ergonzalez.gestiondecpm;

import java.lang.reflect.Method;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class nueva_config extends Activity {
	
	private boolean modifica = false;
	private int conexion;
	private int tipo;
	private int ID;
	Bundle b;
	int IDNC;
	boolean Ddonde;
	
	Method dataConnSwitchmethod;
    Object ITelephonyStub;
    Context context;

	RadioButton ad;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try{

			super.onCreate(savedInstanceState);
			setContentView(R.layout.nueva_config);
			
			
	       	
			EditText nom = (EditText) findViewById(R.id.nombreConfig);
			
			RadioButton ad = (RadioButton) findViewById(R.id.modoavion);
			ad.setOnClickListener(myOptionOnClickListener);
			
			 b = getIntent().getExtras();
			 
		     IDNC = b.getInt("IDprincipal");
		     Ddonde = b.getBoolean("valorBoolean");


		     if (Ddonde == true){
		    	 modifica = true;
		    	 rellenaCAMPOS(IDNC);
		     }else{
				ID = comprobarID();
				nom.setText(getString(R.string.config)+ " " + ID);
		     }
 
		}catch(Exception e){
    		Log.d("NUEVA CONFIGURACION ONCREATE", "Excepción: " + e.getMessage());
    	}
	}//onCreate	
	
	
	public void nuevaConfiguracion(View view){

		try{
			MiBaseDatos MBD = new MiBaseDatos(getApplicationContext());
			
			EditText nom = (EditText) findViewById(R.id.nombreConfig);
			EditText des = (EditText) findViewById(R.id.descripcionConfig);
			
			RadioButton wi = (RadioButton) findViewById(R.id.wifi);
			RadioButton bl = (RadioButton) findViewById(R.id.bluethood);
			RadioButton da = (RadioButton) findViewById(R.id.datos);
			RadioButton mo = (RadioButton) findViewById(R.id.modoavion);
			
			RadioButton ac = (RadioButton) findViewById(R.id.activar);
			RadioButton de = (RadioButton) findViewById(R.id.desactivar);
			RadioButton co = (RadioButton) findViewById(R.id.conmutar);
					
			
			if(wi.isChecked()){
				this.conexion = '0';
			}else if(bl.isChecked()){
				this.conexion = '1';
			}else if(da.isChecked()){
				this.conexion = '2';
			}else if(mo.isChecked()){
				this.conexion = '3';
			}
			
			if(ac.isChecked()){
				this.tipo = '0';
			}else if(de.isChecked()){
				this.tipo = '1';
			}else if(co.isChecked()){
				this.tipo = '2';
			}
			
		
			if (comprobarNOMBRE(nom.getText().toString()) == true){
			    if (nom.getText().toString().length()>0){
			    	
			    	if(modifica == false){
			    		System.out.println("Tipo: " + tipo + " Conexion: " + conexion);
			    		MBD.insertarCONFIGUARCION(ID,nom.getText().toString(),des.getText().toString(),conexion,tipo);		
						Toast.makeText(this,getString(R.string.guardokk),Toast.LENGTH_SHORT).show();
						lanzarnuevaetiqueta(view,nom.getText().toString());
			    	}else{
			    		MBD.modificarCONFIGURACION(MBD.recuperarCONFIGURACION(IDNC).getID(),nom.getText().toString(),des.getText().toString(),conexion,tipo);
						Toast.makeText(this,getString(R.string.editokk),Toast.LENGTH_SHORT).show();
						lanzarnuevaetiqueta(view,nom.getText().toString());
			    	}
			    }else{
			    	Toast.makeText(this,getString(R.string.nomvacio) ,Toast.LENGTH_SHORT).show();
					
			    }
			}else{
				Toast.makeText(this,getString(R.string.nomexis),Toast.LENGTH_SHORT).show();
			}
			
		}catch(Exception e){
    		Log.d("NUEVA CONFIGURACION NCONFIG", "Excepción: " + e.getMessage());
    	}
	}//nuevaConfiguracion
	
	
	public void rellenaCAMPOS(int ids){
		
		try{


			MiBaseDatos MBD = new MiBaseDatos(getApplicationContext());
			
			EditText nom = (EditText) findViewById(R.id.nombreConfig);
			EditText des = (EditText) findViewById(R.id.descripcionConfig);
			
			RadioButton wi = (RadioButton) findViewById(R.id.wifi);
			RadioButton bl = (RadioButton) findViewById(R.id.bluethood);
			RadioButton da = (RadioButton) findViewById(R.id.datos);
			RadioButton mo = (RadioButton) findViewById(R.id.modoavion);
			
			RadioButton ac = (RadioButton) findViewById(R.id.activar);
			RadioButton de = (RadioButton) findViewById(R.id.desactivar);
			RadioButton co = (RadioButton) findViewById(R.id.conmutar);
			
			nom.setText(MBD.recuperarCONFIGURACION(ids).getNOMBRE());
			des.setText(MBD.recuperarCONFIGURACION(ids).getDESCRIPCION());
			
			
			conexion = MBD.recuperarCONFIGURACION(ids).getC0NEXION();
			tipo = MBD.recuperarCONFIGURACION(ids).getTIPO();
			
			if(conexion == '0'){
				wi.setChecked(true);
			}else if (conexion =='1'){
				bl.setChecked(true);
			}else if(conexion == '2'){
				da.setChecked(true);
			}else if(conexion == '3'){
				mo.setChecked(true);
			}
			
			
			if (tipo == '0'){
				ac.setChecked(true);
			}else if(tipo == '1'){
				de.setChecked(true);
			}else if(tipo == '2'){
				co.setChecked(true);
			}
			
		}catch(Exception e){
			Log.d("NUEVA CONFIGURACION rellenaCAMPOS", "Excepción: " + e.getMessage());
		}	
	}//rellenaCAMPOS

	
	public int comprobarID(){
		int x = 0;
		MiBaseDatos MBD = new MiBaseDatos(getApplicationContext());

		try{
			
			for(int i = 0; i < MBD.recuperarCONFIGURACIONES().size(); i++){
				x = MBD.recuperarCONFIGURACIONES().get(i).getID();
			}
			x++;
		}catch(Exception e){
    		Log.d("NUEVA CONFIGURACION COMPROBARID", "Excepción: " + e.getMessage());
    	}
		return x;
	}//comprobarID

	
	
	public boolean comprobarNOMBRE(String nombreC){
		try{
			MiBaseDatos MBD = new MiBaseDatos(getApplicationContext());
			for(int i = 0; i < MBD.recuperarCONFIGURACIONES().size(); i++){
				if (MBD.recuperarCONFIGURACIONES().get(i).getNOMBRE().equalsIgnoreCase(nombreC) == true){
					return false;
				}
			}
		}catch(Exception e){
    		Log.d("NUEVA CONFIGURACION COMPROBARNOMBRE", "Excepción: " + e.getMessage());
    	}
		return true;
	}//comprobarNOMBRE

	
	public void lanzarnuevaetiqueta(View view,String nom){

		finish();
		Intent i = new Intent (this,nueva_etiqueta.class);
		i.putExtra("grabarTAG",(Integer.toString(recuperaID(nom))));
		startActivity(i);
		
	}//lanzarnuevaetiqueta


	public void volverPrincipal(View view){
		finish();
	}//volverPrincipal
	
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
	
	
	
	RadioButton.OnClickListener myOptionOnClickListener =
			   new RadioButton.OnClickListener()
			  {
		
			@Override
			  public void onClick(View v) {
				
				try{
			   // TODO Auto-generated method stub
						  
					RadioButton rar = (RadioButton) findViewById(R.id.modoavion);
				   if (rar.isChecked()){
					   AlertDialog.Builder popup=new AlertDialog.Builder(nueva_config.this);
					   popup.setTitle(getString(R.string.importan));
					   popup.setMessage(getString(R.string.resavion));
					   popup.setPositiveButton("Ok", null);
					   popup.show();
				   }
				}catch(Exception e){
		    		Log.d("Radioburron onlclicklistener", "Excepción: " + e.getMessage());
		    	}
			  }
				

			  };//Escucho el RadioButton
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		super.onCreateOptionsMenu(menu);

	       MenuInflater inflater = getMenuInflater();

	       inflater.inflate(R.menu.menu, menu);

	       return true; /** true -> el menú ya está visible */
		
	}//onCreateOptionsMenu
	
	@Override public boolean onOptionsItemSelected(MenuItem item) {
		try{
	        switch (item.getItemId()) {
	
		        case R.id.acercaDe:
		       	 	Intent i = new Intent (this,AcercaDe.class);
		       	 	startActivity(i);
		            break;
		        case R.id.config:
		        	
		        	
		       	 break;
	
	        }
        }catch(Exception e){
    		Log.d("Menu....", "Excepción: " + e.getMessage());
    	}

        return true; /** true -> consumimos el item, no se propaga*/

}


}
