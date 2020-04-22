package com.vanliuzhi.org.custom.resource.server.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Description 查询应用绑定的资源权限
 * @Author VanLiuZhi
 * @Date 2020-04-20 14:34
 */
@Mapper
@SuppressWarnings("all")
public interface SysServiceDao {

    // 通过client id 查询出对应的 服务记录
	@Select("select p.* from sys_service p inner join sys_client_service rp on p.id = rp.serviceId where rp.clientId = #{clientId} order by p.sort")
    List<Map> listByClientId(Long clientId);

}
