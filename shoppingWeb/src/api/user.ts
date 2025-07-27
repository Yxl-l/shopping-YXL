import request from '@/utils/request';
//分页查询
export const queryPages=(name:any,page:number,pageSize:number)=>request.get(`/user?name=${name}&page=${page}&pageSize=${pageSize}`)
//添加用户
export const addUser=(data:any)=>request.post(`/user`,data)
//删除用户
export const deleteUser=(id:number)=>request.delete(`/user/${id}`)
//修改用户
export const updateUser=(data:any)=>request.put(`/user`,data)
//根据id查询用户
export const queryUserById=(id:number)=>request.get(`/user/${id}`)