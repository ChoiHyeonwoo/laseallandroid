package kr.ac.smu.kuni.testtest;

/**
 * Created by Juseok Song on 2016-03-14.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class RightMainActivity extends MainActivity implements SensorEventListener {

    private SimpleSocket ssocket;
    private TerminateApp terminateApp;
    public WifiManager wifiManager;
    public WifiInfo wInfo;
    public String macAddress;
    private String ip = "172.16.26.54"; // IP
    private int port = 3000; // PORT번호

    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message inputMessage) {
            switch(inputMessage.what){
                case SimpleSocket.MessageTypeClass.SIMSOCK_DATA :
                    String msg = (String) inputMessage.obj;
                    Log.d("OUT", msg);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    // do something with UI
                    break;

                case SimpleSocket.MessageTypeClass.SIMSOCK_CONNECTED :
                    String msg2 = (String) inputMessage.obj;
                    Log.d("OUT", msg2);
                    conChk=1;
                    Toast.makeText(getApplicationContext(), msg2, Toast.LENGTH_SHORT).show();
                    // do something with UI
                    break;

                case SimpleSocket.MessageTypeClass.SIMSOCK_DISCONNECTED :
                    String msg3 = (String) inputMessage.obj;
                    Log.d("OUT", msg3);
                    Toast.makeText(getApplicationContext(), msg3, Toast.LENGTH_SHORT).show();
                    // do something with UI
                    break;

            }
        }
    };

    RadioGroup layoutRG;
    ImageButton red_point;
    ImageButton paintPage;
    ImageButton stringPage;
    ImageButton mousePage;
    ImageButton backBtn;
    ImageButton goBtn;
    EditText ip_Et1;
    EditText string_edittext;
    LinearLayout ll;
    LinearLayout string_ll;
    LinearLayout main_ll;
    StringBuffer buf;
    WindowManager wm;
    Display display;
    Point p;
    private int sx;
    private int sy;
    private int conChk = 0;

    boolean drawButtonFlag = false;
    boolean stringFlag = false;
    boolean mouseFlag = false;
    boolean flag = false;

    float accelXValue;
    float accelYValue;
    float accelZValue;
    // 실수의 출력 자리수를 지정하는 포맷 객체
    DecimalFormat m_format;

    // 데이터를 저장할 변수들
    float[] m_gravity_data = new float[3];
    float[] m_accel_data = new float[3];

    float gyroX;
    float gyroY;
    float gyroZ;
    private SensorManager mSensorManager;
    private Sensor mGyroscope;
    private Sensor accSensor;

    TextView text1;
    int color = 0xffffff00;

    private static final int MY_MENU_1 = Menu.FIRST;
    private static final int MY_MENU_2 = Menu.FIRST + 1;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainlay);

        Intent intent = getIntent();
        ip = intent.getStringExtra("ipAddress");
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wInfo = wifiManager.getConnectionInfo();
        macAddress = wInfo.getMacAddress();
        terminateApp = new TerminateApp(this);

        final String[] colorstrings = new String[4];
        colorstrings[0] = "검정";
        colorstrings[1] = "빨강";
        colorstrings[2] = "초록";
        colorstrings[3] = "파랑";
        final boolean colorselection[] = new boolean[4];
        colorselection[0] = true;
        colorselection[1] = false;
        colorselection[2] = false;
        colorselection[3] = false;
        final String[] sizestrings = new String[3];
        sizestrings[0] = "크게";
        sizestrings[1] = "중간";
        sizestrings[2] = "작게";
        final boolean sizeselection[] = new boolean[3];
        sizeselection[0] = true;
        sizeselection[1] = false;
        sizeselection[2] = false;

        red_point = (ImageButton) findViewById(R.id.red_point);
        paintPage = (ImageButton) findViewById(R.id.paintPage);
        stringPage = (ImageButton) findViewById(R.id.stringPage);
        mousePage = (ImageButton) findViewById(R.id.mousePage);
        main_ll = (LinearLayout) findViewById(R.id.main_ll);
        main_ll.setBackgroundColor(Color.rgb(125, 149, 186));
        layoutRG = (RadioGroup) findViewById(R.id.layoutRG);
        backBtn = (ImageButton) findViewById(R.id.backBtn);
        goBtn = (ImageButton) findViewById(R.id.goBtn);

        // 포맷 객체를 생성한다.
        m_format = new DecimalFormat();
        // 소수점 두자리까지 출력될 수 있는 형식을 지정한다.
        m_format.applyLocalizedPattern("0.##");

        ssocket = new SimpleSocket(ip, port, mHandler);
        ssocket.start();

        paintPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conChk == 1) {
                    if (drawButtonFlag == false) {
                        paintPage.setSelected(true);
                        drawButtonFlag = true;
                        ssocket.sendString("drawon");
                        flag = false;
                        flag = true;
                    } else {
                        paintPage.setSelected(false);
                        drawButtonFlag = false;
                        ssocket.sendString("okeydokey");
                        ssocket.sendString("subMenuOff");
                        flag = true;
                        flag = false;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "IP 연결을 먼저해 주세요!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        stringPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conChk == 1) {
                    if (stringFlag == false) {
                        stringPage.setSelected(true);
                        stringFlag = true;
                        ssocket.sendString("stringon");
                        flag = false;
                        flag = true;
                    } else {
                        stringPage.setSelected(false);
                        stringFlag = false;
                        ssocket.sendString("okeydokey");
                        ssocket.sendString("subMenuOff");
                        flag = true;
                        flag = false;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "IP 연결을 먼저해 주세요!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mousePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conChk == 1) {
                    if (mouseFlag == false) {
                        stringPage.setSelected(true);
                        mouseFlag = true;
                        ssocket.sendString("mouseon");
                        flag = false;
                        flag = true;
                    } else {
                        stringPage.setSelected(false);
                        mouseFlag = false;
                        ssocket.sendString("okeydokey");
                        ssocket.sendString("subMenuOff");
                        flag = true;
                        flag = false;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "IP 연결을 먼저해 주세요!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (conChk == 1) {
                    ssocket.sendString("back");
                } else {
                    Toast.makeText(getApplicationContext(), "IP 연결을 먼저해 주세요!", Toast.LENGTH_SHORT).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder dlg2 = new AlertDialog.Builder(RightMainActivity.this);
                            View dialogView2 = (View) View.inflate(getApplicationContext(), R.layout.ip_layout, null);
                            ll = (LinearLayout) dialogView2.findViewById(R.id.ll);
                            ip_Et1 = (EditText) dialogView2.findViewById(R.id.ip_Et1);

                            dlg2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String ip_str = ip_Et1.getText().toString();
                                    ip = ip_str;
                                    ssocket = new SimpleSocket(ip, port, mHandler);
                                    ssocket.start();
                                }
                            });
                            dlg2.setView(dialogView2);
                            dlg2.show();
                        }
                    }, 300);
                }
            }
        });

        goBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (conChk == 1) {
                    ssocket.sendString("go");
                } else {
                    Toast.makeText(getApplicationContext(), "IP 연결을 먼저해 주세요!", Toast.LENGTH_SHORT).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder dlg2 = new AlertDialog.Builder(RightMainActivity.this);
                            View dialogView2 = (View) View.inflate(getApplicationContext(), R.layout.ip_layout, null);
                            ll = (LinearLayout) dialogView2.findViewById(R.id.ll);
                            ip_Et1 = (EditText) dialogView2.findViewById(R.id.ip_Et1);

                            dlg2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String ip_str = ip_Et1.getText().toString();
                                    ip = ip_str;
                                    ssocket = new SimpleSocket(ip, port, mHandler);
                                    ssocket.start();
                                }
                            });
                            dlg2.setView(dialogView2);
                            dlg2.show();
                        }
                    }, 300);
                }
            }
        });

        //센서 매니저 얻기
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //자이로스코프 센서(회전)
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        //엑셀러로미터 센서(가속)
        accSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        wm = getWindowManager();
        display = wm.getDefaultDisplay();
        p = new Point();
        display.getSize(p);
        buf = new StringBuffer();
        sx = p.x;
        sy = p.y;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        terminateApp.onBackPressed();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();

        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    if (conChk == 1) {
                        ssocket.sendString("go");
                    } else {
                        Toast.makeText(getApplicationContext(), "IP 연결을 먼저해 주세요!", Toast.LENGTH_SHORT).show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder dlg2 = new AlertDialog.Builder(RightMainActivity.this);
                                View dialogView2 = (View) View.inflate(getApplicationContext(), R.layout.ip_layout, null);
                                ll = (LinearLayout) dialogView2.findViewById(R.id.ll);
                                ip_Et1 = (EditText) dialogView2.findViewById(R.id.ip_Et1);

                                dlg2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String ip_str = ip_Et1.getText().toString();
                                        ip = ip_str;
                                        ssocket = new SimpleSocket(ip, port, mHandler);
                                        ssocket.start();
                                    }
                                });
                                dlg2.setView(dialogView2);
                                dlg2.show();
                            }
                        }, 300);
                    }
                }
                return true;

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    if (conChk == 1) {
                        ssocket.sendString("back");
                    } else {
                        Toast.makeText(getApplicationContext(), "IP 연결을 먼저해 주세요!", Toast.LENGTH_SHORT).show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder dlg2 = new AlertDialog.Builder(RightMainActivity.this);
                                View dialogView2 = (View) View.inflate(getApplicationContext(), R.layout.ip_layout, null);
                                ll = (LinearLayout) dialogView2.findViewById(R.id.ll);
                                ip_Et1 = (EditText) dialogView2.findViewById(R.id.ip_Et1);

                                dlg2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String ip_str = ip_Et1.getText().toString();
                                        ip = ip_str;
                                        ssocket = new SimpleSocket(ip, port, mHandler);
                                        ssocket.start();
                                    }
                                });
                                dlg2.setView(dialogView2);
                                dlg2.show();
                            }
                        }, 300);
                    }
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    void openDialog(boolean supportsAlpha) {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(RightMainActivity.this, color, supportsAlpha, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                //Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                RightMainActivity.this.color = color;
                Toast.makeText(getApplicationContext(), String.format("포인터 컬러 : 0x%08x", color), Toast.LENGTH_SHORT).show();
                ssocket.sendString(String.format("0x%08x", color));
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    void displayColor() {
        text1.setText(String.format("포인터 컬러 : 0x%08x", color));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add(0, MY_MENU_1, 0, "IP 재입력");
        menu.add(0, MY_MENU_2, 0, "포인터 색 선택").setShortcut('3', 'c');
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        switch (id) {
            case MY_MENU_1:
                AlertDialog.Builder dlg = new AlertDialog.Builder(RightMainActivity.this);
                View dialogView = (View) View.inflate(getApplicationContext(), R.layout.ip_layout, null);
                ll = (LinearLayout) dialogView.findViewById(R.id.ll);
                ip_Et1 = (EditText) dialogView.findViewById(R.id.ip_Et1);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String ip_str = ip_Et1.getText().toString();
                        ip = ip_str;
                        ssocket = new SimpleSocket(ip, port, mHandler);
                        ssocket.start();
                    }
                });
                dlg.setView(dialogView);
                dlg.show();
                return true;

            case MY_MENU_2:
                openDialog(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    // 주기 설명
    // SENSOR_DELAY_UI 갱신에 필요한 정도 주기
    // SENSOR_DELAY_NORMAL 화면 방향 전환 등의 일상적인  주기
    // SENSOR_DELAY_GAME 게임에 적합한 주기
    // SENSOR_DELAY_FASTEST 최대한의 빠른 주기

    //리스너 등록
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener((SensorEventListener) this, mGyroscope, 0);
        mSensorManager.registerListener((SensorEventListener) this, accSensor, SensorManager.SENSOR_DELAY_UI);
    }

    //리스너 해제
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener((SensorEventListener) this);
    }

    public void onSensorChanged(SensorEvent event) {
        red_point.setClickable(true);
        Sensor sensor = event.sensor;
        red_point.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent mevent) {
                switch (mevent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        if (conChk == 1) {
                            if (drawButtonFlag == true) {
                                flag = true;
                                ssocket.sendString("good");
                                ssocket.sendString("true");
                            }
                            else if (stringFlag == true) {
                                flag = false;
                            }
                            else if (mouseFlag == true) {
                                flag = false;
                                ssocket.sendString("true");
                                ssocket.sendString("good");
                            }
                            else {
                                flag = true;
                                ssocket.sendString("true");
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "IP 연결을 먼저해 주세요!", Toast.LENGTH_SHORT).show();
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                        if (conChk == 1) {
                            if (drawButtonFlag == true) {
                                flag = true;
                                ssocket.sendString("true");
                                ssocket.sendString("false");
                            }
                            else if (stringFlag == true) {
                                ssocket.sendString("true");
                                ssocket.sendString("good");
                                AlertDialog.Builder dlg = new AlertDialog.Builder(RightMainActivity.this);
                                View dialogView = (View) View.inflate(getApplicationContext(), R.layout.string_layout, null);
                                string_ll = (LinearLayout) dialogView.findViewById(R.id.string_ll);
                                string_edittext = (EditText) dialogView.findViewById(R.id.string_edittext);
                                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String sendString = string_edittext.getText().toString();
                                        ssocket.sendString(sendString);
                                        flag = true;
                                    }
                                });
                                dlg.setView(dialogView);
                                dlg.show();
                            }
                            else if (mouseFlag == true) {
                                flag = true;
                            }
                            else {
                                flag = true;
                                ssocket.sendString("true");
                                flag = false;
                                ssocket.sendString("false");
                            }
                        } else {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder dlg2 = new AlertDialog.Builder(RightMainActivity.this);
                                    View dialogView2 = (View) View.inflate(getApplicationContext(), R.layout.ip_layout, null);
                                    ll = (LinearLayout) dialogView2.findViewById(R.id.ll);
                                    ip_Et1 = (EditText) dialogView2.findViewById(R.id.ip_Et1);

                                    dlg2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String ip_str = ip_Et1.getText().toString();
                                            ip = ip_str;
                                            ssocket = new SimpleSocket(ip, port, mHandler);
                                            ssocket.start();
                                        }
                                    });
                                    dlg2.setView(dialogView2);
                                    dlg2.show();
                                }
                            }, 300);
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });

        if (flag == true) {
            if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                gyroX = event.values[0] * 23;
                gyroY = event.values[1] * 23;
                gyroZ = event.values[2] * 23;

                if (gyroZ > 0.35f && gyroZ < 1.0f)
                    gyroZ = 1;
                else if (gyroZ > -1.0f && gyroZ < -0.35f)
                    gyroZ = -1;
                if (gyroX > 0.6f && gyroX < 1.0f)
                    gyroX = 1;
                else if (gyroX > -1.0f && gyroX < -0.6f)
                    gyroX = -1;

                if (Math.abs(accelXValue) < 2.5f) {
                    ssocket.sendString(Integer.toString(-1 * (int) (gyroZ)));
                    ssocket.sendString(Integer.toString(-1 * (int) (gyroX)));
                } else {
                    ssocket.sendString(Integer.toString(-1 * Math.round(gyroZ) * Math.round(Math.abs(accelXValue))));
                    ssocket.sendString(Integer.toString(-1 * Math.round(gyroX) * Math.round(Math.abs(accelYValue))));
                }
            }

            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                // 중력 데이터를 구하기 위해서 저속 통과 필터를 적용할 때 사용하는 비율 데이터.
                // t : 저속 통과 필터의 시정수. 시정수란 센서가 가속도의 63% 를 인지하는데 걸리는 시간
                // dT : 이벤트 전송율 혹은 이벤트 전송속도.
                // alpha = t / (t + Dt)
                final float alpha = (float) 0.8;

                // 저속 통과 필터를 적용한 중력 데이터를 구한다.
                // 직전 중력 값에 alpha 를 곱하고, 현재 데이터에 0.2 를 곱하여 두 값을 더한다.
                m_gravity_data[0] = alpha * m_gravity_data[0] + (1 - alpha) * event.values[0];
                m_gravity_data[1] = alpha * m_gravity_data[1] + (1 - alpha) * event.values[1];
                m_gravity_data[2] = alpha * m_gravity_data[2] + (1 - alpha) * event.values[2];

                // 현재 값에 중력 데이터를 빼서 가속도를 계산한다.
                m_accel_data[0] = event.values[0] - m_gravity_data[0];
                m_accel_data[1] = event.values[1] - m_gravity_data[1];
                m_accel_data[2] = event.values[2] - m_gravity_data[2];

                accelXValue = m_accel_data[0];
                accelYValue = m_accel_data[1];
                accelZValue = m_accel_data[2];
            }
        }
    }
}