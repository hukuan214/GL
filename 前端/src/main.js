// 引入 Vue 核心库
import Vue from 'vue'
// 引入根组件 App.vue
import App from './App.vue'
// 引入路由配置
import router from './router'
// 引入状态管理库 Vuex 的 store 实例
import store from './store'
// 引入路由支持工具（如果有）
import routerSupport from './router/routerSupport'
import axios from 'axios';
// 移除plugins导入

// 设置axios的baseURL
axios.defaults.baseURL = 'http://localhost:8088';

// 关闭 Vue 在生产环境下的提示信息
Vue.config.productionTip = false

// 将 routerSupport 挂载到 Vue 原型上，以便在组件中可以通过 this.to 使用
Vue.prototype.to = routerSupport

// 创建 Vue 实例
new Vue({
  // 注入路由配置
  router,
  // 注入状态管理库
  store,
  // 使用 render 函数渲染根组件 App
  render: h => h(App)
// 将 Vue 实例挂载到 id 为 app 的 DOM 元素上
}).$mount('#app')