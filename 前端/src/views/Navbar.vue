<template>
    <div id="app">
        <nav class="navbar">
            <div class="logo">
                <img src="../images/logo.png" alt="公司Logo"> <!-- 替换为你的logo图片路径 -->
            </div>
            <ul class="links">
                <li>
                    <a @click="greenRecommendation" :class="{ active: activeMenu === 'greenRecommendation' }">绿色推荐</a>
                </li>
                <li>
                    <a @click="garbageClassification" :class="{ active: activeMenu === 'garbageClassification' }">垃圾分类</a>
                </li>
                <li>
                    <a @click="lowCarbonLife" :class="{ active: activeMenu === 'lowCarbonLife' }">低碳生活</a>
                </li>
                <li>
                    <a @click="reservationOrder" :class="{ active: activeMenu === 'reservationOrder' }">预约下单</a>
                </li>
                <li>
                    <a @click="carbonCalculator" :class="{ active: activeMenu === 'carbonCalculator' }">减碳计算器</a>
                </li>
                <li>
                    <a @click="greenForum" :class="{ active: activeMenu === 'greenForum' }">绿色论坛</a>
                </li>
            </ul>
            <div class="avatar" @click="logout">
                <span>退出登录</span>
            </div>
        </nav>
    </div>
</template>

<script>
export default {
    props: {
        msg2: null
    },
    data() {
        return {
            activeMenu: null, // 存储当前选中的菜单
        };
    },
    methods: {
        greenRecommendation() {
            // 这里可以添加跳转到查看绿色产品推荐页面的逻辑
            // 例如使用路由跳转
            this.$router.push('/green-recommendation');
            this.activeMenu = 'greenRecommendation';
        },
        garbageClassification() {
            // 跳转到垃圾分类介绍和指南页面
            this.$router.push('/garbage-classification');
            this.activeMenu = 'garbageClassification';
        },
        lowCarbonLife() {
            // 跳转到低碳生活页面
            this.$router.push('/low-carbon-life');
            this.activeMenu = 'lowCarbonLife';
        },
        reservationOrder() {
            // 跳转到预约下单页面
            this.$router.push('/reservation-order');
            this.activeMenu = 'reservationOrder';
        },
        carbonCalculator() {
            // 跳转到减碳计算器页面
            this.$router.push('/carbon-calculator');
            this.activeMenu = 'carbonCalculator';
        },
        greenForum() {
            // 跳转到绿色论坛页面
            this.$router.push('/green-forum');
            this.activeMenu = 'greenForum';
        },
        logout() {
            localStorage.removeItem('userRole');
            this.$router.push('/login');
        }
    }
};
</script>

<style scoped>
    .test .test2 {
        width: 100px;
        height: 100px;
        background-color: gray;
    }

    .test {
        width: 200px;
        height: 200px;
        background-color: red;
    }

    body {
        margin: 0;
        font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
    }

    .navbar {
        display: flex;
        justify-content: space-between;
        align-items: center;
        background: linear-gradient(135deg, #4CAF50 0%, #2E7D32 100%);
        padding: 12px 40px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        position: relative;
        z-index: 1000;
    }

    .navbar .logo img {
        height: 50px;
        transition: transform 0.3s;
    }

    .navbar .logo:hover img {
        transform: scale(1.05);
    }

    .navbar .links {
        display: flex;
        list-style-type: none;
        margin: 0;
        padding: 0;
    }

    .navbar .links li {
        margin: 0 12px;
        position: relative;
    }

    .navbar .links a {
        color: white;
        text-decoration: none;
        padding: 12px 16px;
        display: block;
        font-weight: 500;
        letter-spacing: 0.5px;
        transition: all 0.3s ease;
        border-radius: 6px;
    }

    .navbar .links a:hover {
        background-color: rgba(255, 255, 255, 0.15);
        transform: translateY(-2px);
    }

    .navbar .links a.active {
        background-color: rgba(255, 255, 255, 0.25);
        transform: translateY(-2px);
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    }

    .avatar {
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        width: 110px;
        height: 44px;
        background: linear-gradient(135deg, #FF5722 0%, #E64A19 100%);
        color: white;
        border-radius: 6px;
        font-weight: 500;
        transition: all 0.3s ease;
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    }

    .avatar:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    }

    @media (max-width: 768px) {
        .navbar {
            flex-direction: column;
            padding: 15px;
        }
        
        .links {
            flex-wrap: wrap;
            justify-content: center;
            margin: 15px 0;
        }
        
        .links li {
            margin: 8px;
        }

        .avatar {
            margin-top: 15px;
        }
    }
</style>