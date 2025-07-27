import axios from 'axios';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useUserStore } from '@/store/loginStore'; // 引入用户store

// 配置新建一个 axios 实例
const service = axios.create({
    //后端服务地址
    baseURL: '/api',
    // 请求超时时间
    timeout: 10 * 1000, 
    // 请求数据封装类型，这里采用JSON格式
    headers: { "Content-Type": "application/json;charset=UTF-8" }
});

// 添加请求拦截器
service.interceptors.request.use(
    (config) => {
        // 在发送请求之前，从store中获取token并设置到Authorization头部
        const userStore = useUserStore();
        if (userStore.token) {
            config.headers!['token'] = userStore.token;
        }
        return config;
    },
    (error) => {
        // 对请求错误做些什么
        return Promise.reject(error);
    }
);
 
// 添加响应拦截器
service.interceptors.response.use(
    res => {
        const resp = res.data
        if (resp.code === 401){
            ElMessageBox.alert('请重新登录', '提示', {})
                .then(() => {
                    console.log(resp)
                 })
                .catch(() => { });
        }
        return Promise.resolve(res.data);
    }, 
    error => {
        switch (error.response.status) {
        case 500:
            ElMessage.error('Oops,Server Error.')
            break
        case 404:
            ElMessage.error('Oops, Not Find!')
            break
        case 400:
            ElMessage.error('Oops, 400!')
            break
        // 其他错误，直接抛出错误提示
        default:
            ElMessage.error(error.message)
        }
        return Promise.reject(error);
    }
);
 
// 导出 axios 实例
export default service;