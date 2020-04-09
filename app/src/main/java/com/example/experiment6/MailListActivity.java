package com.example.experiment6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MailListActivity extends AppCompatActivity {
    private static final String TAG = "MailListActivity";
    private ListView listview;
    private List<PhoneDto> phoneDtos;
    private PhoneUtil PhoneUtil;
    private List<String> contactsList=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_list);
        //Log.d(TAG, "onCreate: 进入");
        listview=findViewById(R.id.listview);
        //获取通讯录
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MailListActivity.this,new String[]{Manifest.permission.READ_CONTACTS},4);
            // Log.d(TAG, "onCreate: 获取通讯录权限");
        } else{
            PhoneUtil=new PhoneUtil(MailListActivity.this);
            phoneDtos=PhoneUtil.getPhone();
            int i=0;

            for( PhoneDto PhoneDto:phoneDtos){
                contactsList.add("   "+PhoneDto.getName()+"\n\n"+"            "+PhoneDto.getTelPhone()+"\n\n");
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MailListActivity.this, android.R.layout.simple_list_item_1,contactsList);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Toast.makeText(MailListActivity.this,phoneDtos.get(position).getName(), Toast.LENGTH_SHORT).show();
                Intent intent =getIntent();
                Bundle bundle =new Bundle();
                bundle.putString("Name",phoneDtos.get(position).getName());
                bundle.putString("TelPhone",phoneDtos.get(position).getTelPhone());
                intent.putExtras(bundle);
               // Log.d(TAG, "onClick: Button3 ");
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case 3:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "电话权限被拒绝了", Toast.LENGTH_LONG).show();
                }
                break;
            case 4:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: 获取通讯录权限成功");
                    PhoneUtil = new PhoneUtil(MailListActivity.this);
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: 获取通讯录权限失败");
                }
                break;
            default:
                break;

        }
    }
}
