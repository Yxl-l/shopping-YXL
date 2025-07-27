<template>
  <el-table :data="tableData" style="width: 100%">
    <el-table-column fixed type="index" label="序号" width="150" />
    <el-table-column prop="name" label="姓名" width="120" />
    <el-table-column prop="id" label="主键id" width="120" />
    <el-table-column prop="userName" label="账户名" width="150" />
    <!-- 修改密码列 -->
    <el-table-column prop="passWord" label="账户密码" width="200">
      <template #default="scope">
        <span>{{ scope.row.showPassword ? scope.row.passWord : '****' }}</span>
        <el-button 
          link 
          type="primary" 
          size="small" 
          @click="togglePassword(scope.row)"
          style="margin-left: 10px"
        >
          {{ scope.row.showPassword ? '隐藏' : '显示' }}
        </el-button>
      </template>
    </el-table-column>
    <el-table-column label="头像" width="120">
      <template #default="scope">
        <img :src="scope.row.image" style="width: 100px; height: 100px;" />
      </template>
    </el-table-column>
    
    <el-table-column fixed="right" label="操作" min-width="120">
      <template #default>
        <el-button type="danger" @click="handleClick">
          删除
        </el-button>
        <el-button type="primary">查看</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { reactive } from 'vue'
const handleClick = () => {
  console.log('click')
}

// 初始化数据时添加 showPassword 属性
const tableData = reactive({})

const queryPages=async()=>{ 
  
}



// 切换密码显示状态的方法
const togglePassword = (row: any) => {
  row.showPassword = !row.showPassword
}
</script>