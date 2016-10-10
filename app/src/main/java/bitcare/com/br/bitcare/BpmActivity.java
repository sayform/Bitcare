package bitcare.com.br.bitcare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

public class BpmActivity extends AppCompatActivity {

    BluetoothSPP bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bpm);

        bt = new BluetoothSPP(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        bt.setupService();
        bt.startService(BluetoothState.DEVICE_OTHER);
        bt.connect("20:16:06:28:08:99");
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                Toast.makeText(BpmActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
