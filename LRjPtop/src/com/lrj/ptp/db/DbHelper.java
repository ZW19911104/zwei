package com.lrj.ptp.db;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

/**
 * 包名：com.example.DBAppDemo
 * 描述：数据库的操作
 * User 张伟
 * Date 2015/7/17 0017.
 * Time 上午 9:07.
 * 修改日期：
 * 修改内容：
 */
public class DbHelper implements DbUtils.DbUpgradeListener {
    private static DbHelper dbHelper;
    private final String dbname = "lrj.db";//数据库名称
    private int version;//数据库版本
    private DbUtils mDBClient;

    private DbHelper(Context context) {
        version = getVersionCode(context);
        mDBClient = DbUtils.create(context, dbname, version, this);
        mDBClient.configAllowTransaction(true);
        mDBClient.configDebug(true);
    }

    public static DbHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DbHelper(context);
        }
        return dbHelper;
    }

    @Override
    public void onUpgrade(DbUtils dbUtils, int i, int i1) {

    }

    /////////###########################################获取系统版本号###############################################/////////

    public int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            return info.versionCode;
        } catch (Exception e) {
            return 1;
        }
    }


    /////////###########################################开始了数据库的 增、删、改、查###############################################/////////

    /**
     * 插入单个对象
     *
     * @param entity 实体类的对象
     * @return true:插入成功 false:插入失败
     */
    public synchronized boolean save(Object entity) {
        try {
            mDBClient.save(entity);
        } catch (DbException e) {
            if (e != null)
                e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 插入全部对象
     *
     * @param entity 实体类的对象
     * @return true:插入成功 false:插入失败
     */
    public synchronized boolean saveAll(List<?> entity) {
        try {
            mDBClient.saveAll(entity);
        } catch (DbException e) {
            if (e != null)
                e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 删除这个表中的所有数据
     *
     * @param entity 实体类的对象
     * @return true:成功 false:失败
     */
    public synchronized boolean delete(Object entity) {
        try {
            mDBClient.delete(entity);
        } catch (Exception e) {
            if (e != null)
                e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据条件删除表
     *
     * @param entity 表名称
     * @param colun  列名
     * @param value  值
     * @return true:成功  false:失败
     */
    public synchronized boolean deleteCriteria(Class<?> entity, String colun, String value) {
        try {
            mDBClient.delete(entity, WhereBuilder.b(colun, "=", value));
        } catch (Exception e) {
            if (e != null)
                e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 更新这张表中的所有数据
     *
     * @param entity 实体类的对象
     * @return true:更新成功 false:更新失败
     */
    public synchronized boolean update(Object entity) {
        try {
            mDBClient.saveOrUpdate(entity);//先去查这个条数据 根据id来判断是存储还是更新 如果存在更新
        } catch (Exception e) {
            if (e != null)
                e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据参数更新表中的数据
     *
     * @param entity 实体类的对象
     * @param value  要更新的字段
     * @return true:更新成功 false:更新失败
     */
    public synchronized boolean update(Object entity, String... value) {
        try {
            mDBClient.update(entity, value);
        } catch (Exception e) {
            if (e != null)
                e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 查找 根据id
     *
     * @param cla 要查询的类
     * @param id  要查询的id
     * @return 查询到的数据
     */
    public synchronized <T> Object searchOne(Class<T> cla, int id) {
        try {
            return mDBClient.findFirst(Selector.from(cla).where(WhereBuilder.b("id", "=", id)));
        } catch (Exception e) {
            if (e != null)
                e.printStackTrace();
        }
        return null;
    }

    /**
     * 正叙查找 没有条件的
     *
     * @param entity 要查询的类
     * @param <T>    要查询的类
     * @return 查询到的数据
     */
    public synchronized <T> List<T> search(Class<T> entity) {
        try {
            return mDBClient.findAll(Selector.from(entity));
        } catch (Exception e) {
            if (e != null)
                e.printStackTrace();
        }
        return null;
    }

    /**
     * 倒叙查找所有数据 没有条件的
     *
     * @param entityClass
     * @return 返回数据库中所有的表数据
     */
    public synchronized <T> List<T> searchDesc(Class<T> entityClass) {
        try {
            return mDBClient.findAll(Selector.from(entityClass).orderBy("id", true));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 倒叙查找所有数据 没有条件的
     *
     * @param entityClass 实体类
     * @param column      定义的查询条件
     * @param value       定义的查询值
     * @return 返回数据库中所有的表数据
     */
    public synchronized <T> List<T> searchCriteria(Class<T> entityClass, String column, String value) {
        try {
            return mDBClient.findAll(Selector.from(entityClass).where(WhereBuilder.b(column, "=", value)));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 倒叙查找所有数据 没有条件的
     *
     * @param entityClass 实体类
     * @param column      定义的查询条件
     * @param value       定义的查询值
     * @return 返回数据库中所有的表数据
     */
    public synchronized <T> List<T> searchCriteria(Class<T> entityClass, String column, String value,String column2, String value2) {
        try {
            return mDBClient.findAll(Selector.from(entityClass).where(WhereBuilder.b(column, "=", value).and(column2,"=",value2)));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 删除表格
     *
     * @param entityClass 实体类
     * @return 返回数据库中所有的表数据
     */
    public synchronized <T> boolean drop(Class<T> entityClass) {
        try {
            mDBClient.dropTable(entityClass);
        } catch (Exception e) {
            return false;
        }
        return true;
    }



}
