package cn.celess.common.constant;

import cn.celess.common.CommonBaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class UserAccountStatusEnumTest extends CommonBaseTest {

    @Test
    public void get() {
        assertEquals(UserAccountStatusEnum.NORMAL, UserAccountStatusEnum.get(0));
        assertEquals(UserAccountStatusEnum.LOCKED, UserAccountStatusEnum.get(1));
        assertEquals(UserAccountStatusEnum.DELETED, UserAccountStatusEnum.get(2));
    }

    @Test
    public void toJson() throws JsonProcessingException {
        // 序列化
        ObjectMapper objectMapper = new ObjectMapper();
        assertEquals("{\"code\":0,\"desc\":\"正常\"}", objectMapper.writeValueAsString(UserAccountStatusEnum.NORMAL));
    }

    @Test
    public void testGet() throws IOException {
        // 反序列化
        ObjectMapper mapper = new ObjectMapper();
        UserAccountStatusEnum userAccountStatusEnum = mapper.readValue(mapper.writeValueAsString(UserAccountStatusEnum.NORMAL), UserAccountStatusEnum.class);
        assertEquals(UserAccountStatusEnum.NORMAL.getCode(), userAccountStatusEnum.getCode());
        assertEquals(UserAccountStatusEnum.NORMAL.getDesc(), userAccountStatusEnum.getDesc());
    }
}