package bitcare.com.br.bitcare;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.text.format.DateUtils;
import android.util.Size;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import net.danlew.android.joda.JodaTimeAndroid;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import bitcare.com.br.bitcare.entities.Pulsacao;
import bitcare.com.br.bitcare.interfaces.PulsacaoEndpointService;
import bitcare.com.br.bitcare.models.PulsacaoDTO;
import bitcare.com.br.bitcare.utils.ConexaoStatusUtil;
import bitcare.com.br.bitcare.utils.ConstantesUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BpmActivity extends AppCompatActivity {

    public static final String BLUETOOTH_MAC_ADDRESS = ConstantesUtils.BLUETOOTH_MAC_ADDRESS;

    private String login;

    BluetoothSPP bt;
    TextView txtValorBpm;
    TableLayout tblBpm;
    //ListView lstBpm;

    final PulsacaoEndpointService pulsacaoService =  new Retrofit.Builder()
                                                                .baseUrl(ConstantesUtils.BASE_URL)
                                                                .addConverterFactory(GsonConverterFactory.create())
                                                                .build()
                                                                .create(PulsacaoEndpointService.class);

    boolean conectado;

    // Utilizado para gerenciar as médias de BPMs para enviar para a API
    List<PulsacaoDTO> ultimasPulsacoes = new ArrayList<>();
    //ArrayAdapter<PulsacaoDTO> pulsacaoDTOArrayAdapter;
    List<Long> bpmsTemp = new ArrayList<>();
    DateTime ultimoRegistro = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bpm);

        txtValorBpm = (TextView) findViewById(R.id.txtValorBpm);
        //lstBpm = (ListView) findViewById(R.id.lstBpm);
        tblBpm = (TableLayout) findViewById(R.id.tblBpm);

        //Recebe o login da activity de login
        Bundle bundle = getIntent().getExtras();
        login = bundle.getString("login");


        // Inicializa o serviço do JodaTime para Android
        JodaTimeAndroid.init(this);
        ultimoRegistro = DateTime.now();

//        pulsacaoDTOArrayAdapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_1, ultimasPulsacoes);

        montarListaBpms();

        bt = new BluetoothSPP(this);

        // Define se está conectado à internet
        conectado = ConexaoStatusUtil.isConnected(this);

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String bpm) {
                if(!StringUtils.isNumeric(bpm)) return;

                bpmsTemp.add(Long.valueOf(bpm));

                // Verifica se passaram alguns segundos desde o último registro para manter uma média coerente
                if(DateTime.now().isAfter(ultimoRegistro.plusSeconds(2))) {
                    String horaFormatada = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss").print(DateTime.now());
                    ultimoRegistro = DateTime.now();

                    Long mediaBpms = extrairMediaBpms();

                    // limpa a lista para a próxima iteração
                    bpmsTemp.clear();

                    if(conectado) {
                        //registrarPulsacoesBanco();
                        registrarPulsacao(mediaBpms, horaFormatada);

                    } else {
                        //Pulsacao pulsacaoEntity = new Pulsacao(login, mediaBpms, horaFormatada);
                        //pulsacaoEntity.save();
                    }
                }

                txtValorBpm.setText(StringUtils.leftPad(bpm, 3, '0')); // adiciona o 0 aos BPMs
            }
        });

        if (bt.isBluetoothAvailable() && bt.isBluetoothEnabled()) {
            bt.setupService();
            bt.startService(BluetoothState.DEVICE_OTHER);
            if (bt.isServiceAvailable()) {
                bt.connect(BLUETOOTH_MAC_ADDRESS);
            }
        }

    }


    private Long extrairMediaBpms() {
        // pega a soma dos BPMs para tirar a média
        Long somaBpms = 0L;
        for (Long bpmItem : bpmsTemp) {
            somaBpms += bpmItem;
        }

        // mantendo a média como um número inteiro
        return somaBpms / bpmsTemp.size();
    }

//    private void registrarPulsacoesBanco() {
//        List<Pulsacao> pulsacoesEntity = Pulsacao.find(Pulsacao.class, "login = ?", login);
//        if(pulsacoesEntity != null && !pulsacoesEntity.isEmpty()) {
//            for (Pulsacao pulsacaoItem : pulsacoesEntity) {
//                registrarPulsacao(pulsacaoItem.valor, pulsacaoItem.hora);
//            }
//        }
//    }

    private PulsacaoDTO registrarPulsacao(Long pulsacao, String hora) {
        PulsacaoDTO dto = new PulsacaoDTO();
        dto.setValor(pulsacao);
        dto.setLogin(login);
        dto.setHora(hora);

        Call<Void> pulsacaoDTOCall = pulsacaoService.registrar(dto);
        pulsacaoDTOCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("Pulsação registrada com sucesso. " + response.message() + " / " + response.code());
                montarListaBpms();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("Não foi possível registrar uma nova pulsação! " + t.getMessage());
            }
        });

        return dto;
    }

    private void montarListaBpms() {
        pulsacaoService.buscar(login, 20L).enqueue(new Callback<List<PulsacaoDTO>>() {
            @Override
            public void onResponse(Call<List<PulsacaoDTO>> call, Response<List<PulsacaoDTO>> response) {
                ultimasPulsacoes = response.body();

                tblBpm.removeAllViews();
                for (int i = 0; i < ultimasPulsacoes.size(); i++) {
                    TableRow row= new TableRow(BpmActivity.this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);

                    TextView txtBpm = new AppCompatTextView(BpmActivity.this);
                    txtBpm.setPadding(26,26,26,26);
                    txtBpm.setTextSize(19.0f);
                    txtBpm.setTextColor(Color.DKGRAY);
                    txtBpm.setText(String.format(Locale.US, "%d BPM", ultimasPulsacoes.get(i).getValor()));

                    TextView secondsAgo = new AppCompatTextView(BpmActivity.this);
                    secondsAgo.setPadding(26,26,26,26);
                    secondsAgo.setTextSize(19.0f);
                    secondsAgo.setTextColor(Color.GRAY);
                    secondsAgo.setGravity(Gravity.END);

                    DateTime hora = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss")
                                                    .parseDateTime(ultimasPulsacoes.get(i).getHora());
                    String horaFormatada = DateUtils.getRelativeTimeSpanString(hora.getMillis()).toString();
                    secondsAgo.setText(horaFormatada);

                    row.addView(txtBpm);
                    row.addView(secondsAgo);

                    tblBpm.addView(row,i);
                }
            }

            @Override
            public void onFailure(Call<List<PulsacaoDTO>> call, Throwable t) {
                System.out.println("não foi possível buscar as pulsações");
                ultimasPulsacoes = new ArrayList<>();
            }
        });
    }


    /**
     * Exibe a tela de estatisticas, com os ultimos batimentos do usuario logado
     * @param view
     */
    public void mostrarEstatistica(View view) {
        Intent toEstatistica = new Intent(this, EstatisticaActivity.class);
        toEstatistica.putExtra("login",login);
        startActivity(toEstatistica);
    }

}







