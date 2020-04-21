// package com.vanliuzhi.org.custom.log.dao;
//
// import com.open.capacity.common.model.SysLog;
// import org.apache.ibatis.annotations.Insert;
// import org.apache.ibatis.annotations.Mapper;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//
// import javax.sql.DataSource;
//
// /**
//  * @Description 保存日志
//  * eureka-server配置不需要datasource,不会装配bean
//  * 需要配置多数据源才可以支持
//  * @Author VanLiuZhi
//  * @Date 2020-04-19 23:48
//  */
// @Mapper
// @ConditionalOnBean(DataSource.class)
// public interface LogDao {
//
//     @Insert("insert into sys_log(username, module, params, remark, flag, createTime) values(#{username}, #{module}, #{params}, #{remark}, #{flag}, #{createTime})")
//     int save(SysLog log);
//
// }
