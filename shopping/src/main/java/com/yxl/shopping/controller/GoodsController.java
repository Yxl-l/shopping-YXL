package com.yxl.shopping.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yxl.shopping.pojo.Good;
import com.yxl.shopping.pojo.Result;
import com.yxl.shopping.service.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/good")
@RequiredArgsConstructor
public class GoodsController {
    private final GoodsService goodService;
/**
 * 查询所有商品
 */
@GetMapping("/all")
public Result classification(){
    return Result.success(AllSP());
}
    /**
     * 查询所有商品并构建层级结构
     */
    @GetMapping
    public Result getAll(){
        List<Good> all = AllSP();
        // 从所有商品中筛选出父级商品（fuid为0的商品为父级）
        List<Good> fu = all.stream().filter(s -> s.getFuid().equals(0)).toList();
        // 递归为每个父级商品设置其子级商品列表，构建完整的层级结构
        fu.forEach(s->setZiList(s,all));
        // 返回构建好层级关系的父级商品列表
        return Result.success(fu);
    }


    /**
     * 根据ID查询单个商品
     * @param id 商品ID
     * @return 商品信息
     */
    @GetMapping("/{id}")
    public Good getGoodById(@PathVariable Integer id) {
        return goodService.getById(id);
    }

    /**
     * 新增商品
     * @param good 商品对象
     * @return 是否添加成功
     */
    @PostMapping
    public boolean addGood(@RequestBody Good good) {
        // 如果前端未传递fuid，默认设置为0（顶级分类）
        if (good.getFuid() == null) {
            good.setFuid(0);
        }
        return goodService.save(good);
    }

    /**
     * 修改商品
     * @param good 商品对象（需包含ID）
     * @return 是否修改成功
     */
    @PutMapping
    public boolean updateGood(@RequestBody Good good) {
        return goodService.updateById(good);
    }

    /**
     * 删除商品
     * @param id 商品ID
     * @return 是否删除成功
     */
    @DeleteMapping("/{id}")
    public boolean deleteGood(@PathVariable Integer id) {
        return goodService.removeById(id);
    }

    /**
     * 递归设置商品的子级列表
     * @param fu 当前需要设置子级列表的父级商品
     * @param all 所有商品的列表，用于查找当前商品的子级
     */
    private void setZiList(Good fu, List<Good> all){
        // 从所有商品中筛选出当前商品的直接子级商品（fuid等于当前商品id的商品）
        List<Good> ziList = all.stream().filter(s -> s.getFuid().equals(fu.getId())).toList();
        // 将找到的子级列表设置到当前商品的ziList属性中
        fu.setZiList(ziList);
        // 递归为每个子级商品也设置它们自己的子级列表，构建完整的多级结构
        ziList.forEach(s->setZiList(s,all));
    }
    /**
     * 查询所有商品
     * @return
     */
    private List<Good> AllSP() {
        // 创建查询条件构造器，用于查询所有商品
        LambdaQueryWrapper<Good> wrapper = new LambdaQueryWrapper<>();
        // 使用MyBatis-Plus的IService.list()方法查询所有商品数据
        List<Good> all = goodService.list(wrapper);
        return all;
    }

}
