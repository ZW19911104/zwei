package com.lrj.ptp.db.domain;

/**
 * 包名：com.lrj.ptp.db.domain
 * 描述：拍照类型
 * User 张伟
 * QQ:326093926
 * Date 2015/12/7 0007.
 * Time 下午 2:56.
 * 修改日期：
 * 修改内容：
 */
public class PhotographType {
    public int id;
    public String typename;//拍照的类型的名称
    public boolean istrue;//是否拍照完成
    public String usercust;//用于判断属于哪个业务员的哪个用户
    public int type = 0;//贷款类型
}
