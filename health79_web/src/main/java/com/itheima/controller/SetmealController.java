package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;


    @RequestMapping("upload")
    public Result upload(@RequestParam("imgFile")MultipartFile file){

        try {
            //获取上传的文件名称
            String originalFilename = file.getOriginalFilename();
            //获取文件的后缀
            int index = originalFilename.indexOf(".");
            String suffix = originalFilename.substring(index);

            //随机的文件名称
            String fileName = UUID.randomUUID().toString()+suffix;
            //将文件上传到七牛云
            QiniuUtils.upload2Qiniu(file.getBytes(),fileName);

            //将上传到七牛云中的图片存入到redis中的set集合中
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);


            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }

    }

    /**
     * 新增套餐
     */
    @RequestMapping("add")
    public Result add(@RequestParam("token")String token,@RequestParam("checkGroupIds")Integer[]checkGroupIds, @RequestBody Setmeal setmeal){

        try {
            String redisToken = jedisPool.getResource().get(token);
            if (redisToken==null || !token.equals(redisToken)){
                return new Result(false,"非法请求");
            }
            setmealService.add(checkGroupIds,setmeal);
            //第一次提交表单成功之后，删除redis中的token
            jedisPool.getResource().del(token);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }


    }

    /**
     * 分页查询
     */

    @RequestMapping("findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){

        try {
            PageResult pageResult = setmealService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
            return new Result(true,MessageConstant.QUERY_SETMEALLIST_SUCCESS,pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEALLIST_FAIL);

        }

    }

    /**
     * 响应令牌
     */
    @RequestMapping("token")
    public Result token(){
        try {
            String token = UUID.randomUUID().toString();
            //将token存入redis中
            jedisPool.getResource().setex(token,60*10,token);
            return new Result(true,"响应令牌成功",token);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"响应令牌失败");

        }
    }
}
