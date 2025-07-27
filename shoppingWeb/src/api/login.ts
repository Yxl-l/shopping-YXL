// Vue 3 接口工具类：login.ts 文件中 
import request from '@/utils/request';
 
// 登录API
export const loginApi=(data:any)=>request.post(`/user/login`,data)