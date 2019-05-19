package com.mertcansegmen.cizgiizleyenarababluetoothkumanda;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton bluetoothButon;
    ImageButton ileriButon;
    ImageButton geriButon;
    ImageButton sagButon;
    ImageButton solButon;


    private final String BLUETOOTH_MODUL_ADRESI = "00:18:E4:40:00:06";
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothDevice bluetoothCihazi;
    private BluetoothSocket socket;
    private OutputStream outputStream;

    String yon;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothButon = findViewById(R.id.btn_bluetooth);
        ileriButon = findViewById(R.id.btn_ileri);
        geriButon = findViewById(R.id.btn_geri);
        sagButon = findViewById(R.id.btn_sag);
        solButon = findViewById(R.id.btn_sol);

        yon = "9";

        bluetoothButon.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));

        bluetoothButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetoothBaglantisiBaslat()){
                    if(aracaBaglan()) {
                        bluetoothButon.setImageResource(R.drawable.ic_basarili);
                        bluetoothButon.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                        Toast.makeText(MainActivity.this, "Bağlantı başarılı.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        bluetoothButon.setImageResource(R.drawable.ic_basarisiz);
                        bluetoothButon.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        Toast.makeText(MainActivity.this, "Bluetooth modülüne bağlanılamadı.", Toast.LENGTH_LONG).show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                bluetoothButon.setImageResource(R.drawable.ic_bluetooth_black_24dp);
                                bluetoothButon.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                            }
                        }, 2000);
                    }
                }
            }
        });

        ileriButon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(yon.equals("3"))
                        yon = "3";
                    else if(yon.equals("2"))
                        yon = "5";
                    else if(yon.equals("4"))
                        yon = "8";
                    else
                        yon = "1";
                    veriGonder(yon);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(yon.equals("5"))
                        yon = "2";
                    else if(yon.equals("8"))
                        yon = "4";
                    else
                        yon = "9";

                    veriGonder(yon);
                }
                return false;
            }
        });

        geriButon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(yon.equals("1"))
                        yon = "1";
                    else if(yon.equals("2"))
                        yon = "6";
                    else if(yon.equals("4"))
                        yon = "7";
                    else
                        yon = "3";

                    veriGonder(yon);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(yon.equals("6"))
                        yon = "2";
                    else if(yon.equals("7"))
                        yon = "4";
                    else
                        yon = "9";

                    veriGonder(yon);
                }
                return false;
            }
        });

        solButon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(yon.equals("2"))
                        yon = "2";
                    else if(yon.equals("1"))
                        yon = "8";
                    else if(yon.equals("3"))
                        yon = "7";
                    else
                        yon = "4";

                    veriGonder(yon);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(yon.equals("8"))
                        yon = "1";
                    else if(yon.equals("7"))
                        yon = "3";
                    else
                        yon = "9";

                    veriGonder(yon);
                }
                return false;
            }
        });

        sagButon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(yon.equals("4"))
                        yon = "4";
                    else if(yon.equals("1"))
                        yon = "5";
                    else if(yon.equals("3"))
                        yon = "6";
                    else
                        yon = "2";

                    veriGonder(yon);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(yon.equals("5"))
                        yon = "1";
                    else if(yon.equals("6"))
                        yon = "3";
                    else
                        yon = "9";

                    veriGonder(yon);
                }
                return false;
            }
        });
    }

    private void veriGonder(String veri){
        Log.i("mert", veri);

        try {
            outputStream.write(veri.getBytes());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private boolean aracaBaglan() {
        boolean baglantiBasarili = true;

        try {
            socket = bluetoothCihazi.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
        }
        catch(IOException e) {
            e.printStackTrace();
            baglantiBasarili = false;
        }

        if(baglantiBasarili) {
            try {
                outputStream = socket.getOutputStream();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }

        return baglantiBasarili;
    }

    private boolean bluetoothBaglantisiBaslat() {
        boolean bluetoothModuluBulundu = false;

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Cihazın bluetooth'u destekleyip desteklemediğini kontrol ediyor
        if(bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Cihaz bluetooth desteklemiyor.", Toast.LENGTH_LONG).show();
            return false;
        }

        // Bluetooth açık değilse açılması için onay istiyor.
        if(!bluetoothAdapter.isEnabled()) {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter,0);
            return false;
        }

        // Telefonun eşleştiği cihazları getiriyor
        Set<BluetoothDevice> eslesmisCihazlar = bluetoothAdapter.getBondedDevices();

        // Eğer telefonun eşleştiği bir cihaz bulunamadıysa hata veriyor
        if(eslesmisCihazlar.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Lütfen telefonunuzu bluetooth modülüyle eşleştirin.", Toast.LENGTH_SHORT).show();
        }
        // En az bir tane eşleşmiş cihaz bulunduysa, bulunan cihazları bluetooth modülünün
        // MAC adresiyle karşılaştırıyor.
        else{
            for(BluetoothDevice eslesmisCihaz : eslesmisCihazlar) {
                if(eslesmisCihaz.getAddress().equals(BLUETOOTH_MODUL_ADRESI)) {
                    bluetoothCihazi = eslesmisCihaz;
                    bluetoothModuluBulundu = true;
                    break;
                }
            }
        }

        return bluetoothModuluBulundu;
    }
}
