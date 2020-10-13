package vanliuzhi.org.common.starter.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.std.ToStringSerializer;

import java.util.Date;
import java.util.List;

/**
 * 用户
 *
 * @author Lys3415
 * @date 2020/10/12 16:13
 */
@Data
@TableName("sys_user")
@EqualsAndHashCode(callSuper = true)
public class SysUser extends Model<SysUser> {

    /**
     * 使用雪花算法生成，该属性序列化时转换为String
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String username;

    private String password;

    @TableField("nick_name")
    private String nickname;

    @TableField(value = "head_img_url")
    private String headImgUrl;

    private String phone;

    private Integer sex;

    /**
     * 用户是否启用，通过改变该值可，用启用/禁用用户(禁用的用户无法通过security的认证)
     */
    private Boolean enabled;

    private String type;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 以下字段不作为表字段存在
     **/

    @TableField(exist = false)
    private List<SysRole> roles;

    @TableField(exist = false)
    private String roleId;

    @TableField(exist = false)
    private String oldPassword;

    @TableField(exist = false)
    private String newPassword;
}
