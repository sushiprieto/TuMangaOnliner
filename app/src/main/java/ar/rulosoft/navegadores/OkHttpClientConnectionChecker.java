package ar.rulosoft.navegadores;

import android.util.Log;

import ar.rulosoft.mimanganu.MainActivity;
import ar.rulosoft.mimanganu.utils.NetworkUtilsAndReceiver;
import okhttp3.OkHttpClient;

/**
 * Created by Raul on 14/06/2016.
 */
public class OkHttpClientConnectionChecker extends OkHttpClient {
    public OkHttpClientConnectionChecker() throws Exception {
        super();
        if (!MainActivity.isConnected) {
            if (NetworkUtilsAndReceiver.ONLY_WIFI) {
                Log.e("OkHttpClientConnectionC","no Wifi Exception");
                throw new Exception();
                //throw new NoWifiException(context);
            } else {
                Log.e("OkHttpClientConnectionC","no Connection Exception");
                throw new Exception();
                //throw new NoConnectionException(context);
            }
        }
    }
}
