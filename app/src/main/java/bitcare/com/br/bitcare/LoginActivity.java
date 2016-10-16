package bitcare.com.br.bitcare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;

import bitcare.com.br.bitcare.interfaces.LoginEndpointService;
import bitcare.com.br.bitcare.models.CloudantViewContainerDTO;
import bitcare.com.br.bitcare.models.LoginRequest;
import bitcare.com.br.bitcare.models.LoginResponse;
import bitcare.com.br.bitcare.utils.ConstantesUtils;
import bitcare.com.br.bitcare.utils.RetrofitGenerator;
import okhttp3.OkHttpClient;
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

    private ProgressBar progressBar;

    private int httpStatusCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (EditText) findViewById(R.id.fieldLogin);
        senha = (EditText) findViewById(R.id.fieldPassword);
        txtErroLogin = (TextView) findViewById(R.id.txtErroLogin);
        progressBar = (ProgressBar) findViewById(R.id.pbLogin);

        progressBar.setVisibility(View.GONE);

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

        progressBar.setVisibility(View.VISIBLE);

        String loginText = login.getText().toString();
        String senhaText = senha.getText().toString();

        final LoginRequest login = new LoginRequest();
        login.setLogin(loginText);
        login.setSenha(senhaText);

        LoginEndpointService service = RetrofitGenerator.createService(LoginEndpointService.class);

        String loginKey = String.format("[\"%s\",\"%s\"]", login.getLogin(), login.getSenha());
        Call<CloudantViewContainerDTO<LoginResponse>> endpointLogin = service.logar(loginKey);

        endpointLogin.enqueue(new Callback<CloudantViewContainerDTO<LoginResponse>>() {
            @Override
            public void onResponse(Call<CloudantViewContainerDTO<LoginResponse>> call,
                                   Response<CloudantViewContainerDTO<LoginResponse>> response) {

                httpStatusCode = response.code();
                Log.i("LOGIN_ACTIVITY","onResponse " + httpStatusCode);

                progressBar.setVisibility(View.GONE);

                if (httpStatusCode == 200) {
                    chamarBpmActivity(response.body().getRows().get(0).getValue().getLoginId());
                } else {
                    txtErroLogin.setText("Login inexistente.");
                }
            }

            @Override
            public void onFailure(Call<CloudantViewContainerDTO<LoginResponse>> call, Throwable t) {
                Log.e("LOGIN_ACTIVITY", "Erro: " + t.getMessage());
                progressBar.setVisibility(View.GONE);
                txtErroLogin.setText("Ocorreu um erro no sistema. Tente novamente.");
            }
        });



    }

    /**
     * Metodo privado que navega para a Activity de BPM
     */
    private void chamarBpmActivity(String loginId) {
        Intent toBpmActivity = new Intent(this, BpmActivity.class);
        toBpmActivity.putExtra("login",loginId);
        startActivity(toBpmActivity);
    }



}