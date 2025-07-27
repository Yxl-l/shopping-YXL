<template>
  <div class="classification-container">
    <!-- 新增分类按钮，点击后打开新增分类弹窗 -->
    <el-button type="primary" @click="handleAdd" style="margin-bottom: 20px;">新增分类</el-button>
    
    <!-- 表格样式的分类展示区域 -->
    <div class="tree-table">
      <!-- 表格头部，显示列名 -->
      <div class="table-header">
        <div class="header-item">分类名称</div>
        <div class="header-item operation-column">操作</div>
      </div>
      
      <!-- 树形组件，用于展示具有层级关系的分类数据 -->
       <!--props 配置项，定义如何解析数据 -->
        <!-- 节点点击事件处理 -->
        <!--  node-key="id"  每个节点的唯一标识字段 -->
      <el-tree
        :data="treeData"                   
        :props="defaultProps"              
        @node-click="handleNodeClick"      
        node-key="id"                       
        class="tree-table-body"           
      >
        <!-- 自定义树节点内容 -->
        <template #default="{ node, data }">
          <!-- node: 节点对象，包含节点信息 -->
          <!-- data: 节点数据，即Tree接口定义的数据结构 -->
          <div class="tree-node-row">
            <!-- 显示分类名称 -->
            <div class="node-label">{{ node.label }}</div>
            <!-- 操作按钮区域 -->
            <div class="node-actions operation-column">
              <!-- 编辑按钮，点击后打开编辑弹窗 -->
              <el-button type="primary" link @click="handleEdit(data)">编辑</el-button>
              <!-- 删除按钮，点击后执行删除操作 -->
              <el-button type="danger" link @click="handleDelete(data)">删除</el-button>
            </div>
          </div>
        </template>
      </el-tree>
    </div>
    
    <!-- 新增/编辑分类弹窗 -->
    <el-dialog 
      :title="dialogTitle"          
      v-model="dialogVisible"       
      width="500px"                
      @close="handleDialogClose"  
    >  <!-- 弹窗关闭时的回调 -->
      <!-- 表单组件，用于收集用户输入 -->
      <el-form 
        :model="form"           
        :rules="rules"            
        ref="formRef"            
        label-width="100px"         
      >
       <!-- 表单数据对象 -->
            <!-- 表单验证规则 -->
              <!--formRef 表单引用，用于调用表单方法 -->
             <!-- 表单标签宽度 -->

        <!-- 分类名称输入项 -->
        <el-form-item label="分类名称" prop="name">
          <!-- 输入框组件，双向绑定form.name -->
          <el-input v-model="form.name" placeholder="请输入分类名称"></el-input>
        </el-form-item>
        
        <!-- 父级分类选择项 -->
        <el-form-item label="父级分类" prop="fuid">
          <!-- 下拉选择组件，双向绑定form.fuid -->
          <el-select v-model="form.fuid" placeholder="请选择父级分类" clearable>
            <!-- 顶级分类选项 -->
            <el-option label="顶级分类" :value="0"></el-option>
            <!-- 动态生成的分类选项列表 -->
            
            <el-option
              v-for="item in classificationOptions"  
              :key="item.id"                          
              :label="item.name"                   
              :value="item.id"                       
            ></el-option>
            <!-- v-for="item in classificationOptions" 遍历可选分类列表 -->
                <!-- key每个选项的唯一标识 -->
                <!-- label选项显示文本 -->
                <!--value 选项值 -->
          </el-select>
        </el-form-item>
      </el-form>
      
      <!-- 弹窗底部操作按钮区域 -->
      <template #footer>
        <span class="dialog-footer">
          <!-- 取消按钮，点击后关闭弹窗 -->
          <el-button @click="dialogVisible = false">取消</el-button>
          <!-- 确定按钮，点击后提交表单 -->
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, FormInstance } from 'element-plus'
import {
  queryClassificationTree,
  queryClassification,
  addClassification,
  updateClassification,
  deleteClassification,
  queryClassificationById
} from '@/api/classification'

