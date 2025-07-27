package com.yxl.shopping.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yxl.shopping.mapper.UserMapper;
import com.yxl.shopping.pojo.*;
import com.yxl.shopping.service.UserService;
import com.yxl.shopping.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/user")
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        log.info("员工来登录啦 , {}", user);
        //获取登录信息对象实列LoginInfo
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,user.getUserName())
                .eq(User::getPassWord,user.getPassWord());
        User userNew = userMapper.selectOne(wrapper);
        if (userNew!=null){
            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setUserId(userNew.getId());
            loginInfo.setName(userNew.getName());
            loginInfo.setUserName(userNew.getUserName());
            //登录成功生成令牌给登录信息对象
            Map<String,Object> map = new HashMap<>();
            map.put("id",userNew.getId());
            String token = JwtUtils.generateJwt(map);
            //生成令牌给登录信息对象
            loginInfo.setToken(token);
            log.info("登陆成功");
            return Result.success(loginInfo);
        }




        log.info("登录失败");
        return Result.error("登陆失败请核对用户名密码");
    }
    /**
     * 查询用户
     */
    @GetMapping
    public Result getUser(UserResult userResult){
        //分页封装对象Page
        Page<User> page = new Page<>(userResult.getPage(), userResult.getPageSize());
        System.out.println();
        log.info("分页查询");
        //构建条件查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(User::getName,userResult.getName());
        //、执行分页查询
        IPage<User> iPage = userService.page(page,wrapper);

        return Result.success(new PageResult<User>(iPage.getTotal(), iPage.getRecords()));
    }
    /**
     * 增加用户
     */
    @PostMapping
    public Result addUser(@RequestBody User user){
        log.info("增加用户{}",user);
        int count = userMapper.insert(user);
        return count==1? Result.success("添加成功"):Result.error("添加失败");
    }
    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable Integer id){
        log.info("删除用户{}",id);
        int count = userMapper.deleteById(id);
        return count==1? Result.success("删除成功"):Result.error("删除失败");
    }
    /**
     * 更具id查询用户
     */
    @GetMapping("/{id}")
    public Result queryUserById(@PathVariable Integer id){
        log.info("查询用户{}",id);
        return Result.success(userMapper.selectById(id));
    }
    /**
     * 修改用户
     */
    @PutMapping
    public Result updateUser(@RequestBody User user){
        log.info("修改用户{}",user);
        int count = userMapper.updateById(user);
        return count==1? Result.success("修改成功"):Result.error("修改失败");
    }
}
