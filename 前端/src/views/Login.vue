<template>
    <div class="login-container">
        <div class="video-container">
            <video class="bg-video" autoplay loop muted>
                <source src="../videos/山顶风景.mp4" type="video/mp4">
            </video>
        </div>
        <div class="card">
            <h1>登录</h1>
            <form @submit.prevent="login">
                <div class="input-group">
                    <input v-model="username" placeholder="用户名" />
                    <span class="input-icon">👤</span>
                </div>
                <div class="input-group">
                    <input v-model="password" type="password" placeholder="密码" />
                    <span class="input-icon">🔒</span>
                </div>
                <button type="submit" class="btn-gradient">
                    <span>登录</span>
                </button>
            </form>
            <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
        </div>
    </div>
</template>

<style scoped>
.login-container {
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
    box-sizing: border-box; /* 新增 */
}

h1 {
    color: #2e7d32;
    text-align: center;
    margin-bottom: 30px;
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
}

@keyframes gradientBG {
    0% { background-position: 0% 50%; }
    50% { background-position: 100% 50%; }
    100% { background-position: 0% 50%; }
}

.bg-animation {
    animation: gradientBG 15s ease infinite;
}
</style>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            username: '',
            password: '',
            errorMessage: ''
        };
    },
    methods: {
        login() {
            axios.post('/api/login', {
                username: this.username,
                password: this.password
            })
           .then(response => {
                if (response.data.error) {
                    this.errorMessage = response.data.error;
                } else {
                    localStorage.setItem('userRole', response.data.role);
                    if (response.data.role === 'admin') {
                        this.$router.push('/admin');
                    } else {
                        this.$router.push('/green-recommendation');
                    }
                }
            })
           .catch(error => {
                this.errorMessage = '登录失败，请检查用户名和密码';
            });
        }
    }
};
</script>