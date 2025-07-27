package com.yxl.shopping.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Good {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer fuid;
    /**
     * 非表字段
     */
    @TableField(exist = false)
    private List<Good> ziList;
}
