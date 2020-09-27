package vanliuzhi.org.auth.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vanliuzhi.org.auth.server.entity.SysUser;
import vanliuzhi.org.auth.server.mapper.SysUserMapper;
import vanliuzhi.org.auth.server.service.ISysUserService;
import org.springframework.stereotype.Service;

/**
 * 功能描述: 
 * @author  Trazen
 * @date  2020/7/14 14:56
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
}
