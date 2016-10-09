package bitcare.com.br.bitcare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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




    }






}
