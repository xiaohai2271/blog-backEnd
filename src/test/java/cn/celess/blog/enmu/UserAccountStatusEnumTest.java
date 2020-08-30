package cn.celess.blog.enmu;

import cn.celess.blog.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserAccountStatusEnumTest extends BaseTest {

    @Test
    public void get() {
        assertEquals(UserAccountStatusEnum.NORMAL, UserAccountStatusEnum.get(0));
        assertEquals(UserAccountStatusEnum.LOCKED, UserAccountStatusEnum.get(1));
        assertEquals(UserAccountStatusEnum.DELETED, UserAccountStatusEnum.get(2));
    }

    @Test
    public void toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        assertEquals("{\"code\":0,\"desc\":\"正常\"}", objectMapper.writeValueAsString(UserAccountStatusEnum.NORMAL));
    }
}