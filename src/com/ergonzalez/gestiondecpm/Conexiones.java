package com.ergonzalez.gestiondecpm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Conexiones extends Activity {
	
	Context context;
	String message;
	Bundle b;
	private Notification notificacion;
	private NotificationManager nm;
	private Notification vibrate;
	private static final int ID_NOTIFICACION_CREAR = 1;

	
	protected void onCreate(Bundle savedInstanceState) {
		try{
			
			super.onCreate(savedInstanceState);
			
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); 
			mNotificationManager.cancel(1);
			
			nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
			
			b = getIntent().getExtras();
			System.out.println("En onCreate"+ b.getInt("IDprincipal"));
			MiBaseDatos MBD = new MiBaseDatos(getApplicationContext());
			int num = b.getInt("IDprincipal");
			
			int c = MBD.recuperarCONFIGURACION(num).getC0NEXION();
			int t = MBD.recuperarCONFIGURACION(num).getTIPO();
			
				Ejecutar(c,t);
		
				
				NotificationCompat.Builder mBuilder =
					    new NotificationCompat.Builder(Conexiones.this)
					        .setSmallIcon(R.drawable.ic_launcher)
					        .setLargeIcon((((BitmapDrawable)getResources()
					            .getDrawable(R.drawable.ic_launcher)).getBitmap()))
					        .setContentTitle(MBD.recuperarCONFIGURACION(num).getNOMBRE())
					        .setContentText(MBD.recuperarCONFIGURACION(num).getDESCRIPCION())
					        .setContentInfo(" ")
					        .setTicker(MBD.recuperarCONFIGURACION(num).getNOMBRE());
				
				
				Intent notIntent = new Intent(this, SplashScreenActivity.class); 
				PendingIntent contIntent = PendingIntent.getActivity(this, 0, notIntent, 0); 
				mBuilder.setContentIntent(contIntent);
					
					
				//NotificationManager mNotificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
						 
				mNotificationManager.notify(ID_NOTIFICACION_CREAR, mBuilder.build());
				
				finish();
				
		}catch(Exception e){
			Log.d("CONEXIONES ONCREATE", "Excepción: " + e.getMessage());
			finish();
		}
	}
	


	public void Ejecutar(int conex, int tipo){
		try{
			
			if(conex == '0'){
				Ewifi(tipo);
			}else if (conex == '1'){
				Ebluethood(tipo);
			}else if(conex == '2'){
				Edatos(tipo);
			}else if(conex == '3'){
				System.out.println(tipo + " es el tipo modo avion");
				Emodoavion(tipo);
				
			}
		}catch(Exception e){
			Log.d("EJECUTAR CONFIGURACION EJECUTAR", "Excepción: " + e.getMessage());
		}
	}//Ejecutar


	public void Ewifi(int tipo){
		try{
			
			String service = Context.WIFI_SERVICE;
			WifiManager wifi = (WifiManager) getSystemService(service);
			
			if (tipo =='0'){
				if(!wifi.isWifiEnabled()){
					wifi.setWifiEnabled(true);
					System.out.println("ESto es WIFI en tipo 0 " );
				}
			}else if(tipo =='1'){
				if (wifi.isWifiEnabled()){
					wifi.setWifiEnabled(false);
					System.out.println("ESto es WIFI en tipo 1 " );
				}
			}else if(tipo =='2'){
				System.out.println("ESto es WIFI en tipo 2 " );
				if(!wifi.isWifiEnabled()){
					wifi.setWifiEnabled(true);
				}else if (wifi.isWifiEnabled()){
					wifi.setWifiEnabled(false);
				}
			}
		}catch(Exception e){
		Log.d("EJECUTAR CONFIGURACION Ewifi", "Excepción: " + e.getMessage());
		}
	}//Ewifi

	public void Ebluethood(int tipo){
	try{
		System.out.println("ESto es Bluettoth: " + tipo);
		BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
		
		if (tipo =='0'){
			if (!bluetooth.isEnabled()){			
				bluetooth.enable();
			}
		}else if(tipo =='1'){
			if (bluetooth.isEnabled()){
				bluetooth.disable();
			}
		}else if(tipo =='2'){
		
			if (!bluetooth.isEnabled()){			
				bluetooth.enable();
			}else if (bluetooth.isEnabled()){
				bluetooth.disable();
			}
		}
		}catch(Exception e){
		Log.d("EJECUTAR CONFIGURACION Ebluethood", "Excepción: " + e.getMessage());
		}
	}//Ebluethood


	private void setMobileDataEnabled(boolean enabled) {
		try{
			   final ConnectivityManager conman = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			   final Class conmanClass = Class.forName(conman.getClass().getName());
			   final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
			   
			   
			   iConnectivityManagerField.setAccessible(true);
			   final Object iConnectivityManager = iConnectivityManagerField.get(conman);
			   final Class iConnectivityManagerClass =  Class.forName(iConnectivityManager.getClass().getName());
			   final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
			   setMobileDataEnabledMethod.setAccessible(true);

			   setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
		}catch(Exception e){
			Log.d("EJECUTAR setMibleDataEnabled", "Excepción: " + e.getMessage());
			}
		   
		}



	public void Edatos(int tipo){
		try{
			
			boolean isEnabled;
			
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		
			
		    if(telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED){
		        isEnabled = true;
		    }else{
		        isEnabled = false; 
		    }   
		   
		if (tipo =='0'){
			setMobileDataEnabled(true);
		}else if(tipo =='1'){
			setMobileDataEnabled(false);
		}else if(tipo =='2'){
		    	
		    if (isEnabled) {
		    	setMobileDataEnabled(false);
		    } else {
		    	setMobileDataEnabled(true);
		    }
		}
		}catch(Exception e){
		Log.d("EJECUTAR CONFIGURACION Edatos", "Excepción: " + e.getMessage());
		}
		

	}//Edatos




	public void Emodoavion(int tipo){
		try{
			System.out.println("Esto es avion");
			boolean state = isAirplaneMode();
			
			
			System.out.println(state + " " + tipo + " " + '2');
			if (tipo =='0'){
				if(!state) toggleAirplaneMode(1,state);
			}else if(tipo =='1'){
					if(state)	toggleAirplaneMode(0,state);
			}else if(tipo =='2'){
		
			if(state){toggleAirplaneMode(0,state);
				System.out.println("Modo avion esta ONN");}
			else {toggleAirplaneMode(1,state);
			System.out.println("Modo avion esta OFFF");}
		}
		}catch(Exception e){
		Log.d("EJECUTAR CONFIGURACION Emodoavion", "Excepción: " + e.getMessage());
		}
	}//Emodoavion


	@SuppressLint("NewApi")
	public void toggleAirplaneMode(int value, boolean state) {

		try{
		
			System.out.println("en ToggleAir");
			if (Build.VERSION.SDK_INT < 
					Build.VERSION_CODES.JELLY_BEAN_MR1) { //if menor verson 4.2
				Settings.System.putInt(
						getContentResolver(),
						Settings.System.AIRPLANE_MODE_ON, value);
				System.out.println("Menor que 4.2");
			} else {
				Settings.Global.putInt(
						getContentResolver(),
						Settings.Global.AIRPLANE_MODE_ON, value);
				System.out.println("Mayor que 4.2");
			}
			
			Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
			intent.putExtra("state", !state);
			System.out.println(state);
			sendBroadcast(intent);
		}catch(Exception e){
		Log.d("EJECUTAR CONFIGURACION Emodoavion", "Excepción: " + e.getMessage());
		}

	}//toggleAirplaneMode




	@SuppressLint("NewApi")
	public boolean isAirplaneMode() {
	  //Comprobar la version
		System.out.println("IsAirplaneeeee");
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {//si es inferior a 4.2
			return Settings.System.getInt(getContentResolver(),
					Settings.System.AIRPLANE_MODE_ON, 0) != 0; 
			
		} else {
			return Settings.Global.getInt(getContentResolver(),
					Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
					
		}

	}//isAirplaneMode
}
