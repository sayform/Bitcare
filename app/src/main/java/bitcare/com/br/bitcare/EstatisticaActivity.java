package bitcare.com.br.bitcare;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import bitcare.com.br.bitcare.entities.MediaEstatistica;
import bitcare.com.br.bitcare.entities.Usuario;
import bitcare.com.br.bitcare.interfaces.EstatisticaEndpointService;
import bitcare.com.br.bitcare.interfaces.LoginEndpointService;
import bitcare.com.br.bitcare.models.CloudantViewContainerDTO;
import bitcare.com.br.bitcare.models.LoginRequest;
import bitcare.com.br.bitcare.models.PulsacaoDTO;
import bitcare.com.br.bitcare.utils.ConstantesUtils;
import bitcare.com.br.bitcare.utils.DateTimeUtils;
import bitcare.com.br.bitcare.utils.RetrofitGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Classe de negocio responsavel pelo login do usuario no sistema
 */
public class EstatisticaActivity extends AppCompatActivity {

    private static final int QTD_ESTATISTICA = 24;
    private String login;

    private List<MediaEstatistica> ultimasPulsacoes = new ArrayList<>();

    private Usuario usuario;

    private TextView nrIdade;
    private TextView txtNome;

    private TableLayout tblPulsacoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatistica);

        Bundle bundle = getIntent().getExtras();
        login = bundle.getString("login");

        tblPulsacoes = (TableLayout) findViewById(R.id.tblEstatistica);

        nrIdade = (TextView) findViewById(R.id.nrIdade);
        txtNome = (TextView) findViewById(R.id.txtNome);

        buscarDadosUsuario();
        buscarEstatisticas();

    }


    private void buscarEstatisticas() {
        montaHeaderTabela();

        DateTime horario = DateTime.now();
        String loginKey = "\"" + login + "\"";

        for(int i = 0; i <= QTD_ESTATISTICA; i++) {
            final DateTime horaInicio = horario.minusHours(i)
                                                .withMinuteOfHour(0)
                                                .withSecondOfMinute(0)
                                                .withMillisOfSecond(0);
            final DateTime horaFim = horaInicio.plusHours(1);

            String startKey = "[" + loginKey + ", \"" + DateTimeUtils.toString(horaInicio) + "\"]";
            String endKey = "[" + loginKey + ", \"" + DateTimeUtils.toString(horaFim) + "\"]";

            EstatisticaEndpointService service = RetrofitGenerator.createService(EstatisticaEndpointService.class);
            Call<CloudantViewContainerDTO<Long>> endpointLogin = service.buscarPulsacoes(startKey, endKey);

            endpointLogin.enqueue(new Callback<CloudantViewContainerDTO<Long>>() {
                @Override
                public void onResponse(Call<CloudantViewContainerDTO<Long>> call, Response<CloudantViewContainerDTO<Long>> response) {

                    CloudantViewContainerDTO<Long> body = response.body();
                    if(body == null || body.getRows() == null || body.getRows().isEmpty()) {
                        return;
                    }

                    Long valor = body.getRows().get(0).getValue();

                    TableRow row = new TableRow(EstatisticaActivity.this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);

                    TextView txtBpm = new AppCompatTextView(EstatisticaActivity.this);
                    txtBpm.setPadding(26, 26, 26, 26);
                    txtBpm.setTextSize(19.0f);
                    txtBpm.setTextColor(Color.DKGRAY);
                    txtBpm.setText(String.format(Locale.US, "%d BPM", valor));

                    TextView secondsAgo = new AppCompatTextView(EstatisticaActivity.this);
                    secondsAgo.setPadding(26, 26, 26, 26);
                    secondsAgo.setTextSize(19.0f);
                    secondsAgo.setTextColor(Color.GRAY);
                    secondsAgo.setGravity(Gravity.END);

                    String horaFormatada = horaInicio.getHourOfDay() + "h - " + horaFim.getHourOfDay() + "h";
                    secondsAgo.setText(horaFormatada);

                    row.addView(txtBpm);
                    row.addView(secondsAgo);

                    tblPulsacoes.addView(row);

                }

                @Override
                public void onFailure(Call<CloudantViewContainerDTO<Long>> call, Throwable t) {
                    System.out.println("Erro ao buscar as pulsações");
                }
            });
        }

    }

    private void montaHeaderTabela() {
        tblPulsacoes.removeAllViews();

        TableRow headerRow = new TableRow(EstatisticaActivity.this);
        headerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));

        TextView headerBpm = new AppCompatTextView(EstatisticaActivity.this);
        headerBpm.setPadding(26, 26, 26, 26);
        headerBpm.setTextSize(19.0f);
        headerBpm.setTextColor(Color.BLACK);
        headerBpm.setTypeface(null, Typeface.BOLD);
        headerBpm.setText("Média");

        TextView headerIntervalo = new AppCompatTextView(EstatisticaActivity.this);
        headerIntervalo.setPadding(26, 26, 26, 26);
        headerIntervalo.setTextSize(19.0f);
        headerIntervalo.setTextColor(Color.BLACK);
        headerIntervalo.setTypeface(null, Typeface.BOLD);
        headerIntervalo.setGravity(Gravity.END);
        headerIntervalo.setText("Intervalo");

        headerRow.addView(headerBpm);
        headerRow.addView(headerIntervalo);

        tblPulsacoes.addView(headerRow);
    }

    private void buscarDadosUsuario() {

        EstatisticaEndpointService service = RetrofitGenerator.createService(EstatisticaEndpointService.class);
        Call<CloudantViewContainerDTO<Usuario>> endpointUsuario = service.buscarDadosUsuario("\"" + login + "\"");

        endpointUsuario.enqueue(new Callback<CloudantViewContainerDTO<Usuario>>() {
            @Override
            public void onResponse(Call<CloudantViewContainerDTO<Usuario>> call, Response<CloudantViewContainerDTO<Usuario>> response) {
                CloudantViewContainerDTO<Usuario> body = response.body();
                if(body == null || body.getRows() == null || body.getRows().isEmpty()) {
                    txtNome.setText("Usuário não encontrado");
                    nrIdade.setText("Idade: ")  ;
                    return;
                }

                usuario = body.getRows().get(0).getValue();

                txtNome.setText(usuario.getNome() );
                nrIdade.setText("Idade: " + String.valueOf( usuario.getIdade() ) )  ;
                System.out.println("Busca de usuário feita com sucesso.");

            }

            @Override
            public void onFailure(Call<CloudantViewContainerDTO<Usuario>> call, Throwable t) {
                System.out.println("Erro ao buscar os dados do usuário");
            }
        });



    }



    public void mostrarBpm(View view) {
        finish();
    }


}