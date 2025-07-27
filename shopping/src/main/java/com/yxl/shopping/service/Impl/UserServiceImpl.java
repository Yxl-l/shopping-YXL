package com.yxl.shopping.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxl.shopping.mapper.UserMapper;
import com.yxl.shopping.pojo.User;
import com.yxl.shopping.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
