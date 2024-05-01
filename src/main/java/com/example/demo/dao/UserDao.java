package com.example.demo.dao;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface UserDao extends Mapper<User> {

    /**
     * 默认结果为1个或0个，其他情况抛异常
     * @param name
     * @return
     */
    @Select("select * from user where name = #{name}")
    User selectByName(String name);
}
