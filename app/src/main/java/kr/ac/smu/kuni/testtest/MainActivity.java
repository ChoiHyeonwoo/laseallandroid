package kr.ac.smu.kuni.testtest;

/**
 * Created by Juseok Song on 2016-03-17.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    EditText ip_1;
    EditText ip_2;
    EditText ip_3;
    EditText ip_4;
    Button bt;
    RadioGroup RG;
    int modechk;
    TerminateApp terminateApp;

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        if (RG.getCheckedRadioButtonId() == R.id.lefthand) {
//            Toast.makeText(getApplicationContext(), "왼손모드 선택됨", Toast.LENGTH_SHORT).show();
//            modechk = 0;
//        }
        if (RG.getCheckedRadioButtonId() == R.id.righthand) {
            Toast.makeText(getApplicationContext(), "오른손모드 선택됨", Toast.LENGTH_SHORT).show();
            modechk = 1;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ip_1 = (EditText) findViewById(R.id.editText);
        ip_2 = (EditText) findViewById(R.id.editText2);
        ip_3 = (EditText) findViewById(R.id.editText3);
        ip_4 = (EditText) findViewById(R.id.editText4);
        bt = (Button) findViewById(R.id.bt);
        RG = (RadioGroup) findViewById(R.id.radio);
        RG.setOnCheckedChangeListener(this);
        terminateApp = new TerminateApp(this);

        ip_1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        ip_2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        ip_3.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        ip_4.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String ip_str = ip_1.getText().toString() + "." + ip_2.getText().toString() + "."
                        + ip_3.getText().toString() + "." + ip_4.getText().toString();
                if (modechk == 0) {
                    Log.i("onClick", "CallLeftActivity");
                    Intent intent = new Intent(MainActivity.this, LeftMainActivity.class);
                    intent.putExtra("ipAddress", ip_str);
                    startActivity(intent);
                    finish();
                } else if (modechk == 1) {
                    Log.i("onClick", "CallRightActivity");
                    Intent intent = new Intent(MainActivity.this, RightMainActivity.class);
                    intent.putExtra("ipAddress", ip_str);
                    startActivity(intent);
                    finish();
                }
            }
        });

        ip_1.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() == 3) {
                    ip_2.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        ip_2.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 3) {
                    ip_3.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });
        ip_3.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 2) {
                    ip_4.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });
        ip_4.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 3) {
                    ip_1.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        terminateApp.onBackPressed();
    }
}