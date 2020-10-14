package vanliuzhi.org.auth.client.service;

import java.util.List;
import java.util.Map;

/**
 * @author Lys3415
 * @date 2020/10/14 14:01
 */
public interface SysClientService {
    /**
     * 根据客户端id获取客户端详情数据
     *
     * @param clientId
     * @return
     */
    public Map getClients(String clientId);

    /**
     * @param clientId
     * @return
     */
    List<Map> listByClientId(String clientId);
}
