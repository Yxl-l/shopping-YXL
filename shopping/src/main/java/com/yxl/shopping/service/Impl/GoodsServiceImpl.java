package com.yxl.shopping.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxl.shopping.mapper.GoodsMapper;
import com.yxl.shopping.pojo.Good;
import com.yxl.shopping.service.GoodsService;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Good> implements GoodsService {

}
