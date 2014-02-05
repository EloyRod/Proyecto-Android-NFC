package com.ergonzalez.gestiondecpm;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


@SuppressLint("NewApi")
public class nueva_etiqueta extends Activity {
	NfcAdapter adapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag myTag;
    Context context;
    Bundle b;
    String message;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try{
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.nueva_etiqueta);
			
	        context = this;
	        
	        b = getIntent().getExtras();
	        message = b.getString("grabarTAG");
	        
	        //Los elementos que vamos a usar en el layout
	        Button btnWrite = (Button)findViewById(R.id.btnguardar);
	        //final TextView message = (TextView)findViewById(R.id.);
	        //setOnCLickListener hará la acción que necesitamos
	        btnWrite.setOnClickListener(new OnClickListener(){
	
	        @Override
	        public void onClick(View v) {
	    
			try{
	            //Si no existe tag al que escribir, mostramos un mensaje de que no existe.
	            if(myTag == null){
	            	Toast.makeText(context, context.getString(R.string.error_notag), Toast.LENGTH_LONG).show();
	            }else{
	             //Llamamos al método write que definimos más adelante donde le pasamos por
	             //parámetro el tag que hemos detectado y el mensaje a escribir.
	            	write(message,myTag);
	            	Toast.makeText(context, context.getString(R.string.ok_write), Toast.LENGTH_LONG).show();
	            	lanzarPrincipal(v);
	            }
				}catch(IOException e){
					Toast.makeText(context, context.getString(R.string.error_write),Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}catch(FormatException e){
					Toast.makeText(context, context.getString(R.string.error_write), Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
	        }
	        
	        });
	        
	        adapter = NfcAdapter.getDefaultAdapter(this);
	        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
	        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
	        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
	        writeTagFilters = new IntentFilter[]{tagDetected};
		}catch(Exception e){
	   		Log.d("NUEBA ETIQUETA ONCREATE", "Excepción: " + e.getMessage());
	   	   }
	}//onCreate

	
	
    //El método write es el más importante, será el que se encargue de crear el mensaje 
    //y escribirlo en nuestro tag.
    private void write(String text, Tag tag) throws IOException, FormatException{
    	try{
		      //Creamos un array de elementos NdefRecord. Este Objeto representa un registro del mensaje NDEF   
		      //Para crear el objeto NdefRecord usamos el método createRecord(String s)
		      NdefRecord[] records = {createRecord(text)};
		      //NdefMessage encapsula un mensaje Ndef(NFC Data Exchange Format). Estos mensajes están 
		      //compuestos por varios registros encapsulados por la clase NdefRecord      
		      NdefMessage message = new NdefMessage(records);
		      //Obtenemos una instancia de Ndef del Tag
		      Ndef ndef = Ndef.get(tag);
		      ndef.connect();
		      ndef.writeNdefMessage(message);
		      ndef.close();
    	}catch(Exception e){
	   		Log.d("WRITE", "Excepción: " + e.getMessage());
	   	  }
      
    }//write
    
    //Método createRecord será el que nos codifique el mensaje para crear un NdefRecord
    private NdefRecord createRecord(String text) throws UnsupportedEncodingException{
    	
	        String lang = "us";
	        byte[] textBytes = text.getBytes();
	        byte[] langBytes = lang.getBytes("US-ASCII");
	        int langLength = langBytes.length;
	        int textLength = textBytes.length;
	        byte[] payLoad = new byte[1 + langLength + textLength];
	 
	        payLoad[0] = (byte) langLength;
	 
	        System.arraycopy(langBytes, 0, payLoad, 1, langLength);
	        System.arraycopy(textBytes, 0, payLoad, 1+langLength, textLength);
	 
	        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payLoad);
	 

    	return recordNFC;
    }//createRecord
    
    //en onnewIntent manejamos el intent para encontrar el Tag
    protected void onNewIntent(Intent intent){
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Toast.makeText(this, this.getString(R.string.ok_detected), Toast.LENGTH_LONG).show();
        }
    }//onNewIntent
 
    public void onPause(){
        super.onPause();
        WriteModeOff();
    }//onPause
    public void onResume(){
        super.onResume();
        WriteModeOn();
    }//onResume
 
    private void WriteModeOn(){
        writeMode = true;
        adapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
    }//WritemodeOn
 
    private void WriteModeOff(){
        writeMode = false;
        adapter.disableForegroundDispatch(this);
    }//WriteModeOff
    
    public void lanzarPrincipal(View view){
    	finish();
    }//lanzarPrincipal
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.principal, menu);
		
		super.onCreateOptionsMenu(menu);

	       MenuInflater inflater = getMenuInflater();

	       inflater.inflate(R.menu.menu, menu);

	       return true; /** true -> el menú ya está visible */
		
	}//onCreateOptionsMenu
    
    
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
