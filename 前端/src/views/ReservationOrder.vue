<template>
  <div class="reservation-order">
    <h1>预约下单</h1>
    <!-- 新增轮播图 -->
    <div class="carousel">
      <!-- 左箭头 -->
      <button class="carousel-arrow prev" @click="prevSlide" :disabled="currentIndex === 0">
        &lt;
      </button>
      <div class="carousel-inner" :style="{ transform: `translateX(-${currentIndex * 100}%)` }">
        <div  v-for="(product, index) in greenProducts" :key="index" class="carousel-item">
          <!-- 假设 product.image 是 Base64 编码的图片数据，添加点击事件 -->
          <img :src="`data:image/png;base64,${product.image}`" :alt="product.name" @click="setProductName(product.name)" />
          <div class="product-name">{{ product.name }}</div>
        </div>
      </div>
      <!-- 右箭头 -->
      <button class="carousel-arrow next" @click="nextSlide" :disabled="currentIndex === greenProducts.length - 1">
        &gt;
      </button>
      <!-- 指示器 -->
      <div class="carousel-indicators">
        <span
          v-for="(product, index) in greenProducts"
          :key="index"
          :class="{ active: index === currentIndex }"
          @click="currentIndex = index"
        ></span>
      </div>
    </div>
    <form @submit.prevent="submitOrder">
      <input v-model="productName" placeholder="商品名称" />
      <input v-model.number="quantity" type="number" min="1" placeholder="商品数量" />
      <input v-model="phoneNumber" placeholder="手机号码" maxlength="11" /> <!-- 新增手机号输入 -->
      <textarea v-model="address" placeholder="收货信息"></textarea>
      <div v-if="errorMessage" :class="{ 'fade-out-error': errorMessage }">{{ errorMessage }}</div>
      <button type="submit" class="custom-submit-btn">提交订单</button>
    </form>
    <!-- 修改为根据 showSuccessMessage 控制显示，并添加 fade-out 类 -->
    <div class="success-messages" v-if="showSuccessMessage">
      <div :class="{ 'fade-out':!isMessageVisible }">{{ successMessages[0] }}</div>
    </div>
    <!-- 在提交订单表单下方添加 -->
    <div v-if="rejectedOrders.length > 0" class="rejected-orders">
      <h3>被拒绝的订单</h3>
      <table>
        <thead>
          <tr>
            <th>商品名称</th>
            <th>数量</th>
            <th>下单日期</th>
            <th>手机号</th>
            <th>拒绝原因</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in rejectedOrders" :key="order.id">
            <td>{{ order.product_name }}</td>
            <td>{{ order.quantity }}</td>
            <td>{{ formatDate(order.order_date) }}</td>
            <td>{{ order.phone_number }}</td>
            <td class="rejection-reason">{{ order.rejection_reason }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      productName: '',
      quantity: 1,
      phoneNumber: '', // 新增手机号字段
      address: '',
      successMessages: [],
      errorMessage: '',
      showSuccessMessage: false, // 控制成功消息的显示
      isMessageVisible: true ,// 控制消息是否开始淡出
      rejectedOrders: [], // 新增被拒绝订单列表
      greenProducts: [], // 新增绿色产品列表
      currentIndex: 0, // 新增当前轮播图索引
      carouselInterval: null // 新增轮播定时器
    };
  },
  methods: {
    fetchRejectedOrders() {
      axios.get('/api/rejected-orders')
        .then(response => {
          // 处理手机号，中间4位替换为*
          this.rejectedOrders = response.data.map(order => {
            if (order.phone_number && order.phone_number.length === 11) {
              order.phone_number = order.phone_number.substring(0, 3) + '****' + order.phone_number.substring(7);
            }
            return order;
          });
        })
        .catch(error => {
          console.error('获取被拒绝订单失败:', error);
        });
    },
    submitOrder() {
      if (!this.productName || !this.address || !this.phoneNumber) {
        this.errorMessage = '商品名称、手机号和收货信息都不能为空';
        // 设置定时器，3 秒后清空错误信息，与动画时长匹配
        setTimeout(() => {
          this.errorMessage = '';
        }, 3000); 
        return;
      }
      if (this.quantity < 1) {
        this.errorMessage = '商品数量必须大于或等于 1';
        // 设置定时器，3 秒后清空错误信息，与动画时长匹配
        setTimeout(() => {
          this.errorMessage = '';
        }, 3000); 
        return;
      }
      // 验证手机号格式
      if (!/^1[3-9]\d{9}$/.test(this.phoneNumber)) {
        this.errorMessage = '请输入正确的手机号码';
        // 设置定时器，3 秒后清空错误信息，与动画时长匹配
        setTimeout(() => {
          this.errorMessage = '';
        }, 3000); 
        return;
      }
      
      axios.post('/api/reservation-orders', {
        product_name: this.productName,
        quantity: this.quantity,
        address: this.address,
        phone_number: this.phoneNumber // 新增手机号字段
      })
      .then(response => {
        this.successMessages = [];
        this.successMessages.push('订单提交成功，等待管理员审核，审核通过快递将到付发出');
        this.productName = '';
        this.quantity = 1;
        this.phoneNumber = ''; // 清空手机号
        this.address = '';
        this.errorMessage = '';
        this.showSuccessMessage = true;
        this.isMessageVisible = true;
        // 设置定时器，3 秒后开始淡出
        setTimeout(() => {
          this.isMessageVisible = false;
          // 淡出动画结束后隐藏消息，调整为 3000ms
          setTimeout(() => {
            this.showSuccessMessage = false;
          }, 3000); 
        }, 3000);
      })
      .catch(error => {
        this.errorMessage = '订单提交失败，请稍后重试';
        console.error('提交订单时出错:', error);
      });
    },
    
    formatDate(dateString) {
      if (!dateString) return '';
      return new Date(dateString).toLocaleDateString();
    },
    // 新增获取绿色产品数据方法
    fetchGreenProducts() {
      axios.get('/api/green-products')
        .then(response => {
          this.greenProducts = response.data;
          // 启动轮播
          this.startCarousel();
        })
        .catch(error => {
          console.error('获取绿色产品数据失败:', error);
        });
    },
    // 新增启动轮播方法
    startCarousel() {
      this.carouselInterval = setInterval(() => {
        this.currentIndex = (this.currentIndex + 1) % this.greenProducts.length;
      }, 3000); // 每3秒切换一次
    },
    // 新增停止轮播方法
    stopCarousel() {
      clearInterval(this.carouselInterval);
    },
    // 新增上一张幻灯片方法
    prevSlide() {
      if (this.currentIndex > 0) {
        this.currentIndex--;
      }
    },
    // 新增下一张幻灯片方法
    nextSlide() {
      if (this.currentIndex < this.greenProducts.length - 1) {
        this.currentIndex++;
      }
    },
    // 新增方法，用于将产品名称设置到输入框
    // 修改 setProductName 方法
    setProductName(name) {
      if (this.productName === name) {
        this.productName = '';
      } else {
        this.productName = name;
      }
    },
  },
  created() {
    this.fetchRejectedOrders();
    this.fetchGreenProducts();
  },
  beforeDestroy() {
    this.stopCarousel();
  }
};
</script>

