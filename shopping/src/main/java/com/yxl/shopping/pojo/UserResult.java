package com.yxl.shopping.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResult extends User {
    private Integer page=1;//当前页
    private Integer pageSize=5;//每页条数
    private String name;

}
