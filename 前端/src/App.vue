<template>
  <div id="app">
    <!-- 根据路由元信息决定是否显示导航栏 -->
    <navbar v-if="!shouldHideNavbar" msg2="AAA"></navbar>
    <div class="container">
      <!-- 根据路由元信息决定是否显示左边栏 -->
      <!-- <div class="left" v-if="!shouldHideNavbar">
        <leftnavbar></leftnavbar>
      </div> -->
      <div class="right" style="width: 100%;">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      user: {
        id: 1
      },
    };
  },
  computed: {
    shouldHideNavbar() {
      return this.$route.meta.hideNavbar;
    }
  },
  created() {
    // 路由守卫，在每次路由跳转前执行
    this.$router.beforeEach((to, from, next) => {
      // 判断当前路由是否需要隐藏导航栏和左边栏
      next();
    });
  },
  methods: {
    test() {
      this.$axios.get("/api/selectAll")
        .then(response => {
          this.user = response.data;
          alert(response.data);
        });
    }
  },
  components: {
    // 异步加载导航栏组件
    navbar: () => import('./views/Navbar.vue'),
    leftnavbar: () => import('./views/Left.vue')
  }
};
</script>

<style>
body {
  margin: 0;
  font-family: Arial, sans-serif;
  background-color: #fcfcfc;
}

.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #041221;
  padding: 10px 20px;
}

.navbar .logo img {
  height: 60px;
}

.navbar .links {
  display: flex;
  list-style-type: none;
}

.navbar .links li {
  margin: 0 15px;
}

.navbar .avatar img {
  border-radius: 50%;
  width: 60px;
  height: 60px;
}

/* 全局响应式样式 */
@media (max-width: 768px) {
  .container {
    padding: 10px;
  }
  
  h1 {
    font-size: 1.5rem;
  }
  
  table {
    display: block;
    overflow-x: auto;
    -webkit-overflow-scrolling: touch;
  }
  
  input, textarea, button {
    font-size: 0.9rem;
  }
}

@media (max-width: 480px) {
  h1 {
    font-size: 1.3rem;
  }
  
  .card {
    padding: 15px;
    border-radius: 0;
    box-shadow: none;
  }
}

.right {
  background: url('../src/images/用户端背景.jpg') no-repeat center center fixed;
  background-size: cover;
  min-height: 100vh;
  position: relative;
}

.right::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.363);
  z-index: 0;
}

.right > * {
  position: relative;
  z-index: 1;
}
</style>
