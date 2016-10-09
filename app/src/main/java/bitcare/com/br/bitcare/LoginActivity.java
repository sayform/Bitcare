package bitcare.com.br.bitcare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    public void toBpm(View view) {
        Intent toBpmActivity = new Intent(this, BpmActivity.class);
        startActivity(toBpmActivity);
    }


    public void logar() {

        try {

            URL url = new URL("https://bitcare-141317.appspot.com/login/autenticacao");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }






}
