package com.lrj.ptp.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import com.ab.util.AbStrUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lrj.ptp.R;
import com.lrj.ptp.adapters.MainAdapter;
import com.lrj.ptp.base.BaseActivity;
import com.lrj.ptp.base.BaseApplication;
import com.lrj.ptp.db.DbHelper;
import com.lrj.ptp.db.domain.PhotographType;
import com.lrj.ptp.http.Contacts;
import com.lrj.ptp.http.HttpHelper;
import com.lrj.ptp.photograph.AlbumItemAty;
import com.lrj.ptp.photograph.CameraContainer;
import com.lrj.ptp.photograph.utils.FileOperateUtil;
import com.lrj.ptp.utils.IMGUtils;
import com.lrj.ptp.utils.LOGUtils;
import com.lrj.ptp.utils.TypeDbInsert;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 包名：com.lrj.ptp.activitys
 * 描述：主界面
 * User 张伟
 * QQ:326093926
 * Date 2015/12/7 0007.
 * Time 上午 11:02.
 * 修改日期：
 * 修改内容：
 */
public class MainActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener {
    public static final int PX_NUMBER = 0x111;//拍照

    //头部
    private TextView head_title;
    private ImageView head_back;

    private TextView tv_title;//项的名字
    private TextView tv_hit;//当没有图片时显示
    private ImageView iv_show;
    private LinearLayout ll_sc_bg;//承载iv_show的父容器
    private ListView listview;
    private Button btn_save;
    private Button btn_shoot;
    private Button btn_upload;
    private String type;//选择的类型

    private MainAdapter adapter;
    private List<PhotographType> typeList = new ArrayList<PhotographType>();
    private CameraContainer cameraContainer;//保存图片类

    private boolean isps;//判断拍摄过照片 并且照片是拍摄成功的
    private DbHelper dbHelper;
    private HttpHelper httpHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        head_title = (TextView) findViewById(R.id.head_title);
        head_title.setText("拍照上传");
        head_back = (ImageView) findViewById(R.id.head_back);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_hit = (TextView) findViewById(R.id.tv_hit);
        iv_show = (ImageView) findViewById(R.id.iv_show);
        ll_sc_bg = (LinearLayout) findViewById(R.id.ll_sc_bg);
        listview = (ListView) findViewById(R.id.listview);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_shoot = (Button) findViewById(R.id.btn_shoot);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        btn_shoot.setOnClickListener(this);
        btn_upload.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        head_back.setOnClickListener(this);
        listview.setOnItemClickListener(this);
        iv_show.setOnClickListener(this);
        BaseApplication.getInstance().shootPathState = "0";

