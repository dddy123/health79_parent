package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

import java.util.Set;

@Component
public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){

        //通过比较两个set获取差值
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);

        for (String img : set) {
            //删除七牛云中的多余的图片
            QiniuUtils.deleteFileFromQiniu(img);
            //删除redis中多余的数据
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,img);
        }


    }

}
