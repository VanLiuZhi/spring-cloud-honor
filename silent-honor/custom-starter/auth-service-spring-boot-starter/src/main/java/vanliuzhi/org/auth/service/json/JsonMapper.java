package vanliuzhi.org.auth.service.json;

/**
 * @author lys3415
 */
public interface JsonMapper {
    /**
     * 封装写操作
     *
     * @param input POJO对象
     * @return String
     * @throws Exception 异常
     */
    String write(Object input) throws Exception;

    /**
     * 封装读操作
     *
     * @param input json字符串
     * @param type  序列化对象类型
     * @return String
     * @throws Exception 异常
     */
    <T> T read(String input, Class<T> type) throws Exception;

}
