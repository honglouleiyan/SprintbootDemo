package com.example.demo.contorller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.DataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import java.util.List;

/**
 * @created with IntelliJ IDEA.
 * @author: heaven
 * @date: 2019/10/12
 * @time: 14:32
 * @description:
 **/
@RestController
public class CheckController {

    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;

   @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @GetMapping("/welcome")
    public String welcome() {
        redisTemplate.opsForValue().set("k","kk");
        String value = (String) redisTemplate.opsForValue().get("k");
        return value;
    }

    @GetMapping("/data")
    public String data() {
        DataModel model = new DataModel();
        model.setAa("aa");
        model.setBb("bb");
        model.setCc("cc");
        redisTemplate.opsForValue().set("aa", JSONObject.toJSONString(model));
        String value = (String) redisTemplate.opsForValue().get("aa");
        System.out.println(value);
        return "ok";
    }

    @GetMapping("/increase")
    public String increase() {

        List<RedisClientInfo> infos = redisTemplate.getConnectionFactory().getClusterConnection().serverCommands().getClientList();
        for(RedisClientInfo info:infos) {
            System.out.println(info);
        }
        for(int i = 0; i < 100; i++) {
//            redisTemplate.opsForValue().set(i+"",i+"");
            System.out.println(redisTemplate.opsForValue().get(i+""));

        }

        return "OK";
    }

    @GetMapping("/increase2")
    public String increase2() {

        for(int i = 0; i < 100; i++) {
            System.out.println(redisTemplate.opsForValue().get(i+""));
        }

        return "OK";
    }
}
