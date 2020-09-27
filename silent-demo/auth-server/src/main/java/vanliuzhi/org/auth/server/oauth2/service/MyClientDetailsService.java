package vanliuzhi.org.auth.server.oauth2.service;

import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import vanliuzhi.org.auth.server.oauth2.constants.Oauth2Constants;

import javax.sql.DataSource;

/**
 * 功能描述:
 *
 * @author Trazen
 * @date 2020/7/15 12:21
 */
public class MyClientDetailsService extends JdbcClientDetailsService {

    public MyClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * 重写原方法后续使用缓存处理,暂不处理
     * TODO
     */
    @Override
    //@Cacheable(value = "CLIENT_DETAILS_KEY",key = "#clientId",unless = "#result == null")
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        super.setSelectClientDetailsSql(Oauth2Constants.DEFAULT_SELECT_STATEMENT);
        return super.loadClientByClientId(clientId);
    }
}
