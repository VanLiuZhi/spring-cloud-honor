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
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import vanliuzhi.org.auth.service.json.Jackson2Mapper;
import vanliuzhi.org.auth.service.json.JacksonMapper;
import vanliuzhi.org.auth.service.json.JsonMapper;
import vanliuzhi.org.auth.service.json.NotSupportedJsonMapper;
import vanliuzhi.org.common.starter.auth.details.DefaultClientDetails;
import vanliuzhi.org.common.starter.constant.AuthConstant;
import vanliuzhi.org.common.starter.utils.CommonStringUtil;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author Lys3415
 * @date 2020/10/16 17:02
 */
@Slf4j
public class RedisClientDetailsService extends JdbcClientDetailsService {

    private static final String JACKSON_OLD = "org.codehaus.jackson.map.ObjectMapper";
    private static final String JACKSON_NEW = "org.codehaus.jackson.map.ObjectMapper";
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

    /**
     * jsonMap 适配
     */
    private static JsonMapper createJsonMapper() {
        // jackson旧类型
        if (ClassUtils.isPresent(JACKSON_OLD, null)) {
            return new JacksonMapper();
        }
        // jackson新类型
        if (ClassUtils.isPresent(JACKSON_NEW, null)) {
            return new Jackson2Mapper();
        }
        return new NotSupportedJsonMapper();
    }

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 核心方法，通过客户端id加载客户端
     */
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        ClientDetails clientDetails;
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

    /**
     * 更新客户端后，重写缓存
     */
    @Override
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
        super.updateClientDetails(clientDetails);
        cacheAndGetClient(clientDetails.getClientId());
    }

    /**
     * 更新secret后，重写缓存
     */
    @Override
    public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
        super.updateClientSecret(clientId, secret);
        cacheAndGetClient(clientId);
    }

    /**
     * 删除后Client，重写缓存
     */
    @Override
    public void removeClientDetails(String clientId) throws NoSuchClientException {
        super.removeClientDetails(clientId);
        removeRedisCache(clientId);
    }

    /**
     * 将oauth_client_details全表刷入redis
     */
    public void loadAllClientToCache() {
        if (redisTemplate.hasKey(AuthConstant.CACHE_CLIENT_KEY)) {
            return;
        }
        log.info("将oauth_client_details全表刷入redis");
        // 获取全部客户端数据，通过 findClientDetailsSql 查询到
        List<ClientDetails> list = this.listClientDetails();
        if (CollectionUtils.isEmpty(list)) {
            log.error("oauth_client_details表数据为空，请检查");
            return;
        }
        list.parallelStream().forEach(client -> {
            redisTemplate.boundHashOps(AuthConstant.CACHE_CLIENT_KEY)
                    .put(client.getClientId(), JSONObject.toJSONString(client));
        });
    }

    /**
     * 删除redis缓存
     */
    private void removeRedisCache(String clientId) {
        redisTemplate.boundHashOps(AuthConstant.CACHE_CLIENT_KEY).delete(clientId);
    }

    /**
     * 追加if_limit  limit_count
     */
    @Override
    public List<ClientDetails> listClientDetails() {
        return  jdbcTemplate.query(SELECT_FIND_STATEMENT,  new ClientDetailsRowMapper());
    }

    /**
     * 重新定义ClientDetailsRowMapper
     */
    private static class ClientDetailsRowMapper implements RowMapper<ClientDetails> {

        private final JsonMapper mapper = createJsonMapper();

        @Override
        public ClientDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
            DefaultClientDetails details = new DefaultClientDetails(
                    rs.getString(1), rs.getString(3), rs.getString(4),
                    rs.getString(5), rs.getString(7), rs.getString(6));
            details.setClientSecret(rs.getString(2));
            // 设置刷新token的过期时间
            if (rs.getObject(8) != null) {
                details.setAccessTokenValiditySeconds(rs.getInt(8));
            }
            if (rs.getObject(9) != null) {
                details.setRefreshTokenValiditySeconds(rs.getInt(9));
            }
            String json = rs.getString(10);
            if (json != null) {
                try {
                    Map<String, Object> additionalInformation = mapper.read(json, Map.class);
                    // 设置additional information（额外信息）
                    details.setAdditionalInformation(additionalInformation);
                } catch (Exception e) {
                    log.warn("Could not decode JSON for additional information: " + details, e);
                }
            }
            String scopes = rs.getString(11);
            long ifLimit = rs.getLong(12);
            details.setIfLimit(ifLimit);
            long limitCount = rs.getLong(13);
            details.setLimitCount(limitCount);
            details.setId(rs.getLong(14));
            if (scopes != null) {
                details.setAutoApproveScopes(org.springframework.util.StringUtils.commaDelimitedListToSet(scopes));
            }
            return details;
        }
    }
}
