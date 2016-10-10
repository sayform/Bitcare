package bitcare.com.br.bitcare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.util.concurrent.ExecutionException;

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
        String senhaText = senha.getText().toString();

        LoginTask loginTask = new LoginTask(this);
        loginTask.execute(urlLogin, loginText, senhaText);

      /*
        Intent toBpmActivity = new Intent(this, BpmActivity.class);
        startActivity(toBpmActivity);
        */

    }



}
