package vanliuzhi.org.common.starter.constant;

/**
 * @author Lys3415
 * @date 2020/10/14 16:52
 */
public class AuthConstant {
    /**
     * 缓存client的redis key，这里是hash结构存储
     */
    public static final String CACHE_CLIENT_KEY = "oauth_client_details";

    public static final String TOKEN_PARAM = "access_token" ;

    public static final String TOKEN_HEADER = "accessToken" ;

    public static final String AUTH = "auth" ;

    public static final String TOKEN = "token" ;

    public static final String AUTHORIZATION = "Authorization" ;
}
