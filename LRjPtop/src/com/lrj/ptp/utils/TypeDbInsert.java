package com.lrj.ptp.utils;

import com.lrj.ptp.R;
import com.lrj.ptp.base.BaseApplication;
import com.lrj.ptp.db.DbHelper;
import com.lrj.ptp.db.domain.PhotographType;

import java.util.ArrayList;
import java.util.List;

/**
 * 包名：com.lrj.ptp.utils
 * 描述：根据输入的不同类型来建立对应的表格 用于判断是否拍摄过照片
 * User 张伟
 * QQ:326093926
 * Date 2015/12/8 0008.
 * Time 上午 10:37.
 * 修改日期：
 * 修改内容：
 */
public class TypeDbInsert {
    private DbHelper dbHelper;
    private static TypeDbInsert insert;
    private TypeDbInsert(){
        dbHelper = DbHelper.getInstance(BaseApplication.getInstance());
    }

    public static TypeDbInsert getInstance(){
        if(insert == null){
            insert = new TypeDbInsert();
        }
        return insert;
    }

    /**
     * 数据库插入类型
     * @param usercust 根据业务员名和客户名生成
     * @param type 根据不同的类型来
     */
    public synchronized List<PhotographType> insert_credit(String usercust,int type){
        List<PhotographType> list = dbHelper.searchCriteria(PhotographType.class,"usercust",usercust,"type",type+"");
        if(list==null){
            list = new ArrayList<PhotographType>();
            String[] cd= null;
            switch (type){
                case 1://车贷
                    cd = BaseApplication.getInstance().getResources().getStringArray(R.array.automobile_credit);
                    break;
                case 2://房贷
                    cd = BaseApplication.getInstance().getResources().getStringArray(R.array.mortgage);
                    break;
                case 3://翼农贷三户联保贷
                case 4://翼农贷三户联保贷
                    cd = BaseApplication.getInstance().getResources().getStringArray(R.array.wing_agricultural_loan);
                    break;
                case 5://翼企贷
                    cd = BaseApplication.getInstance().getResources().getStringArray(R.array.wing_enterprise_credit);
                    break;
                case 6://翼商贷
                    cd = BaseApplication.getInstance().getResources().getStringArray(R.array.wing_business_credit);
                    break;
                default://

                    break;
            }

            for(int i=0;i<cd.length;i++){
                PhotographType photographType = new PhotographType();
                photographType.istrue =false;
                photographType.typename = cd[i];
                photographType.usercust = usercust;
                photographType.type = type;
                list.add(photographType);
            }
            dbHelper.saveAll(list);
        }
        return list;
    }




}
