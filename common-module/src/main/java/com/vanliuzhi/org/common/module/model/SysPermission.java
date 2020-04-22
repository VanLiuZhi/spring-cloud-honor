package com.vanliuzhi.org.common.module.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @Description 权限标识
 * @Author VanLiuZhi
 * @Date 2020-04-20 14:34
 */
@Data
@TableName("sys_permission")
@EqualsAndHashCode(callSuper = true)
public class SysPermission extends Model<SysPermission> implements Serializable {

    private static final long serialVersionUID = 1389727646460449239L;
    @TableId(value = "id", type = IdType.ID_WORKER)  //雪花算法  id生成策略
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String permission;
    private String name;
    @TableField(value = "createTime")
    private Date createTime;
    @TableField(value = "updateTime")
    private Date updateTime;
    @TableField(exist = false)
    private Long roleId;
    @TableField(exist = false)
    private Set<Long> authIds;

}
