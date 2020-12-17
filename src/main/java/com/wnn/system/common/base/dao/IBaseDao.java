package com.wnn.system.common.base.dao;

import tk.mybatis.mapper.common.*;


/**
 *  继承的两个类使用了tk.mapper的所有方法
 * @param <T>
 */
public interface IBaseDao<T> extends BaseMapper<T>, MySqlMapper<T>, IdsMapper<T> {
}