<!-- index.vue (修改后的部分) -->
<script setup lang="ts">
import { ref, reactive, onMounted, computed } from "vue";
import type { ComponentSize } from "element-plus";
import { queryPages, addUser, deleteUser, updateUser, queryUserById } from "@/api/user";
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus';
import type { FormInstance } from 'element-plus';
import { ElMessageBox } from 'element-plus';
import UserList from "./userList.vue";
import Window from "./window.vue"; // 引入 window 组件

// 定义用户数据结构
interface User {
  id: number;
  name: string;
  userName: string;
  passWord: string;
  image: string;
  showPassword?: boolean;
}

// 搜索数据
const searchForm = reactive({
  name: "",
});

//清空搜索表单
const resetSearchForm = () => {
  searchForm.name = "";
  fetchUserList();
};

// 获取用户列表并处理分页逻辑
const fetchUserList = async () => {
  userListRef.value?.fetchUserList();
};

// 创建变量，用于控制弹框是否显示
const dialogShow = ref(false);
// 弹框标题
const tips = ref("");
// 弹框表单数据
const empForm = reactive({
  id: "",
  userName: "",
  name: "",
  image: "",
  passWord: "",
});

// userList组件引用
const userListRef = ref<InstanceType<typeof UserList> | null>(null);
// window组件引用
const windowRef = ref<InstanceType<typeof Window> | null>(null);

// 处理文件选择，保存本地路径
const handleFileChange = (event: Event) => {
  const target = event.target as HTMLInputElement;
  const file = target.files?.[0];
  if (file) {
    // 创建本地URL
    const url = URL.createObjectURL(file);
    empForm.image = url;
  }
};

//点新增
const addShow = () => {
  empForm.id = "";
  empForm.userName = "";
  empForm.name = "";
  empForm.image = "";
  empForm.passWord = "";
  dialogShow.value = true;
  tips.value = "新增用户";
  
  // 清空文件输入框
  windowRef.value?.clearFileInput();
};

//点编辑
const queryEmpById = async (id: number) => { 
  dialogShow.value = true;
  tips.value = "编辑用户";
  const res = await queryUserById(id);
  Object.assign(empForm, res.data);
  
  // 清空文件输入框
  windowRef.value?.clearFileInput();
};

// 提交表单
const handleSubmit = async (data: any) => {
  // 校验通过
  let res;
  if (data.id) {
    //存在ID, 修改
    res = await updateUser(data);
  } else {
    //不存在ID, 新增
    res = await addUser(data);
  }
  // 判断结果
  if (res.code) {
    //操作成功，则刷新界面（重新获取员工列表），关闭弹框
    dialogShow.value = false;
    // 刷新界面
    fetchUserList();
    // 提示
    ElMessage.success("操作成功！");
  } else {
    ElMessage.error("操作失败！");
  }
};

// 取消表单操作
const handleCancel = () => {
  dialogShow.value = false;
};

// 定义校验规则（光标离开后的时间校验）
// 定义校验规则（适用于当前表单字段）
const rules = ref({
  userName: [
    { required: true, message: "请输入用户名", trigger: "blur" },
    {
      min: 2,
      max: 20,
      message: "用户名长度应在2到20个字符之间",
      trigger: "blur",
    },
  ],
  name: [
    { required: true, message: "请输入姓名", trigger: "blur" },
    {
      min: 2,
      max: 10,
      message: "姓名长度应在2到10个字符之间",
      trigger: "blur",
    },
  ],
  passWord: [
    { required: true, message: "请输入密码", trigger: "blur" },
    {
      min: 4,
      max: 20,
      message: "密码长度应在4到20个字符之间",
      trigger: "blur",
    },
  ],
});

// 处理编辑用户事件
const handleEditUser = (id: number) => {
  queryEmpById(id);
};

// 初始化加载
onMounted(() => {
  fetchUserList();
});
</script>

<template>
  <div class="box">
    <el-button type="primary" @click="addShow">添加用户</el-button>
    
    <!-- 使用 window 组件 -->
    <Window
      ref="windowRef"
      v-model="dialogShow"
      :title="tips"
      :form-data="empForm"
      :rules="rules"
      @submit="handleSubmit"
      @cancel="handleCancel"
      @file-change="handleFileChange"
    />
    
    <br>
    <div>
      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="姓名">
          <el-input v-model="searchForm.name" placeholder="请输入姓名" clearable />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="fetchUserList">查询</el-button>
          <el-button type="success" @click="resetSearchForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- 使用 userList 组件 -->
    <UserList 
      ref="userListRef"
      :search-name="searchForm.name"
      @edit-user="handleEditUser"
      @update-user-list="fetchUserList"
    />
  </div>
</template>

<style scoped>
.box{
  opacity: 0.8;
}
</style>