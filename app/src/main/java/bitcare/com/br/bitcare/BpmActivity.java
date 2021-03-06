package bitcare.com.br.bitcare;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import net.danlew.android.joda.JodaTimeAndroid;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import bitcare.com.br.bitcare.interfaces.PulsacaoEndpointService;
import bitcare.com.br.bitcare.models.CloudantViewContainerDTO;
import bitcare.com.br.bitcare.models.CloudantViewRowsDTO;
import bitcare.com.br.bitcare.models.PulsacaoDTO;
import bitcare.com.br.bitcare.utils.ConexaoStatusUtil;
import bitcare.com.br.bitcare.utils.ConstantesUtils;
import bitcare.com.br.bitcare.utils.DateTimeUtils;
import bitcare.com.br.bitcare.utils.RetrofitGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BpmActivity extends AppCompatActivity {

    public static final String BLUETOOTH_MAC_ADDRESS = ConstantesUtils.BLUETOOTH_MAC_ADDRESS;

    private String login;

    BluetoothSPP bt;
    TextView txtValorBpm;
    TableLayout tblBpm;
    //ListView lstBpm;

    boolean conectado;

    // Utilizado para gerenciar as médias de BPMs para enviar para a API
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
                    String horaFormatada = DateTimeUtils.toString(DateTime.now());
                    ultimoRegistro = DateTime.now();

                    Long mediaBpms = extrairMediaBpms();

                    // limpa a lista para a próxima iteração
                    bpmsTemp.clear();

                    if(conectado) {
                        //registrarPulsacoesBanco();
                        registrarPulsacao(mediaBpms, horaFormatada);
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

    private PulsacaoDTO registrarPulsacao(Long pulsacao, String hora) {
        PulsacaoDTO dto = new PulsacaoDTO();
        dto.setValor(pulsacao);
        dto.setLogin(login);
        dto.setHora(hora);

        PulsacaoEndpointService pulsacaoService = RetrofitGenerator.createService(PulsacaoEndpointService.class);
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
        PulsacaoEndpointService pulsacaoService = RetrofitGenerator.createService(PulsacaoEndpointService.class);
        Call<CloudantViewContainerDTO<PulsacaoDTO>> buscar = pulsacaoService.buscar(20L, "\"" + login + "\"");
        buscar.enqueue(new Callback<CloudantViewContainerDTO<PulsacaoDTO>>() {
            @Override
            public void onResponse(Call<CloudantViewContainerDTO<PulsacaoDTO>> call,
                                   Response<CloudantViewContainerDTO<PulsacaoDTO>> response) {
                CloudantViewContainerDTO<PulsacaoDTO> body = response.body();

                if(body == null || body.getRows() == null || body.getRows().isEmpty()) {
                    return;
                }

                List<PulsacaoDTO> dtos = new ArrayList<>();
                for (CloudantViewRowsDTO<PulsacaoDTO> row : body.getRows()) {
                    dtos.add(row.getValue());
                }

                Collections.sort(dtos, new Comparator<PulsacaoDTO>() {
                    @Override
                    public int compare(PulsacaoDTO p1, PulsacaoDTO p2) {
                        return p2.getHora().compareTo(p1.getHora());
                    }
                });

                tblBpm.removeAllViews();
                for (int i = 0; i < dtos.size(); i++) {
                    PulsacaoDTO pulsacaoDTO = dtos.get(i);

                    TableRow row= new TableRow(BpmActivity.this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);

                    TextView txtBpm = new AppCompatTextView(BpmActivity.this);
                    txtBpm.setPadding(26,26,26,26);
                    txtBpm.setTextSize(19.0f);
                    txtBpm.setTextColor(Color.DKGRAY);
                    txtBpm.setText(String.format(Locale.US, "%d BPM", pulsacaoDTO.getValor()));

                    TextView secondsAgo = new AppCompatTextView(BpmActivity.this);
                    secondsAgo.setPadding(26,26,26,26);
                    secondsAgo.setTextSize(19.0f);
                    secondsAgo.setTextColor(Color.GRAY);
                    secondsAgo.setGravity(Gravity.END);

                    DateTime hora = DateTimeUtils.toDateTime(pulsacaoDTO.getHora());
                    String horaFormatada = DateUtils.getRelativeTimeSpanString(hora.getMillis()).toString();
                    secondsAgo.setText(horaFormatada);

                    row.addView(txtBpm);
                    row.addView(secondsAgo);

                    tblBpm.addView(row,i);
                }
            }

            @Override
            public void onFailure(Call<CloudantViewContainerDTO<PulsacaoDTO>> call, Throwable t) {
                System.out.println("não foi possível buscar as pulsações");
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







