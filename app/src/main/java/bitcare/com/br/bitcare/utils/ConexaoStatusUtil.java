package bitcare.com.br.bitcare.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Rafael on 09/10/2016.
 */

public abstract class ConexaoStatusUtil {

    public static boolean isConnected(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
