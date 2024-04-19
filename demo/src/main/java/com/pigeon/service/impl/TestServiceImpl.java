package com.pigeon.service.impl;

import com.pigeon.context.UserHolder;
import com.pigeon.entity.TestPO;
import com.pigeon.feign.AccountServiceClient;
import com.pigeon.manager.RabbitMQManager;
import com.pigeon.response.CommonResponse;
import com.pigeon.service.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Idenn
 * @date 3/16/2024 6:47 PM
 */
@Service
public class TestServiceImpl extends BaseServiceImpl<TestPO> implements TestService {

    @Resource
    private RabbitMQManager rabbitMQManager;

    @Resource
    private AccountServiceClient accountServiceClient;

    public String testa(String a) {
        return a + "aaaaaaa";
    }

    @Override
    public void testFeign() {
        CommonResponse commonResponse = accountServiceClient.testFeign();
        System.out.println(commonResponse);
    }

    // @Override
    // public UserDTO getUserByUsername(String name) {
    //     CommonResponse<UserDTO> response = accountServiceClient.getUserByUsername(name);
    //     return response.getData();
    // }

    @Override
    public String testRabbitmq(String msg) {
        TestPO testPO = new TestPO();
        testPO.setTestValue(msg);

        rabbitMQManager.send("pigeon.test.exchange", "pigeon.test.routingKey", testPO, UserHolder.getUser());
        return msg;
    }
}
