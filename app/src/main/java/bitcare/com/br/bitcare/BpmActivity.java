package bitcare.com.br.bitcare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import net.danlew.android.joda.JodaTimeAndroid;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BpmActivity extends AppCompatActivity {

    public static final String BLUETOOTH_MAC_ADDRESS = "20:16:06:28:08:99";

    BluetoothSPP bt;
    TextView txtValorBpm;

    final PulsacaoService pulsacaoService =  new Retrofit.Builder()
                                                    .baseUrl("http://177.189.64.217:9090/")//"https://bitcare-141317.appspot.com/")
                                                    .addConverterFactory(GsonConverterFactory.create())
                                                    .build()
                                                    .create(PulsacaoService.class);

    boolean conectado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bpm);

        JodaTimeAndroid.init(this);

        txtValorBpm = (TextView) findViewById(R.id.txtValorBpm);

        bt = new BluetoothSPP(this);

        conectado = ConexaoStatusUtil.isConnected(this);
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String bpm) {
                // TODO implementar registro em lista do SQLite com tratamento
                // para pegar apenas resultados úteis
                if(conectado && StringUtils.isNotBlank(bpm)) {
                    registrarPulsacao(bpm);
                } else {
                    // TODO implementar registro no SQLite
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

    private PulsacaoDTO registrarPulsacao(String pulsacao) {
        PulsacaoDTO dto = new PulsacaoDTO();
        dto.setValor(Long.valueOf(pulsacao));
        dto.setLogin("felipe");

        String hora = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss").print(DateTime.now());
        dto.setHora(hora);

        Call<Void> pulsacaoDTOCall = pulsacaoService.registrar(dto);
        pulsacaoDTOCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //System.out.println("Pulsação registrada com sucesso. " + response.message() + " / " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //System.out.println("Não foi possível registrar uma nova pulsação! " + t.getMessage());
            }
        });

        return dto;
    }

}
