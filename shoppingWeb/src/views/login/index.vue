<script setup>
import { ref, onMounted } from "vue";
import { ElMessage } from "element-plus";
import router from "@/router/index";
import { useUserStore } from '@/store/loginStore'

// 定义表单数据
const loginForm = ref({ userName: "", passWord: "" });

// 页面加载时检查是否已登录
onMounted(() => {
  const store = useUserStore();
  if (store.isLogin) {
    router.push("/index");
  }
});

// 使用pinia执行登录
const onSubmitPinia = async () => {
  const store = useUserStore();
  await store.userLogin(loginForm.value.userName, loginForm.value.passWord);
   console.log(store.isLogin);
  // 修复：在登录成功后再执行跳转逻辑
  if (store.isLogin) {
    localStorage.setItem("loginUser", JSON.stringify(store.user));
    router.push("/index");
   
  }
};
</script>

<template>
  <div id="container">
    <div class="login-form">
      <el-form label-width="80px">
        <p class="title">智能仓储系统</p>
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="loginForm.userName" placeholder="请输入用户名"></el-input>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            type="password"
            v-model="loginForm.passWord"
            placeholder="请输入密码"
          ></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="onSubmitPinia">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
#container {
  padding: 10%;
  height: 410px;
  background-image: url("/public/login.jpg");
  background-repeat: no-repeat;
  background-size: cover;
}

.login-form {
  max-width: 400px;
  padding: 30px;
  margin: 0 auto;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
  background-color: white;
}

.title {
  font-size: 30px;
  font-family: "楷体";
  text-align: center;
  margin-bottom: 30px;
  font-weight: bold;
}

.button {
  margin-top: 30px;
  width: 120px;
}

</style>