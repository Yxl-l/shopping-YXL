<!-- userList.vue -->
<template>
    <div>
  <div>
    <el-table :data="tableData" style="width: 100%">
      <el-table-column fixed type="index" label="序号" width="150" />
      <el-table-column prop="name" label="姓名" width="120" />
      <el-table-column prop="id" label="主键id" width="120" />
      <el-table-column prop="userName" label="账户名" width="150" />
      <!-- 修改密码列 -->
      <el-table-column prop="passWord" label="账户密码" width="200">
        <template #default="scope">
             <!-- 根据showPassword状态决定显示密码或星号 -->
          <span>{{ scope.row.showPassword ? scope.row.passWord : "****" }}</span>
           <!-- 切换密码显示状态的按钮 -->
          <el-button
            link
            type="primary"
            size="small"
            @click="togglePassword(scope.row)"
            style="margin-left: 10px"
          >
            {{ scope.row.showPassword ? "隐藏" : "显示" }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="头像" width="120">
        <template #default="scope">
          <img :src="scope.row.image" style="width: 100px; height: 100px" />
        </template>
      </el-table-column>

      <el-table-column fixed="right" label="操作" min-width="120">
        <template #default="scope">
          <el-button type="danger" @click="deleteByIds(scope.row.id)"> 删除 </el-button>
          <el-button type="primary" @click="editUser(scope.row.id)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
  <br />
  <div>
    <el-pagination
      v-model:current-page="page"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 30, 40]"
      :size="size"
      :disabled="disabled"
      :background="background"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, defineProps, defineEmits } from "vue";
import type { ComponentSize } from "element-plus";
import { ElMessage, ElMessageBox } from "element-plus";
import { queryPages, deleteUser, queryUserById } from "@/api/user";

// 定义用户数据结构
interface User {
  id: number;
  name: string;
  userName: string;
  passWord: string;
  image: string;
  showPassword?: boolean;// 控制密码显示状态
}

// 表格数据
const tableData = reactive<User[]>([]);

// 分页相关
const total = ref(0);
const page = ref(1);
const pageSize = ref(5);
const size: ComponentSize = "default";
const disabled = false;
const background = true;

// 定义事件，用于与父组件通信
const emit = defineEmits<{
  (e: "editUser", id: number): void; // 编辑用户事件
  (e: "updateUserList"): void;// 更新用户列表事件
}>();

// 定义属性，接收父组件传递的数据
const props = defineProps<{
  searchName: string;
}>();

// 获取用户列表并处理分页逻辑
const fetchUserList = async () => {
  const result = await queryPages(props.searchName, page.value, pageSize.value);

  // 清空原数据并添加新数据
  tableData.splice(0);
  // 使用 map 方法遍历返回的数据，为每条记录添加 showPassword 属性，默认设为 false
  tableData.push(
    ...result.data.rows.map((item: any) => ({
      ...item,
      showPassword: false,
    }))
  );
  total.value = result.data.total;
};

// 切换密码显示状态的方法
const togglePassword = (row: User) => {
  row.showPassword = !row.showPassword;
};

const handleSizeChange = (val: number) => {
  pageSize.value = val;
  page.value = 1;
  fetchUserList();
};

const handleCurrentChange = (val: number) => {
  page.value = val;
  fetchUserList();
};

// 删除方法
const deleteByIds = async (id: number) => {
  //弹出一个确认框, 如果确认, 就删除;
  ElMessageBox.confirm("确定删除选中用户吗?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      // 删除用户
      const res = await deleteUser(id);
      if (res.code) {
        ElMessage.success(`删除成功`);
        fetchUserList(); //刷新界面
        emit("updateUserList"); // 通知父组件更新列表
      } else {
        ElMessage.error(res.msg);
      }
    })
    .catch(() => {
      // 用户取消删除
    });
};

// 编辑用户方法 - 触发editUser事件通知父组件
const editUser = (id: number) => {
  emit("editUser", id);
};

// 暴露方法给父组件调用
defineExpose({
  fetchUserList,
});
</script>

<style scoped>
.avatar {
  width: 178px;
  height: 178px;
  display: block;
  margin-top: 10px;
}
</style>