// 定义分类数据结构接口，描述每个分类节点的数据格式
interface Tree {
  id: number           // 分类唯一标识
  name: string         // 分类名称
  fuid: number         // 父级分类ID，0表示顶级分类
  ziList?: Tree[]      // 子分类列表，可选属性
}

// 响应式数据定义
// 树形数据，用于在el-tree组件中展示分类的层级结构
const treeData = ref<Tree[]>([])

// 所有分类的扁平结构数据，用于下拉选择框的数据源
const classifications = ref<Tree[]>([])

// 下拉框选项数据，在编辑分类时会排除当前分类及其子分类
const classificationOptions = ref<Tree[]>([])

// 表单实例引用，用于调用表单的验证、重置等方法
const formRef = ref<FormInstance>()

// 控制弹窗显示状态的响应式变量
const dialogVisible = ref(false)

// 弹窗标题，根据新增或编辑操作动态设置
const dialogTitle = ref('')

// 表单数据对象，使用reactive使其具有响应性
const form = reactive({
  id: 0,      // 分类ID，新增时为0，编辑时为实际ID
  name: '',   // 分类名称
  fuid: 0     // 父级分类ID，0表示顶级分类
})

// 表单验证规则，确保用户输入的有效性
const rules = {
  name: [
    { required: true, message: '请输入分类名称', trigger: 'blur' }
  ]
}

// 树形组件的配置项，告诉组件如何从数据中获取节点标签和子节点
const defaultProps = {
  children: 'ziList',  // 子节点属性名
  label: 'name'        // 节点标签属性名
}

// 当前操作类型，用于区分是新增还是编辑操作
const operationType = ref<'add' | 'edit'>('add')

// 获取树形分类数据，用于在页面上展示分类的层级结构
const loadTreeData = async () => {
  try {
    // 调用API获取树形分类数据
    const res = await queryClassificationTree()
    console.log('获取到的树形数据:', res.data)
    // 将获取到的数据赋值给treeData，更新页面显示
    treeData.value = res.data
  } catch (error) {
    // 错误处理，显示错误消息
    ElMessage.error('获取分类数据失败')
    console.error('获取数据失败:', error)
  }
}

// 获取所有分类数据，用于下拉选择框的数据源
const loadClassificationData = async () => {
  try {
    // 调用API获取所有分类的扁平数据
    const res = await queryClassification()
    // 将获取到的数据赋值给classifications
    classifications.value = res.data
  } catch (error) {
    // 错误处理，显示错误消息
    ElMessage.error('获取分类列表失败')
  }
}

// 处理树节点点击事件，目前仅输出日志，可扩展为查看详情等功能
const handleNodeClick = (data: Tree) => {
  console.log(data)
}

// 处理新增分类按钮点击事件
const handleAdd = () => {
  // 设置当前操作类型为新增
  operationType.value = 'add'
  // 设置弹窗标题
  dialogTitle.value = '新增分类'
  // 设置下拉框选项为所有分类（新增时可以选择任何分类作为父级）
  classificationOptions.value = classifications.value
  // 显示弹窗
  dialogVisible.value = true
}

// 处理编辑分类按钮点击事件
const handleEdit = (data: Tree) => {
  // 设置当前操作类型为编辑
  operationType.value = 'edit'
  // 设置弹窗标题
  dialogTitle.value = '编辑分类'
  
  // 将要编辑的分类数据填充到表单中
  form.id = data.id
  form.name = data.name
  form.fuid = data.fuid
  
  // 设置下拉框选项，排除当前分类及其子分类（防止将分类设为自己的子分类）
  classificationOptions.value = classifications.value.filter(item => !isDescendant(item, data))
  
  // 显示弹窗
  dialogVisible.value = true
}

