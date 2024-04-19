package com.pigeon.controller;

import com.pigeon.context.UserHolder;
import com.pigeon.entity.pojo.SecurityContextPOJO;
import com.pigeon.enums.ResultCode;
import com.pigeon.manager.RedisLockManager;
import com.pigeon.manager.RedisManager;
import com.pigeon.response.CommonResponse;
import com.pigeon.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 测试
 *
 * @author Idenn
 * @date 3/6/2024 7:30 PM
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private RedisManager redisManager;

    @Resource
    private RedisLockManager redisLockManager;

    @Resource
    private TestService testService;

    @Value("${pigeon.middleware.redis.host-name}")
    private String a;

    @Resource
    private SecurityContextPOJO securityContextPOJO;

    @GetMapping("/aaa")
    public String demo() {
        return a;
    }

    @GetMapping("/bbb")
    public String[] testStr() {
        return securityContextPOJO.getWhiteList();
    }

    @GetMapping("/testRedis")
    public CommonResponse testRedis() {

        redisManager.set("abc", 123);
        return new CommonResponse(ResultCode.SUCCESS.getCode(), redisManager.getString("abc"));
    }

    @GetMapping("/testService1")
    public String testServiceFunc1(String a) {
        return testService.testa(a);
    }

    // @GetMapping("/testService2")
    // public String testServiceFunc2(String a) {
    //     TestPO testPO = new TestPO();
    //     testPO.setTestValue(a);
    //     // return testService.test(testPO);
    //     return null;
    // }

    // @GetMapping("/testFeign")
    // public UserDTO testFeign(String name) {
    //     return testService.getUserByUsername(name);
    // }

    @GetMapping("/testFeign")
    public void testFeign() {
        System.out.println(UserHolder.getUser());
        testService.testFeign();
    }

    @PostMapping("/testRabbitmq")
    public String testRabbitmq(String msg) {
        return testService.testRabbitmq(msg);
    }

    /*
     * 测试自动释放锁
     * 在执行testRLock1时，锁会被watchdog一直续签，直到sleep结束
     * 期间执行testRLock2无法改变test123的值
     */
    @PostMapping("/testRLock1")
    public String testRLock1() {
        boolean b = redisLockManager.writeLock("LOCK1", () -> {
            redisManager.set("test123", "11111111111");
            Thread.sleep(50000);
        });
        return String.valueOf(b);
    }

    /*
     * 测试手动释放锁
     */
    @PostMapping("/testRLock2")
    public String testRLock2() {
        boolean b = redisLockManager.writeLockManual("LOCK1", (rLock) -> {
            redisManager.set("test123", "222222222222");
            rLock.unlock();
        });
        return String.valueOf(b);
    }
}
