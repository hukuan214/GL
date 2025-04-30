<template>
  <div class="carbon-calculator">
    <h1>减碳计算器</h1>
    <form @submit.prevent="calculateCarbonFootprint">
      <div class="form-group">
        <label>出行方式</label>
        <select v-model="selectedTravelType">
          <option v-for="travelType in travelTypes" :key="travelType.id" :value="travelType">
            {{ travelType.travel_type }}
          </option>
        </select>
      </div>
      <div class="form-group">
        <label>距离(km)</label>
        <input v-model.number="distance" type="number" min="0" placeholder="（公里）" />
      </div>
      <button type="submit">计算碳足迹</button>
    </form>
    <div v-if="carbonFootprint" class="result-container">
      <!-- <p>今日减少的碳排放是: {{ carbonFootprint }} 千克二氧化碳当量</p> -->
      <p>🌱 恭喜！您今日减少了 {{ carbonFootprint }} 千克二氧化碳排放，为地球减负啦！</p>
      <p v-if="carbonFootprint < 1" class="suggestion">✨ 小贴士：短途出行试试步行或骑行吧！既能锻炼身体，又能收获沿途风景~</p>
      <p v-else-if="carbonFootprint < 5" class="suggestion">🚌 太棒了！您的选择让城市少了一辆私家车的尾气，继续保持绿色出行好习惯！</p>
      <p v-else-if="carbonFootprint < 10" class="suggestion">🥗 环保达人！如果每周少吃一天肉，相当于一年种了10棵树哦！</p>
      <p v-else class="suggestion">🏆 减碳冠军！您的贡献相当于种了一片小森林！试试关掉不用的电器，让节能成为新时尚~</p>
    </div>
    
    
    <!-- 修改后 -->
    <!-- 修改说明部分 -->
    <div class="calculator-info">
      <div class="info-card">
        <h3>🌱 减碳计算器说明</h3>
        <div class="info-grid">
          <div class="info-section">
            <p>本计算器通过分析您的出行方式与距离，估算您减少的碳排放量。</p>
          </div>
          
          <div class="info-section">
            <h4><i class="fas fa-cogs"></i> 工作原理</h4>
            <ul class="styled-list">
              <li><i class="fas fa-check-circle"></i> 系统预置了各种出行方式的单位减碳量(kg/km)</li>
              <li><i class="fas fa-check-circle"></i> 计算公式：减碳量 = 出行距离 × 单位减碳量</li>
              <li><i class="fas fa-check-circle"></i> 数据来源：基于国际通用的碳排放计算标准</li>
            </ul>
          </div>
          
          <div class="info-section">
            <h4><i class="fas fa-book-open"></i> 使用方法</h4>
            <ol class="styled-list">
              <li><i class="fas fa-arrow-right"></i> 选择您的出行方式</li>
              <li><i class="fas fa-arrow-right"></i> 输入出行距离(公里)</li>
              <li><i class="fas fa-arrow-right"></i> 点击"计算碳足迹"按钮</li>
            </ol>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      travelTypes: [],
      selectedTravelType: null,
      distance: 0,
      carbonFootprint: null
    };
  },
  mounted() {
    this.fetchTravelTypes();
  },
  methods: {
    fetchTravelTypes() {
      axios.get('/api/carbon-calculator/travel-types')
        .then(response => {
          this.travelTypes = response.data;
        })
        .catch(error => {
          console.error('获取出行方式数据时出错:', error);
        });
    },
    calculateCarbonFootprint() {
      if (this.selectedTravelType && this.distance > 0) {
        this.carbonFootprint = this.selectedTravelType.carbon_reduction_per_unit * this.distance;
      } else {
        this.carbonFootprint = null;
      }
    }
  }
};
</script>

<style scoped>
/* 添加/修改样式 */
.carbon-calculator {
  max-width: 1200px; /* 增大整体宽度 */
  margin: 0 auto;
  padding: 20px;
}

.calculator-layout {
  display: flex;
  gap: 20px;
  align-items: flex-start;
}

form {
  flex: 1;
  min-width: 350px;
  margin-bottom: 0;
}

.calculator-info {
  flex: 1;
  min-width: 350px;
  margin-top: 0;
}

/* 保持其他样式不变 */
.calculator-info {
  margin-top: 20px;
  background: #f8f9fa;
  border-radius: 10px;
  padding: 15px;
}



.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 15px;
}

.info-section {
  padding: 12px;
  background: #f8f9fa;
  border-radius: 6px;
  border-left: 3px solid #4CAF50;
}

.info-section h4 {
  font-size: 16px;
  margin-bottom: 8px;
  color: #2e7d32;
}

.styled-list {
  padding-left: 5px;
  font-size: 14px;
}

.styled-list li {
  margin-bottom: 6px;
}

.carbon-calculator h1 {
  text-align: center;
  color: #2e7d32; /* 绿色主题色 */
  margin-bottom: 30px;
}

form {
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.form-group {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  width: 100%;
  max-width: 400px;
}

.form-group label {
  width: 80px;
  margin-right: 10px;
  text-align: right;
  color: #2e7d32;
}

.form-group select,
.form-group input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

button {
  padding: 10px 20px;
  background-color: #4caf50; /* 绿色按钮 */
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #388e3c; /* 深绿色悬停效果 */
}

div p {
  margin: 10px 0;
  color: #2e7d32; /* 绿色文字 */
}

.result-container {
  text-align: center;
}

.suggestion {
  margin: 10px auto;
  max-width: 80%;
}

/* 添加在style部分 */
.calculator-info {
  margin-top: 30px;
  background: #f8f9fa;
  border-radius: 10px;
  padding: 20px;
}

.info-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}

.info-section {
  margin: 15px 0;
  padding: 15px;
  border-left: 4px solid #4CAF50;
  background: #f8f9fa;
  border-radius: 4px;
}

.info-section h4 {
  color: #2e7d32;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
}

.styled-list {
  padding-left: 20px;
}

.styled-list li {
  margin-bottom: 8px;
  display: flex;
  align-items: center;
}

.styled-list i {
  margin-right: 8px;
  color: #4CAF50;
}

.calculator-info h3 {
  color: #2e7d32;
  margin-bottom: 15px;
}

.calculator-info ul, 
.calculator-info ol {
  padding-left: 20px;
}

.calculator-info li {
  margin-bottom: 8px;
}

@media (max-width: 768px) {
  .form-container {
    padding: 15px;
  }
  
  .form-group {
    flex-direction: column;
  }
  
  .form-group label {
    text-align: left;
    margin-bottom: 5px;
  }
}
</style>