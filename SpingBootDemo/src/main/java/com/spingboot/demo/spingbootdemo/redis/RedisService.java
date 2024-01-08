package com.spingboot.demo.spingbootdemo.redis;

import com.spingboot.demo.spingbootdemo.mark.Mark;
import com.spingboot.demo.spingbootdemo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private UserRepository userRepository;
    @Autowired
    public RedisService(RedisTemplate<String, Object> redisTemplate,UserRepository userRepository) {
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
    }


    /***
     * 初始化同步用户数据，只会在项目启动时执行一次，缓存所有有效用户ID
     */
    @Async
    @PostConstruct
    public void initAsyncUser(){
        List<Integer> list = userRepository.findAllUser();
        List<String> stringList = list.stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        addToList(Mark.ALL_USER_DATA_KEY,stringList);
        log.info("初始化同步用户数据:"+ list.size());
    }

    /**
     * 存储单个数据
     * @param key
     * @param value
     */
    public void saveData(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 取出单个数据
     * @param key
     * @return
     */

    public Object getData(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    /**
     * 存储列表数据
     * @param key
     * @param list
     */
    public void addToList(String key, List<String> list) {
        redisTemplate.opsForList().rightPushAll(key, list.toArray());
    }

    /**
     * 获取列表数据
     * @param key
     */
    public List<Object> getList(String key) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(key,0,-1);
    }

    /**
     * 删除指定数据中的所有关于value的值
     * @param key
     * @param value
     */
    public void remove(String key,String value){
        redisTemplate.opsForList().remove(key,0,value);
    }


    /**
     * 向列表中添加数据
     * @param key
     * @param value
     * @param right 当为true时添加到列表末尾，false添加到列表头部
     */
    public void addListValue(String key,String value,boolean right){
        if (right){
            redisTemplate.opsForList().rightPush(key,value);
        }else{
            redisTemplate.opsForList().leftPush(key,value);
        }
    }

    /***
     * 清空Redis所有数据
     */
    public void clearAllData(){
        redisTemplate.getConnectionFactory().getConnection().flushDb(); // 清空当前数据库
    }
}
