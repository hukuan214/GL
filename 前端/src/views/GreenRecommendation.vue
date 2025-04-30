<template>
  <div class="green-recommendation">
    <h1 class="title">绿色推荐</h1>
    <!-- 添加搜索框 -->
    <div class="search-box">
      <input 
        v-model="searchText" 
        placeholder="输入产品名称查询" 
        @keyup.enter="searchGreenProducts"
      />
      <button @click="searchGreenProducts">搜索</button>
      <button @click="resetSearch">重置</button>
    </div>
    
    <!-- 保留原有加载和错误提示 -->
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">
      <p>获取数据时出错：{{ error }}</p>
    </div>
    
    <div v-else class="product-container">
      <!-- 产品列表 -->
      <div class="product-list">
        <table class="product-table">
          <thead>
            <tr>
              <th>名称</th>
              <th>描述</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="product in greenProducts" 
                :key="product.id" 
                @click="selectProduct(product.id)"
                :class="{ 'active': selectedProductId === product.id }">
                <td>{{ product.name }}</td>
                <td>{{ product.description }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      
      <!-- 产品详情 -->
      <div class="product-detail" v-if="selectedProduct">
        <h2>{{ selectedProduct.name }}</h2>
        <img :src="'data:image/jpeg;base64,' + selectedProduct.image" 
             alt="产品图片" 
             v-if="selectedProduct.image">
        <p>{{ selectedProduct.detailedDescription }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      greenProducts: [],
      originalProducts: [], // 新增：保存原始产品列表
      searchText: '', // 新增：搜索关键词
      selectedProduct: null,
      selectedProductId: null,  // 新增：记录当前选中的产品ID
      loading: true,
      error: null
    };
  },
  mounted() {
    this.fetchGreenProducts();
  },
  methods: {
    // 新增搜索方法
    searchGreenProducts() {
      if (!this.searchText.trim()) {
        this.greenProducts = [...this.originalProducts];
        return;
      }
      this.loading = true;
      axios.get(`/api/green-products/search?search=${this.searchText}`)
        .then(response => {
          this.greenProducts = response.data;
        })
        .catch(error => {
          this.error = error.message;
        })
        .finally(() => {
          this.loading = false;
        });
    },
    
    // 新增重置搜索方法
    resetSearch() {
      this.searchText = '';
      this.greenProducts = [...this.originalProducts];
    },
    
    fetchGreenProducts() {
      axios.get('/api/green-products')
        .then(response => {
          this.greenProducts = response.data;
          this.originalProducts = [...response.data]; // 保存原始数据
        })
        .catch(error => {
          this.error = error.message;
        })
        .finally(() => {
          this.loading = false;
        });
    },
    selectProduct(id) {
      // 如果点击的是已选中的产品，则取消选中
      if (this.selectedProductId === id) {
          this.selectedProductId = null;
          this.selectedProduct = null;
          return;
      }
      
      this.selectedProductId = id;
      axios.get(`/api/green-products/${id}`)
          .then(response => {
              this.selectedProduct = response.data;
          })
          .catch(error => {
              console.error(error);
              this.error = "获取产品详情失败，请稍后再试";
          });
    }
  }
};
</script>

<style scoped>
.green-recommendation {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.title {
  color: #2e7d32;
  text-align: center;
  margin-bottom: 20px;
}

.loading {
  color: #666;
  text-align: center;
  font-style: italic;
}

.error {
  color: #d32f2f;
  text-align: center;
  font-weight: bold;
}

.product-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
  table-layout: fixed; /* 新增：固定表格布局 */
}

.product-table th,
.product-table td {
  padding: 12px 15px;
  border-bottom: 1px solid #ddd;
  text-align: left;
  word-wrap: break-word; /* 新增：允许单词换行 */
}

.product-table th {
  background-color: #e8f5e9;
  color: #2e7d32;
}

.product-table tr.active {
    background-color: #f1f8e9 !important;
}

.product-table tr:hover {
    background-color: #f1f8e9;
    cursor: pointer;
}
.product-container {
  display: flex;
  gap: 20px; /* 修改为20px间距，与垃圾分类页面一致 */
}

.product-list {
  flex: 1;
}

.product-detail {
  flex: 1;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0,0,0,0.1);
}

.product-detail img {
  max-width: 100%;
  height: auto;
  margin-bottom: 20px;
}

/* 新增搜索框样式 */
.search-box {
  margin: 20px 0;
  display: flex;
  gap: 10px;
}

.search-box input {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.search-box button {
  padding: 8px 16px;
  background-color: #2e7d32;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.search-box button:hover {
  background-color: #1b5e20;
}
</style>


