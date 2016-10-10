package bitcare.com.br.bitcare;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Felipe on 08/10/2016.
 */

public class LoginTask extends AsyncTask<String, Void, String> {

    private Activity activity;

    /**
     * Construtor padrao da classe
     *
     * @param activity
     */
    public LoginTask(Activity activity) {
        this.activity = activity;
    }



    /**
     *
     * @param params
     * @return
     */
    @Override
    protected String doInBackground(String... params) {

        String urlServico = params[0];
        String login = params[1];
        String senha = params[2];

        try {

            URL url = new URL(urlServico);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");


            //Define que os dados serao enviados no corpo da mensagem (padrao post)
            conn.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());

            JSONObject dadosLogin = new JSONObject();
            dadosLogin.put("login",login);
            dadosLogin.put("senha",senha);

            out.writeBytes(dadosLogin.toString());

            Log.i("RESPONSE", String.valueOf(conn.getResponseCode()));

            if (conn.getResponseCode() != 200) {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "ok";

    }

    @Override
    protected void onPostExecute(String s) {

        if (s != null) {
            Intent toBpmActivity = new Intent(activity, BpmActivity.class);
            activity.startActivity(toBpmActivity);
        } else {
            Toast.makeText(activity, "Login incorreto", Toast.LENGTH_LONG).show();
        }



    }
}
