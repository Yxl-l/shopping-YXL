//引入pinia
import { defineStore } from 'pinia'
import { loginApi } from '@/api/login'
import { ElMessage } from 'element-plus'
import { ref } from 'vue'

//创建store
export const useUserStore = defineStore('localUser', () => {
    // 定义管理用户数据的state
    const user = ref({
        userId: 0,
        userName: '',
        name: '',
        roles: [] as string[]
    })
    const token = ref('')
    const isLogin = ref(false)

    // 定义获取接口数据的action函数
    const userLogin = async (account: String, password: String) => {
        //执行登录
        const result = await loginApi({ 'userName': account, 'passWord': password }) as any
        //登录成功
        if (result.code == 1) {
            user.value = {
                userId: result.data.userId,     // 添加 userId
                userName: result.data.userName,
                name: result.data.name,         // 添加 name（原代码中的 account 改为 name）
                roles: result.data.roles
            },
                isLogin.value = true as boolean;
                token.value = result.data.token     // 修改为 result.data.token

            //信息提示
            ElMessage({
                showClose: true,
                message: result.msg,
                type: 'success',
            })
        } else {
            //登录失败，提示错误信息
            ElMessage({
                showClose: true,
                message: result.msg,
                type: 'error',
            })
        }
    }

    // 登出方法
    const userLoginOut = () => {
        user.value = {
            userId: 0,
            userName: '',
            name: '',        // 原 account 改为 name，保持类型一致
            roles: []
        }
        isLogin.value = false
        token.value = ''
    }

    // 以对象形式把state和action return
    return { user, userLogin, userLoginOut, token, isLogin }
},
    // 持久化配置
    {
        persist: true,
    }
)