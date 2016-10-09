package bitcare.com.br.bitcare;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementa uma requisição asincrona
 * ao backend para obter um JSON
 * Parametros:
 * 1. Tipo dados de entrada (String URL servico)
 * 2. Tipo dado andamento (Void ignorar)
 * 3. Tipo dado saída (String JSON)
 */
public class RequestTask extends AsyncTask<String,Void,String>{

    private Spinner spLinhas;
    private Activity activity;

    public RequestTask(Spinner spLinhas, Activity activity) {
        this.spLinhas = spLinhas;
        this.activity = activity;
    }

    // Código a ser executado em um Thread separado do Main Thread
    @Override
    protected String doInBackground(String... params) {

        String json = "";

        try {

            URL url = new URL(params[0]);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();

            if (c.getResponseCode() == 200) { //OK!!!!

                // ler o conteúdo da mensagem
                json = Util.toString(c.getInputStream());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }

    // Interpretrar o JSON retornado e popular a Activity
    @Override
    protected void onPostExecute(String json) {

        Log.i("JSON", json);

        try {

            JSONObject obj = new JSONObject(json);

            String hora = obj.getString("hora");

            JSONArray linhas = obj.getJSONArray("linhas");

            List<String> l = new ArrayList<String>();

            for (int i = 0; i < linhas.length(); i++) {

                JSONObject o = linhas.getJSONObject(i);
                l.add(o.getString("linha") + "-" + o.getString("local"));

            }

            ArrayAdapter<String> adp =
                    new ArrayAdapter<String>(this.activity,
            android.R.layout.simple_spinner_item, l);

            this.spLinhas.setAdapter(adp);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
