<template>
    <div class="choice-container">
        <div class="video-container">
            <video class="bg-video" autoplay loop muted>
                <source src="../videos/山顶风景.mp4" type="video/mp4">
            </video>
        </div>
        <div class="card">
            <h1>欢迎来到绿色生活平台</h1>
            <div class="system-intro">
                <p>绿色生活平台是一个致力于推广环保理念、倡导低碳生活的综合性平台。</p>
                <p>平台功能包括：</p>
                <ul>
                    <li>• 绿色产品推荐与购买</li>
                    <li>• 垃圾分类指南查询</li>
                    <li>• 低碳生活小贴士</li>
                    <li>• 环保活动信息发布</li>
                    <li>• 碳足迹计算器</li>
                    <li>• 绿色论坛交流</li>  <!-- 新增绿色论坛功能点 -->
                </ul>
                <p>加入我们，一起为地球环保贡献力量！</p>
            </div>
            <div class="button-group">
                <button class="btn-gradient" @click="$router.push('/register')">
                    <span>注册</span>
                </button>
                <button class="btn-gradient" @click="$router.push('/login')">
                    <span>登录</span>
                </button>
            </div>
        </div>
    </div>
</template>

<style scoped>
.choice-container {
    height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    position: relative;
    overflow: hidden;
}

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
    text-align: center;
    width: 400px;
    transform: translateY(0);
    transition: all 0.3s ease;
}

.card:hover {
    transform: translateY(-5px);
    box-shadow: 0 15px 35px rgba(0, 0, 0, 0.15);
}

h1 {
    color: #2e7d32;
    margin-bottom: 30px;
    font-size: 28px;
}

.button-group {
    display: flex;
    flex-direction: column;
    gap: 20px;
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
    overflow: hidden;
    transition: all 0.3s;
    box-shadow: 0 4px 15px rgba(46, 125, 50, 0.3);
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
    0% { background-position: 0% 50%; }
    50% { background-position: 100% 50%; }
    100% { background-position: 0% 50%; }
}

.system-intro {
    margin: 20px 0;
    text-align: left;
    color: #333;
    line-height: 1.6;
}

.system-intro p {
    margin-bottom: 10px;
}

.system-intro ul {
    padding-left: 20px;
    margin: 10px 0;
}

.system-intro li {
    margin-bottom: 8px;
}
</style>