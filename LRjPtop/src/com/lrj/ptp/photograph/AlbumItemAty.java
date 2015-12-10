package com.lrj.ptp.photograph;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.lrj.ptp.R;
import com.lrj.ptp.base.BaseActivity;
import com.lrj.ptp.base.BaseApplication;
import com.lrj.ptp.photograph.utils.FileOperateUtil;
import com.lrj.ptp.photograph.views.AlbumViewPager;
import com.lrj.ptp.photograph.views.MatrixImageView.OnSingleTapListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 包名：com.lrj.ptp.photograph
 * 描述：相册图片大图Activity 包含图片编辑功能
 * User 张伟
 * QQ:326093926
 * Date 2015/12/3 0003.
 * Time 上午 9:44.
 * 修改日期：
 * 修改内容：
 */
public class AlbumItemAty extends BaseActivity implements View.OnClickListener,OnSingleTapListener {
    private AlbumViewPager mViewPager;//显示大图
    private ImageView mBackView;
    private ImageView mCameraView;
    private TextView mCountView;
    private View mHeaderBar,mBottomBar;
    private Button mDeleteButton;
    private Button mEditButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.albumitem);

        mViewPager=(AlbumViewPager)findViewById(R.id.albumviewpager);
        mBackView=(ImageView)findViewById(R.id.header_bar_photo_back);
        mCameraView=(ImageView)findViewById(R.id.header_bar_photo_to_camera);
        mCountView=(TextView)findViewById(R.id.header_bar_photo_count);
        mHeaderBar=findViewById(R.id.album_item_header_bar);
        mBottomBar=findViewById(R.id.album_item_bottom_bar);
        mDeleteButton=(Button)findViewById(R.id.delete);
        mEditButton=(Button) findViewById(R.id.edit);

        mBackView.setOnClickListener(this);
        mCameraView.setOnClickListener(this);
        mCountView.setOnClickListener(this);
        mDeleteButton.setOnClickListener(this);
        mEditButton.setOnClickListener(this);

        mViewPager.setOnPageChangeListener(pageChangeListener);
        mViewPager.setOnSingleTapListener(this);
        String currentFileName=null;
        if(getIntent().getExtras()!=null)
            currentFileName=getIntent().getExtras().getString("path");
        if(currentFileName!=null){
            File file=new File(currentFileName);
            currentFileName=file.getName();
            if(currentFileName.indexOf(".")>0)
                currentFileName=currentFileName.substring(0, currentFileName.lastIndexOf("."));
        }

        loadAlbum(BaseApplication.getInstance().shootPathState,currentFileName);
    }

    public void loadAlbum(String rootPath,String fileName){
        //获取根目录下缩略图文件夹
        String folder= FileOperateUtil.getFolderPath(this, rootPath);
        //获取图片文件大图
        List<File> imageList=FileOperateUtil.listFiles(folder, ".jpg");

        FileOperateUtil.sortList(imageList, false);
        if(imageList.size()>0){
            List<String> paths=new ArrayList<String>();
            int currentItem=0;
            for (File file : imageList) {
                if(fileName!=null&&file.getName().contains(fileName))
                    currentItem=imageList.indexOf(file);
                paths.add(file.getAbsolutePath());
            }
            mViewPager.setAdapter(mViewPager.new ViewPagerAdapter(paths));
            mViewPager.setCurrentItem(currentItem);
            mCountView.setText((currentItem+1)+"/"+paths.size());
        }
        else {
            mCountView.setText("0/0");
        }
    }

    private ViewPager.OnPageChangeListener pageChangeListener=new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            if(mViewPager.getAdapter()!=null){
                String text=(position+1)+"/"+mViewPager.getAdapter().getCount();
                mCountView.setText(text);
            }else {
                mCountView.setText("0/0");
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }
    };

    @Override
    public void onSingleTap() {
        if(mHeaderBar.getVisibility()==View.VISIBLE){
            AlphaAnimation animation=new AlphaAnimation(1, 0);
            animation.setDuration(300);
            mHeaderBar.startAnimation(animation);
            mBottomBar.startAnimation(animation);
            mHeaderBar.setVisibility(View.GONE);
            mBottomBar.setVisibility(View.GONE);
        }else {
            AlphaAnimation animation=new AlphaAnimation(0, 1);
            animation.setDuration(300);
            mHeaderBar.startAnimation(animation);
            mBottomBar.startAnimation(animation);
            mHeaderBar.setVisibility(View.VISIBLE);
            mBottomBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_bar_photo_back:
                finish();
                break;
            case R.id.delete:
                if(mViewPager.getChildCount()>0){
                    String result=mViewPager.deleteCurrentPath();
                    if(result!=null)
                        mCountView.setText(result);
                }
                break;
            default:
                break;
        }
    }


}
