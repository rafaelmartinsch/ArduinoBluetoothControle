package com.rmc.arduinobluetoothcontrole;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter bluetoothAdapter;
    BluetoothSocket bluSocket = null;
    private OutputStream outStream = null;
    ArrayList<String> nomeArrayAdapter = new ArrayList<String>();
    ArrayList<String> macArrayAdapter = new ArrayList<String>();
    private Boolean conectado = false;

    ImageView bt_conectar;
    TextView tx_conectar;
    ImageButton bt_up, bt_down, bt_left, bt_right, bt_rotate_left, bt_rotate_right;

    Comando comando;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w("BLuetoouth", "Start");

        LinearLayout conectar = findViewById(R.id.container_conexao);
        bt_conectar = findViewById(R.id.bt_conectar);
        tx_conectar = findViewById(R.id.tex_conectar);

        //controles
        bt_down = findViewById(R.id.bt_down);
        bt_up = findViewById(R.id.bt_up);
        bt_left = findViewById(R.id.bt_left);
        bt_right = findViewById(R.id.bt_right);
        bt_rotate_left = findViewById(R.id.bt_rotate_left);
        bt_rotate_right = findViewById(R.id.bt_rotate_right);


        SharedPreferences preferencias = getSharedPreferences("prefComandos", MODE_PRIVATE);
        comando = new Comando(preferencias);



        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.i("BLuetoouth", "Adaptador não encontrado");
            Toast.makeText(getApplicationContext(), "Adaptador Buetooth Não Encontrado!", Toast.LENGTH_LONG).show();
            bt_conectar.setImageResource(R.drawable.blueerro);
            tx_conectar.setText("Adaptador não encontrado");

        } else if (!bluetoothAdapter.isEnabled()) {
            Log.i("BLuetoouth", "Adaptador não ativo");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) return;
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        Set<BluetoothDevice> pairedDevices = null;


        if (bluetoothAdapter != null) {
            Log.i("BLuetoouth", "listando dispositivos");
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            pairedDevices = bluetoothAdapter.getBondedDevices();
            for (BluetoothDevice device : pairedDevices) {
                nomeArrayAdapter.add(device.getName());
                macArrayAdapter.add(device.getAddress());
            }
        }


        conectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listDevices();
            }
        });



        //botoes
        bt_up.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(conectado)
                    switch(event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            if(bluSocket != null){
                                bt_up.setBackgroundResource(R.drawable.seta_on_up);
                                dadosEnvio(comando.getUp());
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if(bluSocket != null){
                                bt_up.setBackgroundResource(R.drawable.seta_off_up);
                                dadosEnvio(comando.getStop());
                            }
                            break;
                    }
                return false;
            }
        });

        bt_down.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(conectado) switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if(bluSocket != null){
                            bt_down.setBackgroundResource(R.drawable.seta_on_down);
                            dadosEnvio(comando.getDown());
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if(bluSocket != null){
                            bt_down.setBackgroundResource(R.drawable.seta_off_down);
                            dadosEnvio(comando.getStop());
                        }
                        break;
                }
                return false;

            }
        });

        bt_right.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(conectado) switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if(bluSocket != null){
                            bt_right.setBackgroundResource(R.drawable.seta_on_right);
                            dadosEnvio(comando.getRight());
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if(bluSocket != null){
                            bt_right.setBackgroundResource(R.drawable.seta_off_right);
                            dadosEnvio(comando.getStop());
                        }
                        break;
                }
                return false;

            }
        });

        bt_left.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(conectado) switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if(bluSocket != null){
                            bt_left.setBackgroundResource(R.drawable.seta_on_left);
                            dadosEnvio(comando.getLeft());
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if(bluSocket != null){
                            bt_left.setBackgroundResource(R.drawable.seta_off_left);
                            dadosEnvio(comando.getStop());
                        }
                        break;
                }
                return false;

            }
        });

        bt_rotate_left.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(conectado) switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if(bluSocket != null){
                            bt_rotate_left.setBackgroundResource(R.drawable.rotate_left_on);
                            dadosEnvio(comando.getRotLeft());
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if(bluSocket != null){
                            bt_rotate_left.setBackgroundResource(R.drawable.rotate_left);
                            dadosEnvio(comando.getStop());
                        }
                        break;
                }
                return false;

            }
        });

        bt_rotate_right.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(conectado) switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if(bluSocket != null){
                            bt_rotate_right.setBackgroundResource(R.drawable.rotate_right_on);
                            dadosEnvio(comando.getRotRight());
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if(bluSocket != null){
                            bt_rotate_right.setBackgroundResource(R.drawable.rotate_right);
                            dadosEnvio(comando.getStop());
                        }
                        break;
                }
                return false;

            }
        });
    }

    public void listDevices(){

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, nomeArrayAdapter);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione o Dispositivo:");
        builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                if (arg1 >= 0) {
                    Conectar(macArrayAdapter.get(arg1), nomeArrayAdapter.get(arg1));
                    arg0.dismiss();
                }
            }
        });

        AlertDialog alerta = builder.create();
        alerta.show();
    }

    public void Conectar(String mac, String name) {
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(mac);
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            bluSocket = device.createRfcommSocketToServiceRecord(uuid);
            bluSocket =(BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
            bluSocket.connect();

            conectado=true;
            bt_conectar.setImageResource(R.drawable.blueon);
            tx_conectar.setTextColor(Color.BLUE);
            tx_conectar.setText("Conectado "+ name);
        }catch(IOException e){
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Erro");
            alertDialog.setMessage("Não foi possível se conectar com o dispositivo selecionado!");
            Log.e("BtExemption",e.toString()+"Max Andress: "+mac+":"+name);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.setIcon(R.mipmap.ic_launcher);
            alertDialog.show();
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void dadosEnvio(String data){
        try{
            outStream = bluSocket.getOutputStream();
        }catch(IOException e){}

        String mensagem = data;
        byte[] msgBuffer = mensagem.getBytes();

        try{
            outStream.write(msgBuffer);
        }catch(IOException e){}
    }

}
