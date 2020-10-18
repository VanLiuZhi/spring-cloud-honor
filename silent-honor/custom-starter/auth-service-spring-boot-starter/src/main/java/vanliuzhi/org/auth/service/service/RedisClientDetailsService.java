package vanliuzhi.org.auth.service.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import vanliuzhi.org.common.starter.constant.AuthConstant;
import vanliuzhi.org.common.starter.utils.CommonStringUtil;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Lys3415
 * @date 2020/10/16 17:02
 */
@Slf4j
public class RedisClientDetailsService extends JdbcClientDetailsService {

    private static final String SELECT_CLIENT_DETAILS_SQL = "" +
            "select client_id, client_secret, resource_ids, scope, authorized_grant_types, " +
            "web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, " +
            "additional_information, autoapprove ,if_limit, limit_count ,id " +
            "from oauth_client_details where client_id = ? and status = 1  ";
    /**
     * 扩展 默认的 ClientDetailsService, 增加逻辑删除判断( status = 1)
     */
    private static final String SELECT_FIND_STATEMENT = "" +
            "select client_id, client_secret,resource_ids, scope, " +
            "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, " +
            "refresh_token_validity, additional_information, autoapprove ,if_limit, limit_count ,id " +
            "from oauth_client_details where status = 1 order by client_id ";

    private final JdbcTemplate jdbcTemplate;

    private RedisTemplate<String, Object> redisTemplate;

    public RedisClientDetailsService(DataSource dataSource) {
        super(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.setSelectClientDetailsSql(SELECT_CLIENT_DETAILS_SQL);
        this.setFindClientDetailsSql(SELECT_FIND_STATEMENT);
    }

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 核心方法，通过客户端id加载客户端
     *
     * @param clientId
     * @return
     * @throws InvalidClientException
     */
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        ClientDetails clientDetails = null;
        String value = (String) redisTemplate.boundHashOps(AuthConstant.CACHE_CLIENT_KEY).get(clientId);
        try {
            if (CommonStringUtil.isBlank(value)) {
                clientDetails = cacheAndGetClient(value);
            } else {
                clientDetails = JSONObject.parseObject(value, BaseClientDetails.class);
            }
        } catch (Exception e) {
            log.error("clientId:{},{}", clientId, clientId);
            throw new InvalidClientException("应用获取失败") {
            };
        }
        return clientDetails;
    }

    /**
     * 缓存clientId 并返回
     * 该方法会先从数据库获取客户端，如果存在则写入缓存
     */
    private ClientDetails cacheAndGetClient(String clientId) {
        ClientDetails clientDetails;
        try {
            clientDetails = jdbcTemplate.queryForObject(SELECT_CLIENT_DETAILS_SQL, new ClientDetailsRowMapper(), clientId);
            if (clientDetails != null) {
                // 写入Redis缓存
                redisTemplate.boundHashOps(AuthConstant.CACHE_CLIENT_KEY).put(clientId, JSONObject.toJSONString(clientDetails));
                log.info("缓存clientId:{},{}", clientId, clientDetails);
            }
        } catch (EmptyResultDataAccessException | NoSuchClientException e) {
            log.error("clientId:{},{}", clientId, clientId);
            throw new AuthenticationException("应用不存在") {
            };
        } catch (InvalidClientException e) {
            throw new AuthenticationException("应用状态不合法") {
            };
        }
        return clientDetails;
    }

    private static class ClientDetailsRowMapper implements RowMapper<ClientDetails> {

        @Override
        public ClientDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
            return null;
        }
    }
}
