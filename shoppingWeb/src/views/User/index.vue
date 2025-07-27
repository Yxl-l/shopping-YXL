<script setup lang="ts">
import { ref, reactive } from "vue";
import type { ComponentSize } from "element-plus";
import { queryPages, addUser, deleteUser, updateUser, queryUserById } from "@/api/user";
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus';
import type { FormInstance } from 'element-plus';
import { ElMessageBox } from 'element-plus';

// 定义用户数据结构
interface User {
  id: number;
  name: string;
  userName: string;
  passWord: string;
  image: string;
  showPassword?: boolean;
}

// 初始化数据时添加 showPassword 属性
const tableData = reactive<User[]>([]);
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
  const result = await queryPages(searchForm.name, page.value, pageSize.value);

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

// 分页相关
const total = ref(0);
const page = ref(1);
const pageSize = ref(5);
const size: ComponentSize = "default";
const disabled = false;
const background = true;

const handleSizeChange = (val: number) => {
  pageSize.value = val;
  page.value = 1;
  fetchUserList();
};

const handleCurrentChange = (val: number) => {
  page.value = val;
  fetchUserList();
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

// 文件输入引用
const fileInputRef = ref<HTMLInputElement | null>(null);

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
  if (fileInputRef.value) {
    fileInputRef.value.value = '';
  }
};

//点编辑
const queryEmpById = async (id: number) => { 
  dialogShow.value = true;
  tips.value = "编辑用户";
  const res = await queryUserById(id);
  Object.assign(empForm, res.data);
  
  // 清空文件输入框
  if (fileInputRef.value) {
    fileInputRef.value.value = '';
  }
};

// 关联表单对象
const ruleFormRef = ref<FormInstance | null>(null);
// 提交表单校验

const addEmp = async () => {
  ruleFormRef.value!.validate(async (valid: boolean) => {
    if (valid) {
      //校验通过
      let res;
      if (empForm.id) {
        //存在ID, 修改
        res = await updateUser(empForm);
      } else {
        //不存在ID, 新增
        res = await addUser(empForm);
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
    }
  });
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

//删除方法
const deleteByIds = async (id: number) => {
  //弹出一个确认框, 如果确认, 就删除;
  ElMessageBox.confirm("确定删除选中用户吗?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(async () => {
    // 删除用户
    const res = await deleteUser(id);
    if (res.code) {
      ElMessage.success(`删除成功`);
      fetchUserList(); //刷新界面
    } else {
      ElMessage.error(res.msg);
    }
  });
};

// 初始化加载
fetchUserList();
</script>

<template>
  <div>
    <el-button type="primary" @click="addShow">添加用户</el-button>
    <div>
      <!-- 弹框表单 -->
      <el-dialog
        v-model="dialogShow"
        :title="tips"
        width="800"
      >
        <el-form :model="empForm" label-width="auto" ref="ruleFormRef" :rules="rules">
          <el-row :gutter="10">
            <el-col :span="12">
              <el-form-item label="用户名" prop="userName">
                <el-input v-model="empForm.userName" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="姓名" prop="name">
                <el-input v-model="empForm.name" />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="用户密码" prop="passWord">
                <el-input v-model="empForm.passWord" />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="图像">
                <input 
                  ref="fileInputRef"
                  type="file" 
                  @change="handleFileChange" 
                  accept="image/*" 
                />
                <div v-if="empForm.image">
                  <img :src="empForm.image" class="avatar" />
                </div>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogShow = false">取消</el-button>
            <el-button type="primary" @click="addEmp">提交</el-button>
          </div>
        </template>
      </el-dialog>
    </div>
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
    <div>
      <el-table :data="tableData" style="width: 100%">
        <el-table-column fixed type="index" label="序号" width="150" />
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="id" label="主键id" width="120" />
        <el-table-column prop="userName" label="账户名" width="150" />
        <!-- 修改密码列 -->
        <el-table-column prop="passWord" label="账户密码" width="200">
          <template #default="scope">
            <span>{{ scope.row.showPassword ? scope.row.passWord : "****" }}</span>
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
            <el-button type="primary" @click="queryEmpById(scope.row.id)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

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

<style scoped>
.avatar {
  width: 178px;
  height: 178px;
  display: block;
  margin-top: 10px;
}
</style>