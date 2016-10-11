package bitcare.com.br.bitcare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import net.danlew.android.joda.JodaTimeAndroid;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import bitcare.com.br.bitcare.entities.Pulsacao;
import bitcare.com.br.bitcare.interfaces.PulsacaoEndpointService;
import bitcare.com.br.bitcare.models.PulsacaoDTO;
import bitcare.com.br.bitcare.utils.ConexaoStatusUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BpmActivity extends AppCompatActivity {

    public static final String BLUETOOTH_MAC_ADDRESS = "20:16:06:28:08:99";
    public static final String LOGIN = "felipe";

    BluetoothSPP bt;
    TextView txtValorBpm;

    final PulsacaoEndpointService pulsacaoService =  new Retrofit.Builder()
                                                        .baseUrl("http://177.189.64.217:9090/")//"https://bitcare-141317.appspot.com/")
                                                        .addConverterFactory(GsonConverterFactory.create())
                                                        .build()
                                                        .create(PulsacaoEndpointService.class);

    boolean conectado;

    // Utilizado para gerenciar as médias de BPMs para enviar para a API
    List<Long> bpmsTemp = new ArrayList<>();
    DateTime ultimoRegistro = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bpm);

        txtValorBpm = (TextView) findViewById(R.id.txtValorBpm);

        // Inicializa o serviço do JodaTime para Android
        JodaTimeAndroid.init(this);
        ultimoRegistro = DateTime.now();

        bt = new BluetoothSPP(this);

        // Define se está conectado à internet
        conectado = ConexaoStatusUtil.isConnected(this);

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String bpm) {
                if(!StringUtils.isNumeric(bpm)) return;

                bpmsTemp.add(Long.valueOf(bpm));

                // Verifica se passaram alguns segundos desde o último registro para manter uma média coerente
                if(DateTime.now().isAfter(ultimoRegistro.plusSeconds(5))) {
                    String horaFormatada = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss").print(DateTime.now());

                    Long mediaBpms = extrairMediaBpms();

                    // limpa a lista para a próxima iteração
                    bpmsTemp.clear();

                    if(conectado) {
                        //registrarPulsacoesBanco();
                        registrarPulsacao(mediaBpms, horaFormatada);

                        ultimoRegistro = DateTime.now();
                    } else {
                        //Pulsacao pulsacaoEntity = new Pulsacao(LOGIN, mediaBpms, horaFormatada);
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

    private void registrarPulsacoesBanco() {
        List<Pulsacao> pulsacoesEntity = Pulsacao.find(Pulsacao.class, "login = ?", LOGIN);
        if(pulsacoesEntity != null && !pulsacoesEntity.isEmpty()) {
            for (Pulsacao pulsacaoItem : pulsacoesEntity) {
                registrarPulsacao(pulsacaoItem.valor, pulsacaoItem.hora);
            }
        }
    }

    private PulsacaoDTO registrarPulsacao(Long pulsacao, String hora) {
        PulsacaoDTO dto = new PulsacaoDTO();
        dto.setValor(pulsacao);
        dto.setLogin(LOGIN);
        dto.setHora(hora);

        Call<Void> pulsacaoDTOCall = pulsacaoService.registrar(dto);
        pulsacaoDTOCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("Pulsação registrada com sucesso. " + response.message() + " / " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("Não foi possível registrar uma nova pulsação! " + t.getMessage());
            }
        });

        return dto;
    }

}