<style scoped>
.reservation-order {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
  /* 确保父容器可以完全展开 */
  width: 100%; 
  box-sizing: border-box;
}

.carousel {
  /* 确保继承父容器宽度 */
  width: 100%; 
  height: 300px;
  overflow: hidden;
  margin-bottom: 20px;
  /* 确保盒模型计算正确 */
  box-sizing: border-box; 
  /* 添加相对定位 */
  position: relative; 
}

.reservation-order h1 {
  text-align: center;
  color: #2e7d32; /* 修改为绿色主题色 */
  margin-bottom: 20px;
}

.reservation-order input,
.reservation-order textarea {
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.reservation-order input[type="tel"] {
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

/* .reservation-order button {
  width: 100%;
   padding: 10px; 
  background-color: #4caf4f4d; 
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
} */

.reservation-order button:hover {
  background-color: #388e3c; /* 修改为深绿色悬停效果 */
}

.reservation-order div {
  margin-top: 10px;
  color: #2f81d39b;
}

.success-messages {
  color: #2e7d32; /* 修改为绿色主题色 */
  background-color: #e8f5e9; /* 添加浅绿色背景 */
  padding: 15px;
  border-radius: 4px;
  margin: 15px 0;
}

.fade-out-error {
  animation: fadeOutError 3.5s ease-out forwards; 
}

@keyframes fadeOutError {
  from {
    opacity: 1;
  }
  to {
    opacity: 0;
  }
}

.rejected-orders {
  margin: 30px auto;
  padding: 20px;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  width: 90%;
  max-width: 800px;
}

.rejected-orders h3 {
  color: #2e7d32;
  margin-bottom: 20px;
  text-align: center;
  font-size: 1.3em;
  font-weight: 600;
}

.rejected-orders table {
  width: 100%;
  border-collapse: collapse;
  margin: 0 auto;
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
}

.rejected-orders th {
  background-color: #e8f5e9;
  color: #2e7d32;
  padding: 14px;
  text-align: center;
  font-weight: 600;
  font-size: 1.05em;
}

.rejected-orders td {
  padding: 12px;
  border-bottom: 1px solid #e8f5e9;
  text-align: center;
  color: #333;
}

.rejected-orders tr:last-child td {
  border-bottom: none;
}

.rejected-orders tr:hover {
  background-color: #f1f8e9;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .rejected-orders {
    padding: 15px;
    width: 95%;
  }
  
  .rejected-orders th, 
  .rejected-orders td {
    padding: 10px;
    font-size: 0.95em;
  }
}

.rejected-orders td.rejection-reason {
  color: #d32f2f; /* 红色文字 */
  font-weight: 500;
}

/* 新增轮播图样式 */
.carousel {
  width: 100%;
  height: 300px;
  overflow: hidden;
  margin-bottom: 20px;
}

.carousel-arrow {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  padding: 10px;
  font-size: 20px;
  cursor: pointer;
  z-index: 10;
}

.carousel-arrow.prev {
  left: 10px !important;
}

.carousel-arrow.next {
  right: 10px !important;
}

.carousel-arrow:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* 指示器样式 */
.carousel-indicators {
  position: absolute;
  bottom: 10px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 10px;
}

.carousel-indicators span {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.5);
  cursor: pointer;
}

.carousel-indicators span.active {
  background-color: white;
}

/* 优化过渡动画 */
.carousel-inner {
  display: flex;
  transition: transform 0.8s ease-in-out; /* 调整过渡时间和效果 */
  height: 100%;
}
.carousel-item {
  flex: 0 0 100%;
  position: relative;
}

.carousel-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-name {
  position: absolute;
  bottom: 10px;
  right: 10px;
  background-color: rgba(0, 0, 0, 0.7);
  /* 修改为你想要的颜色，这里以黄色为例 */
  color: #FFFF00; 
  padding: 5px 10px;
  border-radius: 4px;
}
.custom-submit-btn {
  /* 参照 CarbonCalculator.vue 的按钮样式 */
  padding: 10px 20px;
  background-color: #4caf50; 
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
  /* 按钮居中 */
  margin: 0 auto; 
  display: block; 
  width: 102%; 
}

.custom-submit-btn:hover {
  background-color: #388e3c; /* 深绿色悬停效果 */
}
</style>