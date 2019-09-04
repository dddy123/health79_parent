package com.itheima.mapper;

import com.itheima.pojo.Member;
import org.apache.ibatis.annotations.Param;

public interface MemberMapper {
    Member findByTelephone(@Param("telephone")String telephone);

    void add(Member member);

    Integer findMemberCountsByDate(@Param("dateBegin")String dateBegin, @Param("dateEnd")String dateEnd);

    Integer findTodayNewMember(@Param("today")String today);

    Integer findTotalMember();


    Integer findNewMemberCountAfterDate(@Param("date")String date);
}
