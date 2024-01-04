package com.spingboot.demo.spingbootdemo.async;

import com.google.gson.Gson;
import com.spingboot.demo.spingbootdemo.bean.User;
import com.spingboot.demo.spingbootdemo.mark.Mark;
import com.spingboot.demo.spingbootdemo.redis.RedisService;
import com.spingboot.demo.spingbootdemo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class UserSyncTask {

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserRepository userRepository;

    @Async
    @Scheduled(fixedRate = 30000)
    public void syncUsers() {
        // 查询数据库，获取所有用户数据
        List<Integer> list = userRepository.findAllUser();
        redisService.saveData(Mark.ALL_USER_DATA_KEY,splitUid(list));
        log.info("用户数据定时同步任务执行: "+ list.size());
    }


    private String splitUid(List<Integer> list){
        StringBuilder item = new StringBuilder();
        for (Integer id : list){
            if (item.isEmpty()){
                item.append(id);
            }else{
                item.append(",").append(id);
            }
        }
        log.info(item.toString());
        return item.toString();
    }

}
