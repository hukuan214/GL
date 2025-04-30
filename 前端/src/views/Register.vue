<template>
    <div class="register-container">
        <div class="video-container">
            <video class="bg-video" autoplay loop muted>
                <source src="../videos/山顶风景.mp4" type="video/mp4">
            </video>
        </div>
        <div class="card">
            <h1>注册</h1>
            <form @submit.prevent="register">
                <div class="input-group">
                    <input v-model="username" placeholder="用户名" />
                    <span class="input-icon">👤</span>
                </div>
                <div class="input-group">
                    <input v-model="password" type="password" placeholder="密码" />
                    <span class="input-icon">🔒</span>
                </div>
                <button type="submit" class="btn-gradient">
                    <span>注册</span>
                </button>
            </form>
            <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
            <div v-if="successMessage" class="success-message">{{ successMessage }}</div>
        </div>
    </div>
</template>

<style scoped>
.register-container {
    height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    position: relative;
    overflow: hidden;
}

/* 替换原有的.bg-animation样式 */
.video-container {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    overflow: hidden;
    z-index: -1;
}

.bg-video {
    position: absolute;
    min-width: 100%;
    min-height: 100%;
    width: auto;
    height: auto;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
}

.card {
    background: rgba(255, 255, 255, 0.5); /* 将透明度从0.9改为0.7 */
    padding: 40px;
    border-radius: 20px;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
    width: 400px;
    box-sizing: border-box; /* 新增 */
    transform: translateY(0);
    transition: all 0.3s ease;
}

.input-group {
    position: relative;
    margin-bottom: 20px;
    width: 100%; /* 新增 */
}

input {
    width: calc(100% - 60px); /* 修改 */
    padding: 15px 15px 15px 45px;
    border: 2px solid #e0e0e0;
    border-radius: 50px;
    font-size: 16px;
    transition: all 0.3s;
    box-sizing: border-box; /* 新增 */
}

.card:hover {
    transform: translateY(-5px);
    box-shadow: 0 15px 35px rgba(0, 0, 0, 0.15);
}

h1 {
    color: #2e7d32;
    text-align: center;
    margin-bottom: 30px;
    font-size: 28px;
}

.input-group {
    position: relative;
    margin-bottom: 20px;
}

input {
    width: 100%;
    padding: 15px 15px 15px 45px;
    border: 2px solid #e0e0e0;
    border-radius: 50px;
    font-size: 16px;
    transition: all 0.3s;
}

input:focus {
    border-color: #66bb6a;
    outline: none;
    box-shadow: 0 0 0 3px rgba(102, 187, 106, 0.2);
}

.input-icon {
    position: absolute;
    left: 15px;
    top: 50%;
    transform: translateY(-50%);
    font-size: 20px;
}

.error-message {
    color: #e53935;
    text-align: center;
    margin-top: 15px;
}

.success-message {
    color: #2e7d32;
    text-align: center;
    margin-top: 15px;
    font-weight: bold;
}

/* 复用之前定义的btn-gradient和gradientBG动画 */
.btn-gradient {
    position: relative;
    padding: 15px 30px;
    border: none;
    border-radius: 50px;
    background: linear-gradient(to right, #2e7d32, #66bb6a);
    color: white;
    font-size: 16px;
    cursor: pointer;
    width: 100%;
    transition: all 0.3s;
    box-shadow: 0 4px 15px rgba(46, 125, 50, 0.3);
    overflow: hidden;
}

.btn-gradient:hover {
    transform: translateY(-3px);
    box-shadow: 0 7px 20px rgba(46, 125, 50, 0.4);
}

.btn-gradient span {
    position: relative;
    z-index: 1;
}

.btn-gradient::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: linear-gradient(to right, #66bb6a, #2e7d32);
    opacity: 0;
    transition: opacity 0.3s;
}

.btn-gradient:hover::before {
    opacity: 1;
}
@keyframes gradientBG {
    /* 保持与RegisterLoginChoice.vue相同的动画 */
}
</style>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            username: '',
            password: '',
            errorMessage: '',
            successMessage: ''
        };
    },
    methods: {
        register() {
            if (!this.username || !this.password) {
                this.errorMessage = '用户名和密码不能为空';
                return;
            }
            
            // 新增验证：必须是邮箱或手机号
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            const phoneRegex = /^1[3-9]\d{9}$/;
            
            if (!emailRegex.test(this.username) && !phoneRegex.test(this.username)) {
                this.errorMessage = '用户名必须是有效的邮箱或手机号';
                return;
            }
            
            // 新增密码复杂度验证
            const passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,15}$/;
            if (!passwordRegex.test(this.password)) {
                this.errorMessage = '密码必须为6-15位且包含数字、大小写字母';
                return;
            }
            
            axios.post('/api/register', {
                username: this.username,
                password: this.password
            })
            .then(response => {
                if (response.data && response.data.message) {
                    this.successMessage = '注册成功，正在跳转登录页面';
                    this.errorMessage = '';
                    setTimeout(() => {
                        this.$router.push('/login');
                    }, 2000);
                }
            })
            .catch(error => {
                if (error.response && error.response.status === 400) {
                    this.errorMessage = '用户名已存在，请使用其他用户名';
                } else {
                    this.errorMessage = '注册失败，请稍后重试';
                }
                this.successMessage = '';
            });
        }
    }
};
</script>