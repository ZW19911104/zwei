package com.lrj.ptp.base;

import android.app.Dialog;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.lrj.ptp.R;

/**
 * 包名：com.lrj.ptp.base
 * 描述：dialog的基类
 * User 张伟
 * QQ:326093926
 * Date 2015/12/4 0004.
 * Time 上午 10:51.
 * 修改日期：
 * 修改内容：
 */
public class BaseDialog extends Dialog {
    private ImageView mFivIcon;
    private TextView mHtvText;
    private String mText;
    private Animation mLoadingAnim;
    private Context mContext;


    public BaseDialog(Context context,String text) {
        super(context, R.style.Theme_Light_FullScreenDialogAct);
        mText = text;
        mContext=context;
        initView();
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
    }

    private void initView() {
        setContentView(R.layout.view_loading);
        mFivIcon = (ImageView) findViewById(R.id.loadingdialog_fiv_icon);
        mHtvText = (TextView) findViewById(R.id.loadingdialog_htv_text);
        mLoadingAnim = AnimationUtils.loadAnimation(mContext, R.anim.loading_animation);
        mFivIcon.startAnimation(mLoadingAnim);
        mHtvText.setText(mText);
    }

    @Override
    public boolean isShowing() {
        // TODO Auto-generated method stub
        mFivIcon.startAnimation(mLoadingAnim);
        return super.isShowing();

    }

    public void setText(String text) {
        mText = text;
        mHtvText.setText(mText);
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            if(mFivIcon!=null)
                mFivIcon.clearAnimation();
            super.dismiss();
        }
    }


}
