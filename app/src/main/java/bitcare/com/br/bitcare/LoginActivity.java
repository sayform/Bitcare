package bitcare.com.br.bitcare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import bitcare.com.br.bitcare.interfaces.LoginEndpointService;
import bitcare.com.br.bitcare.models.LoginRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Classe de negocio responsavel pelo login do usuario no sistema
 */
public class LoginActivity extends AppCompatActivity {

    private EditText login;
    private EditText senha;
    private TextView txtErroLogin;

    private int httpStatusCode = 0;

    private String urlLogin = "https://bitcare-141317.appspot.com/login/autenticacao";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (EditText) findViewById(R.id.fieldLogin);
        senha = (EditText) findViewById(R.id.fieldPassword);
        txtErroLogin = (TextView) findViewById(R.id.txtErroLogin);

    }

    /**
     * <p>
     * Implementacao do servico de login
     * </p>
     * <p>
     * Utilizado o framework Retrofit, altamente recomendado pela comunidade
     * de desenvolvimento de aplicativos Android.
     * </p>
     * <p>
     * Nao e necessaria a utilizacao de classes AsyncTask, pois
     * a a execucao da chamada dos servicos remotos via enqueue(enfileiramento)
     * nao prende a Thread de interface do usuario
     * </p>
     *
     * @param view
     */
    public void logar(View view) {

        String loginText = login.getText().toString();
        String senhaText = senha.getText().toString();

        final LoginRequest login = new LoginRequest();
        login.setLogin(loginText);
        login.setSenha(senhaText);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantesUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginEndpointService service = retrofit.create(LoginEndpointService.class);
        Call<Void> endpointLogin = service.logar(login);


        endpointLogin.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                httpStatusCode = response.code();
                Log.i("LOGIN_ACTIVITY","onResponse " + httpStatusCode);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("LOGIN_ACTIVITY", "Erro: " + t.getMessage());
            }
        });

        if (httpStatusCode != 200) {
            txtErroLogin.setText("Login inexistente");
        } else {
            Intent toBpmActivity = new Intent(this, BpmActivity.class);
            startActivity(toBpmActivity);
        }

    }

}