        cameraContainer = new CameraContainer(this);
        dbHelper = DbHelper.getInstance(this);
        httpHelper = HttpHelper.getInstance();
        //初始化左侧菜单
        initData();
        //显示图片
        getFileimg();
        if(typeList!=null&&typeList.size()>0) {
            tv_title.setText(typeList.get(0).typename);
        }
    }

    private void initData(){
        typeList = TypeDbInsert.getInstance().
                insert_credit(BaseApplication.getInstance().pathState + BaseApplication.getInstance().custPathState,
                        Integer.valueOf(BaseApplication.getInstance().creditPathState));
        adapter = new MainAdapter(this, typeList);
        listview.setAdapter(adapter);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(isps){
            //拍摄照片完成后执行
            isps = false;
            //修改数据库并显示左侧不同的类型 更新是否拍摄过
            updatePhoto(true);
        }else{
            //如果有删除操作 更新左侧菜单
            getFileimg();
        }

    }

    /**
     * 更新状态
     * @param isTrue 更新拍摄状态
     */
    private void updatePhoto(boolean isTrue){
        if(AbStrUtil.isEmpty(tv_title.getText().toString())){
            return;
        }
        for(int i =0 ;i<typeList.size();i++){
            if(typeList.get(i).typename.equals(tv_title.getText().toString())){
                typeList.get(i).istrue = isTrue;
                dbHelper.update(typeList.get(i),"istrue");
                break;
            }
        }
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }

    }


    /**
     * 调用拍照
     */
    private void intentPic(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory() + "/test.jpg");
        //下面这句指定调用相机拍照后的照片存储的路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(file));
        startActivityForResult(intent, PX_NUMBER);
    }

    /**
     * 判断是否必须拍摄的照片都已经拍摄完成
     * @return
     */
    private boolean isOver(){
        boolean isTrue = true;
        for(PhotographType type:typeList){
            //必拍项如果其中有没有拍摄的就不能提交
            if(type.typename.contains("*")&&!type.istrue){
                isTrue = false;
                break;
            }
        }
        return isTrue;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_show://点击进入图片详情
                Intent intent = new Intent(this, AlbumItemAty.class);
                startActivity(intent);
                break;
            case R.id.btn_save://保存
                //更新
                break;
            case R.id.btn_shoot://拍摄
                intentPic();
                break;
            case R.id.btn_upload://上传
                //暂时测试使用
                List<File> files = IMGUtils.getFileAll(this);
                shangchuan(files);

                //先判断是否所有的图片都拍摄完成
//                btn_upload.setEnabled(false);
//                if(isOver()) {
//                    //获取所有待上传的文件
//                    List<File> files = IMGUtils.getFileAll(this);
//                    if(files==null||files.size()==0){
//                        btn_upload.setEnabled(true);
//                        return;
//                    }else{
//                        //开线程开始上传 基本信息和图片
//                        shangchuan(files);
//                        btn_upload.setEnabled(true);
//                    }
//                }else{
//                    btn_upload.setEnabled(true);
//                    Toast.makeText(this,"请拍摄完所有带*必拍项",Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.head_back://返回
                finish();
                break;
        }
    }

    private void shangchuan(final List<File> files) {
        for(File file:files) {
            RequestParams requestParams = new RequestParams();
            requestParams.addBodyParameter("userid", BaseApplication.getInstance().pathState);
            requestParams.addBodyParameter("custname", BaseApplication.getInstance().custPathState);
            requestParams.addBodyParameter("image", file.getName());
            requestParams.addBodyParameter("file", file);
            //RequestCallBack
            httpHelper.sendTo(HttpRequest.HttpMethod.POST, Contacts.UPLOAD_ACTION, requestParams, new RequestCallBack<String>() {

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    LOGUtils.getLogger().i(s);
                }
            });
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BaseApplication.getInstance().shootPathState = position+"";
        tv_title.setText(typeList.get(position).typename);
        getFileimg();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PX_NUMBER://拍照返回的图片
                File file = new File(Environment.getExternalStorageDirectory() + "/test.jpg");
                if (file.exists()) {
                    if (file.length() == 0) {
                        file.delete();
                        Toast.makeText(this, "当前没有图片", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Bitmap bm = BitmapFactory.decodeFile(file.getPath());
                    Bitmap bitmap = ThumbnailUtils.extractThumbnail(bm, 300, 300);
                    if(bitmap!=null) {
                        iv_show.setImageBitmap(bitmap);
                        iv_show.setVisibility(View.VISIBLE);
                        tv_hit.setVisibility(View.GONE);
                    }
                    FileInputStream fileInputStream = null;
                    try {
                        fileInputStream = new FileInputStream(file);
                        byte[] bytes = new byte[fileInputStream.available()];
                        fileInputStream.read(bytes);
                        cameraContainer.save(bytes, BaseApplication.getInstance().shootPathState);
                        isps = true;
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

    /**
     * 如果找到文件就显示 找不到就不显示
     */
    private void getFileimg(){
        String path = FileOperateUtil.getFolderPath(this,BaseApplication.getInstance().shootPathState);
        File folder = new File(path);
        if (folder.exists()) {
            File[]  files = folder.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if(pathname.getName().contains(".jpg")){
                        return true;
                    }
                    return false;
                }
            });
            if(files!=null&&files.length>0){
                //解析生成相机返回的图片
                Bitmap bm = BitmapFactory.decodeFile(files[files.length-1].getPath());
                //生成缩略图
                Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bm, 300, 300);
                iv_show.setImageBitmap(thumbnail);
                iv_show.setVisibility(View.VISIBLE);
                tv_hit.setVisibility(View.GONE);
            }else{
                iv_show.setVisibility(View.GONE);
                tv_hit.setVisibility(View.VISIBLE);
                updatePhoto(false);
            }
        }else{
            iv_show.setVisibility(View.GONE);
            tv_hit.setVisibility(View.VISIBLE);
            updatePhoto(false);
        }
    }

}
