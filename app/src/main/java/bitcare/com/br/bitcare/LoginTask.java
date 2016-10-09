package bitcare.com.br.bitcare;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Spinner;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import bitcare.com.br.bitcare.teste.sptrans.Util;

/**
 * Created by Felipe on 08/10/2016.
 */

public class LoginTask extends AsyncTask<String, Void, String> {

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
                throw new RuntimeException("Erro ao fazer login");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "ok";

    }

}
