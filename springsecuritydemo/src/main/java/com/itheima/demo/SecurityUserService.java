package com.itheima.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SecurityUserService implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //将map模拟为一个数据库
    private Map map = new HashMap<>();

    public void init(){

        User user1 = new User();
        user1.setUsername("zhangsan");
        user1.setPassword(bCryptPasswordEncoder.encode("1234"));
        map.put(user1.getUsername(),user1);

        System.out.println(user1.getPassword());

        User user2 = new User();
        user2.setUsername("lisi");
        user2.setPassword(bCryptPasswordEncoder.encode("1234"));
        map.put(user2.getUsername(),user2);

        System.out.println(user2.getPassword());

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //初始化数据，模拟数据库查询
        init();

        User user = (User) map.get(username);

        //创建一个list集合，用来添加权限
        List<GrantedAuthority> list = new ArrayList<>();
        /**
         * 设置权限的认证规则：
         *   小写字母，表示，是赋予权限
         *   大小字母：表示，是赋予角色的，ROLE_ADMIN,ROLE_MANAGER
         */
        /*list.add(new SimpleGrantedAuthority("add"));
        list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));*/

        if (user.getUsername().equals("zhangsan")){
            list.add(new SimpleGrantedAuthority("add"));
            list.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
//            list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }



        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),list);
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("1234");
        String encode1 = bCryptPasswordEncoder.encode("1234");
        System.out.println(encode);
        System.out.println(encode1);

        boolean flag = bCryptPasswordEncoder.matches("1234", encode);
        System.out.println(flag);

        boolean flag2 = bCryptPasswordEncoder.matches("1234", encode1);
        System.out.println(flag2);


    }
}
