package vanliuzhi.org.auth.service.json;

/**
 * @author lys3415
 */
public class NotSupportedJsonMapper implements JsonMapper {
    @Override
    public String write(Object input) throws Exception {
        throw new UnsupportedOperationException(
                "Neither Jackson 1 nor 2 is available so JSON conversion cannot be done");
    }

    @Override
    public <T> T read(String input, Class<T> type) throws Exception {
        throw new UnsupportedOperationException(
                "Neither Jackson 1 nor 2 is available so JSON conversion cannot be done");
    }
}