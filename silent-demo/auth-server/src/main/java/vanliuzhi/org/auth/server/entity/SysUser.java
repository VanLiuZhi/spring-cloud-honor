package vanliuzhi.org.auth.server.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author VanLiuZhi
 * @date 2020/9/27 14:42
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_user")
public class SysUser implements Serializable {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String username;

    private String password;

    private String email;

    private String mobile;

    private Boolean delFlag;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modifiedTime;

    private Long modifiedUser;

}
