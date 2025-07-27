<script setup >
import { ref, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { useRouter } from "vue-router";
import { useUserStore } from '@/store/loginStore'
import { SwitchButton } from '@element-plus/icons-vue'

let router = useRouter();
const userStore = useUserStore();
const loginName = ref(""); //定义用户名;

onMounted(() => {
  // 从 Pinia store 中获取用户名
  if (userStore.user.name) {
    loginName.value = userStore.user.name;
  } else {
    router.push("/login");
  }
});

const logout = () => {
  //弹出确认框, 如果确认, 则退出登录, 跳转到登录页面
  ElMessageBox.confirm("确认退出登录吗?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    //确认, 则清空登录信息
    ElMessage.success("退出登录成功");
    // 使用 Pinia store 的登出方法
    userStore.userLoginOut();
    router.push("/login"); //跳转到登录页面
  });
};
</script>

<template>
  <div class="common-layout">
    <el-container>
      <!-- Header 区域 -->
      <el-header class="header">
        <span class="title">智能仓储系统</span>
        <span class="right_tool">
          <a href="javascript:void(0)" @click="logout">
            <el-icon><SwitchButton /></el-icon> 退出登录【{{ loginName }}】
          </a>
        </span>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <el-menu default-active="2" class="el-menu-vertical-demo" router>
            <el-sub-menu index="1">
              <template #title>用户模块</template>
              <el-menu-item index="/user">用户管理</el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="2">
              <template #title>分类模块</template>
              <el-menu-item index="/classification">分类管理</el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-aside>
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
.header {
  background-image: linear-gradient(
    to right,
    #00547d,
    #007fa4,
    #00aaa0,
    #00d072,
    #a8eb12
  );
}

.title {
  color: rgb(229, 240, 22);
  font-size: 40px;
  font-family: 楷体;
  line-height: 60px;
  font-weight: bolder;
}

.right_tool {
  float: right;
  line-height: 60px;
}

a {
  color: rgb(14, 156, 238);
  text-decoration: none;
}

/* 顶部颜色 */
.el-header {
  background-color: palegreen;
}
/* 菜单颜色 */
.el-aside {
  background-color: azure;
  min-height: 900px;
}

/* 顶部样式修改 */
.el-header {
  /* background: url("/好看的.png") repeat-x;  */
  /* 横向重复 */
  background-size: auto 100%; /* 高度填充，宽度保持原比例 */
  background-position: center;
  height: 100px; /* 根据图片实际高度调整 */
  min-width: 100%; /* 确保横向能平铺 */
  white-space: nowrap; /* 防止换行 */
  color: white;
}

.el-aside {
  /* background: url("/岸宝.png") no-repeat center center; */
  background-size: cover;
  min-height: 900px;
  position: relative; /* 为伪元素定位做准备 */
}

/* 半透明遮罩层 */
.el-aside::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.01); /* 白色半透明，0.5是透明度 */
  z-index: 1;
}

/* 确保菜单内容在遮罩层之上 */
.el-menu {
  position: relative;
  z-index: 2;
  background-color: transparent !important; /* 菜单背景透明 */
}

.el-main {
  /* background: url("./瑞希.png") no-repeat center center; */
  background-size: cover;
}
</style>