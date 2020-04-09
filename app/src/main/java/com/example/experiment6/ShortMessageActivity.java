package com.example.experiment6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
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

public class ShortMessageActivity extends AppCompatActivity {
    private static final String TAG = "ShortMessageActivity";
    private TextView TextView2;
    private TextInputLayout layoutName;
    private TextInputLayout layoutMessage;
    private EditText TextName;
    private EditText TextMessage;
    private Button Button;
    private PhoneUtil PhoneUtil;
    private Map<String, String> phoneMap;
    private List<PhoneDto> phoneDtos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_message);
        TextView2=findViewById(R.id.textView2);
        layoutName=findViewById(R.id.TextName);
        layoutMessage=findViewById(R.id.TextName1);
        Button=findViewById(R.id.button);
        TextName = layoutName.getEditText();
        TextMessage = layoutMessage.getEditText();
        //设置发送按钮不能点击
        Button.setEnabled(Boolean.FALSE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    ShortMessageActivity.this,new String[]{Manifest.permission.READ_CONTACTS},4);
            // Log.d(TAG, "onCreate: 获取通讯录权限");
        } else{
            PhoneUtil=new PhoneUtil(ShortMessageActivity.this);
            phoneDtos=PhoneUtil.getPhone();
            phoneMap=new HashMap<String, String>();
            for( PhoneDto PhoneDto:phoneDtos){
                phoneMap.put(PhoneDto.getTelPhone().replace(" ", ""),PhoneDto.getName());
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    ShortMessageActivity.this,new String[]{Manifest.permission.SEND_SMS},3);
             Log.d(TAG, "onCreate: 获取发送短信权限");
        }
        //接受传来的信息
        Intent intent =getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null&&!bundle.equals("")){
            String  Number =bundle.getString("Number");
            TextName.setText(Number);
            if(PhoneUtil!=null){

                if(phoneMap.containsKey(Number)){
                    // layoutTNumber.setError(phoneMap.get(s.toString()));
                    // TextNumber.setText(TextNumber.getText()+"      "+phoneMap.get(s.toString()));
                    TextView2.setText("联系人 : "+phoneMap.get(Number));
                }

            }
        }
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=TextName.getText().toString();
                String message=TextMessage.getText().toString();
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phone, null, message, null, null);
                    Toast.makeText(ShortMessageActivity.this, "发送成功", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(ShortMessageActivity.this, "发送失败，请再次尝试",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        });

        //输入控制
        TextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String number=s.toString().replace(" ", "");
                if (number.length() != 11) {
                    //显示错误提示
                    layoutName.setError("格式不正确");
                    layoutName.setErrorEnabled(true);
                } else {
                    Button.setEnabled(Boolean.TRUE);
                    layoutName.setErrorEnabled(false);
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

        TextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() >= 100) {
                    //显示错误提示

                    layoutMessage.setError("输入字符不能大于70个");
                    layoutMessage.setErrorEnabled(true);
                } else {
                    Button.setEnabled(Boolean.TRUE);
                    layoutMessage.setErrorEnabled(false);

                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }



    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 3:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: 发送短信权限获取成功");
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: 发送短信权限获取失败");
                }
                break;
            case 4:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: 获取通讯录权限成功");
                    PhoneUtil = new PhoneUtil(ShortMessageActivity.this);
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: 获取通讯录权限失败");
                }
                break;
            default:
                break;

        }
    }
}
