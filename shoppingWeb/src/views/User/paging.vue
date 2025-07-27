<script setup lang="ts">
import { ref } from 'vue'
import type { ComponentSize } from 'element-plus'
// 分页  参数：当前页、每页多少条，每次查询，需要将这参数传入后台
// 总条数数据存储
const total = ref(0);
//当前页
const Page = ref(1);
const pageSize = ref(5);
const size = "default";
const disabled = false;
const background = true;

// val 每页显示的条数
const handleSizeChange = (val: number) => {
  pageSize.value = val;
 Page.value = 1;
  // 执行搜索
//   queryPages();
};
// val  当前页
const handleCurrentChange = (val: number) => {
    Page.value = val;
  // 执行搜索
//   queryPages();
};

</script>

<template>
<div>
     <!-- <el-pagination
      v-model:current-page="Page"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 30, 40]"
      :size="size"
      :disabled="disabled"
      :background="background"
      layout="total, sizes, prev, pager, next, jumper"
      :total="0"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    /> -->


</div>
</template>

<style scoped>

</style>