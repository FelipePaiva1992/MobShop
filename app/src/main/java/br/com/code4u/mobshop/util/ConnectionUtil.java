package br.com.code4u.mobshop.util;

import android.app.Activity;
import android.net.ConnectivityManager;

import java.net.ConnectException;

/**
 * Created by felipepaiva on 25/01/15.
 */
public class ConnectionUtil {

    /* Função para verificar existência de conexão com a internet  */
    public static void verificaConexao(Activity activity) {
        /*ConnectivityManager conectivtyManager = (ConnectivityManager) activity.getSystemService(activity.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
        } else {
            fechaAplicacao(activity);
        }*/
    }

        private static void fechaAplicacao(final Activity activity){

        /*AlertDialog.Builder builder = new AlertDialog.Builder(activity);
         builder.setTitle("Sem Conexão!")
                .setMessage("Seu dispositivo não está conectado a internet, favor conecte-o e retorne a aplicação!")
                .setCancelable(false)
                .setPositiveButton("Entendi", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       if(activity.getClass().getSimpleName().toUpperCase().equals("INSERCAOCODVENDEDOR")) {
                           activity.finish();
                           System.exit(0);
                       }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();*/

    }

    public static void verificaConexaoWebService(Activity activity) throws ConnectException {
        ConnectivityManager conectivtyManager = (ConnectivityManager) activity.getSystemService(activity.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
        } else {
            throw new ConnectException("falha conexão webservice");
        }
    }
}
