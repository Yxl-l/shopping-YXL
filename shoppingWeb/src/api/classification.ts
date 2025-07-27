// Vue 3 接口工具类：login.ts 文件中 
import request from '@/utils/request';
//获取树结构分类
export const queryClassificationTree=()=>request.get(`/good`)
//获取普通结构全部分类
export const queryClassification=()=>request.get(`/good/all`)
//增加分类
export const addClassification=(data:any)=>request.post(`/good`,data)
// 根据id删除分类
export const deleteClassification=(id:number)=>request.delete(`/good/${id}`)
// 根据id查询分类
export const queryClassificationById=(id:number)=>request.get(`/good/${id}`)
// 修改分类
export const updateClassification=(data:any)=>request.put(`/good`,data)

