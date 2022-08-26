package com.example.client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {
    SensorManager sensormanager;
    Sensor gyro;
    Sensor gravity;
    Sensor accel;
    float gravityx;
    float gravityy;
    float gravityz;
    float accelx;
    float accely;
    float accelz;
    float gyrox;
    float gyroy;
    float gyroz;
    boolean flag = false;
//    boolean isNear = true;
    int isNear;

    String accelMessage = "{\"S\":\"A\",\"x\":"+ Float.toString(0) + ",\"y\":" + Float.toString(0)+ ",\"z\":" + Float.toString(0)+"}";
    String gravityMessage = "{\"S\": \"G\",\"x\":"+ Float.toString(0) + ",\"y\":" + Float.toString(0)+ ",\"z\":" + Float.toString(0)+"}";
    String gyroMessage = "{\"S\":\"Gy\",\"x\":"+ Float.toString(0) + ",\"y\":" + Float.toString(0)+ ",\"z\":" + Float.toString(0)+"}";

    Thread Thread1 = null;
    EditText etIP, etPort;
    TextView tvMessages;
    EditText etMessage;
    Button btnSend;
    Button btnNear;
    Button btnFar;
    Button btnMid;
    String SERVER_IP;
    int SERVER_PORT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etIP = findViewById(R.id.etIP);
        etPort = findViewById(R.id.etPort);
        tvMessages = findViewById(R.id.tvMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        btnNear = findViewById(R.id.Near);
        btnFar = findViewById(R.id.Far);
        btnMid = findViewById(R.id.mid);
        Button btnConnect = findViewById(R.id.btnConnect);

        sensormanager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyro =sensormanager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        gravity = sensormanager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        accel = sensormanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        btnNear.setBackgroundColor(Color.RED);
        btnFar.setBackgroundColor(Color.BLUE);


        btnNear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNear != 1){
                    isNear = 1;
                    btnNear.setBackgroundColor(Color.RED);
                    btnMid.setBackgroundColor(Color.BLUE);
                    btnFar.setBackgroundColor(Color.BLUE);
                }

            }
        });

        btnMid.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (isNear != 2){
                    isNear = 2;
                    btnNear.setBackgroundColor(Color.BLUE);
                    btnMid.setBackgroundColor(Color.RED);
                    btnFar.setBackgroundColor(Color.BLUE);
                }
            }
        });


        btnFar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNear != 3){
                    isNear = 3;
                    btnNear.setBackgroundColor(Color.BLUE);
                    btnMid.setBackgroundColor(Color.BLUE);
                    btnFar.setBackgroundColor(Color.RED);
                }
            }
        });

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMessages.setText("");
                SERVER_IP = etIP.getText().toString().trim();
                SERVER_PORT = Integer.parseInt(etPort.getText().toString().trim());
                Thread1 = new Thread(new Thread1());
                Thread1.start();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etMessage.getText().toString().trim();
                String mess = Float.toString(accelx);
                flag = true;
//                new Thread(new Thread3(mess)).start();
            }
        });
    }



    public void onResume() {
        super.onResume();
        sensormanager.registerListener(gyroListener, gyro, SensorManager.SENSOR_DELAY_UI);
        sensormanager.registerListener(accelListener, accel, SensorManager.SENSOR_DELAY_UI);
        sensormanager.registerListener(gravityListener, gravity, SensorManager.SENSOR_DELAY_UI);
    }

    public void onStop() {
        super.onStop();
        sensormanager.unregisterListener(gyroListener);
        sensormanager.unregisterListener(accelListener);
        sensormanager.unregisterListener(gravityListener);
    }
    

    public SensorEventListener gyroListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
                gyrox = sensorEvent.values[0];
                gyroy = sensorEvent.values[1];
                gyroz = sensorEvent.values[2];
                gyroMessage = "{\"S\":\"Gy\",\"x\":"+ Float.toString(gyrox) + ",\"y\":" + Float.toString(gyroy)+ ",\"z\":" + Float.toString(gyroz)+"}";
//                if (flag) {
//                    new Thread(new Thread3(gyroMessage)).start();
//                }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    public SensorEventListener accelListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            accelx = sensorEvent.values[0];
            accely = sensorEvent.values[1];
            accelz = sensorEvent.values[2];
            accelMessage = "{\"S\":\"A\",\"x\":"+ Float.toString(accelx) + ",\"y\":" + Float.toString(accely)+ ",\"z\":" + Float.toString(accelz)+"}";

//            if (flag) {
//                new Thread(new Thread3(accelMessage)).start();
//
//            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    public SensorEventListener gravityListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            gravityx = sensorEvent.values[0];
            gravityy = sensorEvent.values[1];
            gravityz = sensorEvent.values[2];
            gravityMessage = "{\"S\": \"G\",\"x\":"+ Float.toString(gravityx) + ",\"y\":" + Float.toString(gravityy)+ ",\"z\":" + Float.toString(gravityz)+"}";
            String payLoad;
            if (flag) {
//                new Thread(new Thread3(gravityMessage)).start();
                if (isNear == 1){
                    payLoad = "{\"L\": \"N\"}";
                }
                else if (isNear == 2){
                    payLoad = "{\"L\": \"M\"}";
                }
                else{
                    payLoad = "{\"L\": \"F\"}";
                }
                new Thread(new Thread3(payLoad+accelMessage+gyroMessage+gravityMessage)).start();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };




    private PrintWriter output;
    private BufferedReader input;
    class Thread1 implements Runnable {
        public void run() {
            Socket socket;
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
                output = new PrintWriter(socket.getOutputStream());
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvMessages.setText("Connected\n");
                    }
                });
                new Thread(new Thread2()).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    class Thread2 implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    final String message = input.readLine();
                    if (message != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvMessages.append("server: " + message + "\n");
                            }
                        });
                    } else {
                        Thread1 = new Thread(new Thread1());
                        Thread1.start();
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class Thread3 implements Runnable {
        private String message;
        Thread3(String message) {
            this.message = message;
        }
        @Override
        public void run() {
            output.write(message);
            output.flush();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    tvMessages.append("client: " + message + "\n");
                    etMessage.setText("");
                }
            });
        }
    }
}