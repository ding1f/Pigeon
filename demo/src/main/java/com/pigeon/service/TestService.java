package com.pigeon.service;

import com.pigeon.entity.TestPO;

public interface TestService extends BaseService<TestPO> {
    String testa(String a);

    void testFeign();

    String testRabbitmq(String msg);

    // UserDTO getUserByUsername(String name);
}
