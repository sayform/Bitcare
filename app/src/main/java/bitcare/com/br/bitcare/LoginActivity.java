package bitcare.com.br.bitcare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.R.attr.data;

public class LoginActivity extends AppCompatActivity {

    private EditText login;
    private EditText senha;

    private String urlLogin = "https://bitcare-141317.appspot.com/login/autenticacao";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (EditText) findViewById(R.id.fieldLogin);
        senha = (EditText) findViewById(R.id.fieldPassword);

    }

    public void logar(View view) {

        String loginText = login.getText().toString();
        String senhaText = login.getText().toString();

        String sucessoChamada = "";

        LoginTask loginTask = new LoginTask(sucessoChamada, this);
        loginTask.execute(urlLogin, loginText, senhaText, sucessoChamada);


        if (sucessoChamada != null) {
            Intent toBpmActivity = new Intent(this, BpmActivity.class);
            startActivity(toBpmActivity);
        } else {
            Toast.makeText(this, "Erro ao logar no sistema",Toast.LENGTH_LONG).show();
        }


    }



 /*   public void logar() {

        try {

            URL url = new URL("https://bitcare-141317.appspot.com/login/autenticacao");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            //Define que os dados serao enviados no corpo da mensagem (padrao post)
            conn.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());

            JSONObject dadosLogin = new JSONObject();
            dadosLogin.put("login",login.getText());
            dadosLogin.put("senha",senha.getText());

            out.writeBytes(dadosLogin.toString());

            int statusResponse =  conn.getResponseCode();

            if (statusResponse != 200) {
                Toast.makeText(this, "Erro ao logar no sistema",Toast.LENGTH_LONG).show();
                out.flush();
                out.close();
                throw new RuntimeException("Erro de login");
            }

            out.flush();
            out.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }*/






}
