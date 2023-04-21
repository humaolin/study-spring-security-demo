package com.pearl.oauth2.demo.mapper;

import com.pearl.oauth2.demo.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pearl
 * @since 2023-03-23
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
