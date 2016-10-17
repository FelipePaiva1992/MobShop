package br.com.thesource.mobshop.util;

import java.io.IOException;

import android.util.Log;

public class LogUtil {

	public static void printStackTrace(Exception e){
		Log.e("appTeste", e.getMessage() + " -- " + e.getCause() + " -- " + e.getStackTrace());
	}
	
	public static void printStackTrace(IOException e){
		Log.e("appTeste", e.getMessage() + " -- " + e.getCause() + " -- " + e.getStackTrace());
	}

	public static void printInfo(Object msg){
		Log.i("appTeste", "INFO: " + String.valueOf(msg));
	}

	public static void printError(Object msg){
		Log.i("appTeste", "ERROR: " + String.valueOf(msg));
	}
	
}
