package com.dysen.opencard.common;

import android.content.Context;

import com.dysen.opencard.base.BaseApplication;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.litesuits.orm.db.model.ColumnsValue;
import com.litesuits.orm.db.model.ConflictAlgorithm;

import java.util.List;

/**
 * Created by hutian on 2018/6/22.
 * 数据库管理工具类
 */

public class DataBaseManager {

	private static LiteOrm liteOrm;
	private static Context mContext = BaseApplication.getAppContext();

	public DataBaseManager() {
		if(liteOrm==null) {
			liteOrm = LiteOrm.newCascadeInstance(mContext, "PrinterInfo.db");
		}
	}
	public static LiteOrm getLiteOrm() {
		return liteOrm;
	}
	private static DataBaseManager ourInstance;

	//单例模式
	public static DataBaseManager getInstance() {
		if (ourInstance == null) {
			ourInstance = new DataBaseManager();
		}
		return ourInstance;
	}
	public void save(Object o) {
		if (o == null) {
			return;
		}

		liteOrm.save(o);
	}

	public <T> void save(List<T> collection) {
		if (collection ==null||collection.size()==0) {
			return;
		}

		liteOrm.save(collection);
	}
	/**
	 * 插入一条记录
	 *
	 * @param t
	 */
	public <T> long insert(T t) {
		return liteOrm.save(t);
	}

	public <T> long insertOrReplace(T t) {
		return liteOrm.insert(t, ConflictAlgorithm.Replace);
	}

	/**
	 * 插入所有记录
	 *
	 * @param list
	 */
	public <T> void insertAll(List<T> list) {
		liteOrm.save(list);
	}

	public <T> void insertOrReplaceAll(List<T> list) {
		liteOrm.insert(list, ConflictAlgorithm.Ignore);
	}

	/**
	 * 查询所有
	 *
	 * @param cla
	 * @return
	 */
	public <T> List<T> getQueryAll(Class<T> cla) {
		return liteOrm.query(cla);
	}

	/**
	 * 查询  某字段 等于 Value的值
	 *
	 * @param cla
	 * @param field
	 * @param value
	 * @return
	 */
	public <T> List<T> getQueryByWhere(Class<T> cla, String field, Object[] value) {
		return liteOrm.<T>query(new QueryBuilder(cla).where(field + "=?", value));
	}

	/**
	 * 查询  某字段 等于 Value的值  可以指定从1-20，就是分页
	 *
	 * @param cla
	 * @param field
	 * @param value
	 * @param start
	 * @param length
	 * @return
	 */
	public <T> List<T> getQueryByWhereLength(Class<T> cla, String field, String[] value, int start, int length) {
		return liteOrm.<T>query(new QueryBuilder(cla).where(field +"=?", value).limit(start, length));
	}

	/**
	 * 删除所有 某字段等于 Vlaue的值
	 *
	 * @param cla
	 * @param field
	 * @param value
	 */
	public <T> void deleteWhere(Class<T> cla, String field, String value) {
		liteOrm.delete(cla, WhereBuilder.create(cla).where(field + "=?", value));
	}

	/**
	 * 删除所有
	 *
	 * @param cla
	 */
	public <T> void deleteAll(Class<T> cla) {
		liteOrm.deleteAll(cla);
	}

	/**
	 * 仅在以存在时更新
	 *
	 * @param t
	 */
	public <T> void update(T t) {
		liteOrm.update(t, ConflictAlgorithm.Replace);
	}

		public <T> void updateByWhere(Class<T> cla, String where, String whereArgs, String colume, Object value) {
		int result = liteOrm.update(WhereBuilder.create(cla).where(where + "=?", new String[]{whereArgs}), new ColumnsValue(new String[]{colume}, new Object[]{value}), ConflictAlgorithm.Replace);
	}

	public <T> void updateALL(List<T> list) {
		liteOrm.update(list);
	}

}
