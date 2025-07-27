import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/views/Layout/index.vue';
import User from '@/views/User/index.vue';
import Classification from '@/views/Classification/index.vue';
import Index from '@/views/Index/index.vue';
import Login from '@/views/login/index.vue';
import { useUserStore } from '@/store/loginStore'; // 添加这一行

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',component: Layout,
      children: [
        { path: '/', redirect: '/index' },
        { path: '/index', name: 'Index', component: Index },
        { path: '/user', name: 'User', component: User },
        { path: '/classification', name: 'Classification', component: Classification },
      ]
    },
    { path: '/login', component: Login },
  ]
})

// 添加路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore();
  
  // 如果访问的不是登录页面，且未登录，则跳转到登录页
  if (to.path !== '/login' && !userStore.isLogin) {
    next('/login');
  } else {
    next();
  }
});

export default router