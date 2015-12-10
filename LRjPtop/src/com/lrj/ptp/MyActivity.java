package com.lrj.ptp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import cn.jpush.android.api.JPushInterface;
import com.ab.util.AbStrUtil;
import com.lrj.ptp.activitys.MainActivity;
import com.lrj.ptp.base.BaseActivity;
import com.lrj.ptp.base.BaseApplication;
import com.lrj.ptp.photograph.CameraContainer;

import java.io.*;

public class MyActivity extends BaseActivity {
    public static final int PX_NUMBER = 0x111;//拍照
    public static final int LP_NUMBER = 0x112;//录屏

    private CameraContainer container;

    private EditText msgText;
    private EditText msgTexts;
    private Spinner spinner;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        msgText = (EditText) findViewById(R.id.msg_rec);
        msgTexts = (EditText) findViewById(R.id.msg_recs);
        spinner = (Spinner) findViewById(R.id.spinner);
        registerMessageReceiver();
        adapt();

        container = new CameraContainer(this);//test表示的是用户id 以后拍完照片后 根据用户id的不同来存储不同位置

    }

    private void adapt(){
        //将可选内容与ArrayAdapter连接起来
        ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.list_blx));
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter添加到spinner中
        spinner.setAdapter(adapter);
        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
    }

    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            BaseApplication.getInstance().creditPathState=arg2+"";//记录当前贷款类型
        }
        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }


    public void paizhao(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory() + "/test.jpg");
        //下面这句指定调用相机拍照后的照片存储的路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(file));
        startActivityForResult(intent, PX_NUMBER);
    }

    public void zhujiemain(View view){
        String ss = msgText.getText().toString();
        if(ss.trim().equals("")){
            showCustomToast("请输入用户名");
            return;
        }
        String sss = msgTexts.getText().toString();
        if(sss.trim().equals("")){
            showCustomToast("请输入客户名");
            return;
        }
        if(AbStrUtil.isEmpty(BaseApplication.getInstance().creditPathState)||BaseApplication.getInstance().creditPathState.equals("0")){
            showCustomToast("请选择贷款类型");
            return;
        }

        BaseApplication.getInstance().pathState = ss.trim();
        BaseApplication.getInstance().custPathState = sss.trim();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



    public void locat(View view){
        Toast.makeText(this,"经度："+ BaseApplication.getInstance().longitude+"，纬度："+BaseApplication.getInstance().latitude,Toast.LENGTH_SHORT).show();
    }

    public void initJpush(View view){
        JPushInterface.init(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PX_NUMBER://拍照返回的图片
                File file = new File(Environment.getExternalStorageDirectory() + "/test.jpg");
                if (file.exists()) {
                    if (file.length() == 0) {
                        file.delete();
                        Toast.makeText(this, "当前没有图片", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    FileInputStream fileInputStream = null;
                    try {
                        fileInputStream = new FileInputStream(file);
                        byte[] bytes = new byte[fileInputStream.available()];
                        fileInputStream.read(bytes);
                        container.save(bytes,"");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            file.delete();
                        }
                    }
                } else {
                    Toast.makeText(this, "当前文件不存在", Toast.LENGTH_SHORT).show();
                }
                break;

            default:

                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
//        JPushInterface.onResume(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
//        JPushInterface.onPause(this);
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        BaseApplication.getInstance().mLocationClient.stop();
        super.onDestroy();
    }


    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static boolean isForeground = false;
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!AbStrUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                setCostomMsg(showMsg.toString());
            }
        }
    }

    private void setCostomMsg(String msg){
        if (null != msgText) {
            msgText.setText(msg);
            msgText.setVisibility(android.view.View.VISIBLE);
        }
    }


}
