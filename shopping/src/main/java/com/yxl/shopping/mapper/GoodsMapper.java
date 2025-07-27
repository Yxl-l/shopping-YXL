package com.yxl.shopping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.yxl.shopping.pojo.Good;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsMapper extends BaseMapper<Good> {
}
