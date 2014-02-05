package com.ergonzalez.gestiondecpm;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;



public class leer_borrar extends Activity {
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    private TextView mTextView;
    private NfcAdapter mNfcAdapter;
    public boolean leer = false;
    Bundle b; 
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	try{
	    	requestWindowFeature(Window.FEATURE_NO_TITLE);
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.leer_borrar);
	        
	        
	        
	        b = getIntent().getExtras();
	        leer = b.getBoolean("valorBoolean");
	        
	        //mTextView = (TextView) findViewById(R.id.textGrab);
	        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
	        if (mNfcAdapter == null) {
	            // Stop here, we definitely need NFC
	            Toast.makeText(this, getString(R.string.nonfc), Toast.LENGTH_LONG).show();

	        }
	        if (!mNfcAdapter.isEnabled()) {
	        	  Toast.makeText(this,getString(R.string.offnfc) , Toast.LENGTH_LONG).show();
	        } 
	        
	        handleIntent(getIntent());
    	}catch(Exception e){
	   		Log.d("LEER BORRAR ONCREATE", "Excepción: " + e.getMessage());
	   	}
    }
    @Override
    protected void onResume() {
        super.onResume();
        setupForegroundDispatch(this, mNfcAdapter);
    }
    @Override
    protected void onPause() {
        stopForegroundDispatch(this, mNfcAdapter);
        super.onPause();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }
   

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);
        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};
        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }
        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
        
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }
    
    
    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);
            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();
            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }
    
    
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {
    	@Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];
            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }
            NdefMessage ndefMessage = ndef.getCachedNdefMessage();
            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }
            return null;
        }
        private String readText(NdefRecord record) throws UnsupportedEncodingException {

            byte[] payload = record.getPayload();
            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;
            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"
            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }
        
        @Override
        protected void onPostExecute(String result) {
        	
            if (result != null) {
            	
            	if (leer){
            		lanzartoast(result);
            	}
                
                if (!leer){
                	finish();
                	if(result.length()>0){
		                Intent i = new Intent (leer_borrar.this,Conexiones.class);
		                System.out.println(Integer.parseInt(result));
		
						i.putExtra("IDprincipal",Integer.parseInt(result));
						startActivity(i);
                	}
                }
            }
        }
    }
    
    
    public void lanzartoast(String result){
    	Toast t = Toast.makeText(this,getString(R.string.leotag) + " " +  result,
				Toast.LENGTH_LONG);
				t.show();
    }
   
  
  public void lanzarnuevaetiqueta(View view){
		finish();
		Intent i = new Intent (this,nueva_etiqueta.class);
		i.putExtra("grabarTAG",(""));
		startActivity(i);
	}
  
  public void lanzarVolver(View view){
	  finish();
  }
  
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