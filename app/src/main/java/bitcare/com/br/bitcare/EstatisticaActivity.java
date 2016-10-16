package bitcare.com.br.bitcare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bitcare.com.br.bitcare.entities.MediaEstatistica;
import bitcare.com.br.bitcare.entities.Usuario;
import bitcare.com.br.bitcare.interfaces.EstatisticaEndpointService;
import bitcare.com.br.bitcare.interfaces.LoginEndpointService;
import bitcare.com.br.bitcare.models.LoginRequest;
import bitcare.com.br.bitcare.models.PulsacaoDTO;
import bitcare.com.br.bitcare.utils.ConstantesUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Classe de negocio responsavel pelo login do usuario no sistema
 */
public class EstatisticaActivity extends AppCompatActivity {

    private String login;

    private List<MediaEstatistica> ultimasPulsacoes = new ArrayList<>();

    private Usuario usuario;

    private TextView nrIdade;
    private TextView txtNome;

    private ArrayAdapter<MediaEstatistica> pulsacaoDTOArrayAdapter;

    private EstatisticaEndpointService estatisticaEndpointService = new Retrofit.Builder()
            .baseUrl(ConstantesUtils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EstatisticaEndpointService.class);

    private ListView listaPulsacoes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatistica);

        Bundle bundle = getIntent().getExtras();
        login = bundle.getString("login");

        pulsacaoDTOArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ultimasPulsacoes);
        listaPulsacoes = (ListView) findViewById(R.id.lstEstatisticas);

        nrIdade = (TextView) findViewById(R.id.nrIdade);
        txtNome = (TextView) findViewById(R.id.txtNome);

        buscarDadosUsuario();
        buscarEstatisticas();

    }


    private void buscarEstatisticas() {

        Call<List<MediaEstatistica>> endpointLogin = estatisticaEndpointService.buscarPulsacoes(login, 5);

        endpointLogin.enqueue(new Callback<List<MediaEstatistica>>() {
            @Override
            public void onResponse(Call<List<MediaEstatistica>> call, Response<List<MediaEstatistica>> response) {

                ultimasPulsacoes = response.body();

                pulsacaoDTOArrayAdapter.clear();
                pulsacaoDTOArrayAdapter.addAll(ultimasPulsacoes);
                listaPulsacoes.setAdapter(pulsacaoDTOArrayAdapter);

            }

            @Override
            public void onFailure(Call<List<MediaEstatistica>> call, Throwable t) {
                System.out.println("Erro ao buscar as pulsações");
                ultimasPulsacoes = new ArrayList<>();
            }
        });

    }



    private void buscarDadosUsuario() {

        Call<Usuario> endpointUsuario = estatisticaEndpointService.buscarDadosUsuario(login);

        endpointUsuario.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                usuario = response.body();

                txtNome.setText(usuario.getNome() );
                nrIdade.setText("Idade: " + String.valueOf( usuario.getIdade() ) )  ;
                System.out.println("Busca de usuário feita com sucesso.");

            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                System.out.println("Erro ao buscar os dados do usuário");
            }
        });



    }



    public void mostrarBpm(View view) {
        Intent toBpm = new Intent(this, BpmActivity.class);
        toBpm.putExtra("login",login);
        startActivity(toBpm);
    }


}