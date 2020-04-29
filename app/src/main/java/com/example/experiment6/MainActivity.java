package com.example.experiment6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;

import java.util.List;
import java.util.Map;


import com.example.experiment6.RegexUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    public static final int PASS_0 = 0;
    public static final int PASS_1 = 1;
    private TextInputLayout layoutTNumber;
    private EditText TextNumber;
    private Button Button;
    private Button Button1;
    private Button Button2;
    private TextView TextView2;
    private PhoneUtil PhoneUtil;
    private Map<String, String> phoneMap;
    private List<PhoneDto> phoneDtos;
    private String number;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button = findViewById(R.id.button);
        Button1 = findViewById(R.id.button2);
        Button2 = findViewById(R.id.button3);
        TextView2=findViewById(R.id.textView2);

        layoutTNumber = findViewById(R.id.TextName);
        TextNumber = layoutTNumber.getEditText();

        //获取通讯录权限
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS},4);
            Log.d(TAG, "onCreate: 获取通讯录权限");
        } else{
            PhoneUtil=new PhoneUtil(MainActivity.this);
            phoneDtos=PhoneUtil.getPhone();
            phoneMap=new HashMap<String, String>();
            int i=0;
            for( PhoneDto PhoneDto:phoneDtos){
                phoneMap.put(PhoneDto.getTelPhone().replace(" ", ""),PhoneDto.getName());
            }
        }
        TextNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                number=s.toString().replace(" ", "");
                if (!RegexUtils.isTel(number.toString())&&!RegexUtils.isMobileExact(number.toString())) {
                    //显示错误提示
                   // if(!RegexUtils.isMobileExact(s.toString())){
                        layoutTNumber.setError("格式不正确");
                        layoutTNumber.setErrorEnabled(true);
                   // }

                } else {
                    Button.setEnabled(Boolean.TRUE);
                    layoutTNumber.setErrorEnabled(false);
                    int i;
                    if(PhoneUtil!=null){

                          if(phoneMap.containsKey(number)){
                             // layoutTNumber.setError(phoneMap.get(s.toString()));
                             // TextNumber.setText(TextNumber.getText()+"      "+phoneMap.get(s.toString()));
                              TextView2.setText("联系人 : "+phoneMap.get(number));
                          }



                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //设置打电话按钮不能点击
        Button.setEnabled(Boolean.FALSE);
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = TextNumber.getText().toString();
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + number);
                intent.setData(data);

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 3);
                }
                startActivity(intent);

            }
        });


        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,MailListActivity.class);
                startActivityForResult(intent, PASS_0);
            }
        });

        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ShortMessageActivity.class);
                Log.d(TAG, "onClick:number1");
                if(number!=null&&!number.equals("")){
                    Log.d(TAG, "onClick:number");
                    intent.putExtra("Number", number);
                }
                startActivityForResult(intent, PASS_1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        //Log.d(TAG, "onActivityResult: 返回");
        if(requestCode==PASS_0 && resultCode==RESULT_OK){
            Bundle bundle=intent.getExtras();
            if(!bundle.equals("")){
                TextNumber.setText(bundle.getString("TelPhone"));
                TextView2.setText(bundle.getString("Name"));

            }
        }
    }

    //获取权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 3:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: 获取电话权限成功");
                } else {
                    Toast.makeText(this, "电话权限被拒绝了", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onRequestPermissionsResult: 获取电话权限失败");
                }
                break;
            case 4:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: 获取通讯录权限成功");
                    PhoneUtil = new PhoneUtil(MainActivity.this);
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: 获取通讯录权限失败");
                }
                break;
            default:
                break;

        }
    }
    @Override
    public void onClick(View v) {

    }
}