// 检查item是否是data的子孙节点，用于防止分类嵌套错误
const isDescendant = (item: Tree, data: Tree): boolean => {
  // 如果item就是data本身，返回true
  if (item.id === data.id) return true
  // 如果item的父级是data，返回true
  if (item.fuid === data.id) return true
  // 递归检查data的子节点中是否包含item
  if (data.ziList && data.ziList.length > 0) {
    return data.ziList.some(child => isDescendant(item, child))
  }
  // 默认返回false
  return false
}

// 处理删除分类按钮点击事件
const handleDelete = (data: Tree) => {
  // 检查是否有子分类，如果有则不允许删除
  if (data.ziList && data.ziList.length > 0) {
    ElMessage.warning('该分类下有子分类，无法删除')
    return
  }
  
  // 弹出确认对话框，让用户确认是否删除
  ElMessageBox.confirm(
    `确定要删除分类 "${data.name}" 吗？`,  // 确认消息
    '提示',                               // 标题
    {
      confirmButtonText: '确定',          // 确认按钮文本
      cancelButtonText: '取消',           // 取消按钮文本
      type: 'warning'                     // 对话框类型
    }
  ).then(async () => {
    // 用户点击确定后的处理
    try {
      // 调用API删除分类
      await deleteClassification(data.id)
      // 显示删除成功消息
      ElMessage.success('删除成功')
      // 重新加载数据以更新页面显示
      loadTreeData()
      loadClassificationData()
    } catch (error) {
      // 错误处理，显示删除失败消息
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 用户点击取消或关闭对话框的处理
  })
}

// 提交表单处理函数
const submitForm = async () => {
  // 检查表单引用是否存在
  if (!formRef.value) return
  
  // 验证表单数据
  await formRef.value.validate(async (valid) => {
    // 如果表单验证通过
    if (valid) {
      try {
        // 根据操作类型执行不同的处理
        if (operationType.value === 'add') {
          // 新增分类操作
          await addClassification({
            name: form.name,
            fuid: form.fuid || 0  // 如果fuid为空则默认为0（顶级分类）
          })
          ElMessage.success('新增成功')
        } else {
          // 编辑分类操作
          await updateClassification({
            id: form.id,
            name: form.name,
            fuid: form.fuid || 0
          })
          ElMessage.success('编辑成功')
        }
        
        // 关闭弹窗
        dialogVisible.value = false
        // 重新加载数据以更新页面显示
        loadTreeData()
        loadClassificationData()
      } catch (error) {
        // 错误处理，根据操作类型显示不同的错误消息
        ElMessage.error(operationType.value === 'add' ? '新增失败' : '编辑失败')
      }
    }
  })
}

// 处理弹窗关闭事件，重置表单数据
const handleDialogClose = () => {
  // 重置表单验证状态和输入内容
  formRef.value?.resetFields()
  // 重置表单数据
  form.id = 0
  form.name = ''
  form.fuid = 0
}

// 初始化数据加载
loadTreeData()
loadClassificationData()
</script>

<style scoped>
/* 样式部分保持不变 */
.classification-container {
  padding: 20px;
  opacity: 0.8;
}

.tree-table {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  overflow: hidden;
}

.table-header {
  display: flex;
  align-items: center;
  background-color: #f5f7fa;
  border-bottom: 1px solid #ebeef5;
  padding: 12px 20px;
  font-weight: bold;
}

.header-item {
  flex: 1;
}

.operation-column {
  width: 200px;
  text-align: right;
}

.tree-table-body :deep(.el-tree-node) {
  border-bottom: 1px solid #ebeef5;
}

.tree-table-body :deep(.el-tree-node:last-child) {
  border-bottom: none;
}

.tree-table-body :deep(.el-tree-node__content) {
  padding: 12px 20px;
  border-bottom: 1px solid #ebeef5;
}

.tree-table-body :deep(.el-tree-node__content:hover) {
  background-color: #f5f7fa;
}

.tree-node-row {
  display: flex;
  align-items: center;
  width: 100%;
}

.node-label {
  flex: 1;
  font-size: 14px;
}

.node-actions {
  width: 200px;
  text-align: right;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
</style>