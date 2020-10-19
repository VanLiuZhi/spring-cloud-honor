package vanliuzhi.org.common.starter.auth.details;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

/**
 * @author Lys3415
 * @date 2020/10/19 11:31
 */
@Data
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DefaultClientDetails extends BaseClientDetails {
    private long id ;
    //限流标识
    private long ifLimit;
    //限流次数
    private long limitCount;

    public DefaultClientDetails(String clientId, String resourceIds, String scopes,
                                String grantTypes, String authorities, String redirectUris) {
        super(clientId, resourceIds, scopes, grantTypes, authorities, redirectUris);
    }
}
