package com.vanliuzhi.org.common.module.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 角色
 * @Author VanLiuZhi
 * @Date 2020-04-20 14:34
 */
@Data
public class SysRole implements Serializable {

    private static final long serialVersionUID = 4497149010220586111L;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String code;
    private String name;
    private Date createTime;
    private Date updateTime;
    private Long userId;

}
