import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    proxy: {
      // 匹配所有以/api的请求
      '/api': {
        // 所有以/api的请求，将转发给指定服务处理
        target: 'http://localhost:8080/',
        // 是否开启ssl安全加密处理
        secure: false,
        // 是否做跨域处理
        changeOrigin: true,
        // 路径重写，会将请求路径中的 /api 替换成''
        rewrite: (path) => path.replace(/^\/api/, ''),
      }
    }
  }
})
