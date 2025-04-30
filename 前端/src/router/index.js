import Vue from 'vue';
import VueRouter from 'vue-router';
import RegisterLoginChoice from '../views/RegisterLoginChoice.vue';
import Register from '../views/Register.vue';
import Login from '../views/Login.vue';
import Admin from '../views/Admin.vue';
import GreenRecommendation from '../views/GreenRecommendation.vue';
import GarbageClassification from '../views/GarbageClassification.vue';
import LowCarbonLife from '../views/LowCarbonLife.vue';
import ReservationOrder from '../views/ReservationOrder.vue';
import CarbonCalculator from '../views/CarbonCalculator.vue';
import GreenForum from '../views/GreenForum.vue';

Vue.use(VueRouter);

// 重写 push 和 replace 方法，捕获并忽略重复导航的错误
const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => {
    if (err.name === 'NavigationDuplicated') {
      return err;
    }
    throw err;
  });
};

const originalReplace = VueRouter.prototype.replace;
VueRouter.prototype.replace = function replace(location) {
  return originalReplace.call(this, location).catch(err => {
    if (err.name === 'NavigationDuplicated') {
      return err;
    }
    throw err;
  });
};

const routes = [
  {
    path: '/',
    redirect: '/register-login-choice'
  },
  {
    path: '/register-login-choice',
    name: 'RegisterLoginChoice',
    component: RegisterLoginChoice,
    meta: { hideNavbar: true } // 添加 hideNavbar 元信息
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
    meta: { hideNavbar: true } // 添加 hideNavbar 元信息
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { hideNavbar: true } // 添加 hideNavbar 元信息
  },
  {
    path: '/admin',
    name: 'Admin',
    component: Admin,
    meta: { requiresAuth: true, role: 'admin', hideNavbar: true } // 已有 hideNavbar 元信息
  },
  {
    path: '/green-recommendation',
    name: 'GreenRecommendation',
    component: GreenRecommendation,
    meta: { requiresAuth: true, role: 'user' }
  },
  {
    path: '/garbage-classification',
    name: 'GarbageClassification',
    component: GarbageClassification
  },
  {
    path: '/low-carbon-life',
    name: 'LowCarbonLife',
    component: LowCarbonLife
  },
  {
    path: '/reservation-order',
    name: 'ReservationOrder',
    component: ReservationOrder
  },
  {
    path: '/carbon-calculator',
    name: 'CarbonCalculator',
    component: CarbonCalculator
  },
  {
    path: '/green-forum',
    name: 'GreenForum',
    component: GreenForum
  }
];

const router = new VueRouter({
  routes
});

router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth) {
    const userRole = localStorage.getItem('userRole');
    if (!userRole) {
      next('/login');
    } else if (to.meta.role!== userRole) {
      next('/login');
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router;
