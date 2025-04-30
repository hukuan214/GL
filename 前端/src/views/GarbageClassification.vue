<template>
  <div class="garbage-classification">
    <h1 class="title">垃圾分类介绍和指南</h1>
    <!-- 保留原有搜索框 -->
    <div class="search-box">
      <input 
        v-model="searchText" 
        placeholder="输入垃圾名称查询分类" 
        @keyup.enter="searchGarbageItems"
      />
      <button @click="searchGarbageItems">搜索</button>
      <button @click="resetSearch">重置</button>
    </div>
    
    <!-- 保留原有加载和错误提示 -->
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">
      <p>获取数据时出错：{{ error }}</p>
    </div>
    
    <!-- 添加产品容器 -->
    <div v-else class="product-container">
      <!-- 垃圾列表 -->
      <div class="product-list">
        <table class="product-table">
          <thead>
            <tr>
              <!-- <th>ID</th> -->
              <th>垃圾名称</th>
              <th>所属分类</th>
              <th>分类说明</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in garbageItems" 
                :key="item.id" 
                @click="selectItem(item.id)"
                :class="{ 'active': selectedItemId === item.id }">
              <!-- <td>{{ item.id }}</td> -->
              <td>{{ item.name }}</td>
              <td>{{ item.category }}</td>
              <td>{{ item.description }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      
      <!-- 垃圾详情 -->
      <div class="product-detail" v-if="selectedItem">
        <h2>{{ selectedItem.name }}</h2>
        <img :src="'data:image/jpeg;base64,' + selectedItem.image" 
             alt="垃圾图片" 
             v-if="selectedItem.image">
        <div class="category-badge">{{ selectedItem.category }}</div>
        <p>{{ selectedItem.detailedDescription }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      garbageItems: [],
      selectedItem: null,
      selectedItemId: null,
      loading: true,
      error: null,
      searchText: ''
    };
  },
  mounted() {
    this.fetchGarbageItems();
  },
  methods: {
    fetchGarbageItems() {
      axios.get('/api/garbage-items')
        .then(response => {
          this.garbageItems = response.data;
        })
        .catch(error => {
          this.error = error.message;
        })
        .finally(() => {
          this.loading = false;
        });
    },
    searchGarbageItems() {
      if (!this.searchText.trim()) {
        this.fetchGarbageItems();
        return;
      }
      this.loading = true;
      axios.get(`/api/garbage-items/search?search=${this.searchText}`)
        .then(response => {
          this.garbageItems = response.data;
        })
        .catch(error => {
          this.error = error.message;
        })
        .finally(() => {
          this.loading = false;
        });
    },
    resetSearch() {
      this.searchText = '';
      this.fetchGarbageItems();
    },
    selectItem(id) {
      if (this.selectedItemId === id) {
        this.selectedItemId = null;
        this.selectedItem = null;
        return;
      }
      
      this.selectedItemId = id;
      axios.get(`/api/garbage-items/${id}`)
        .then(response => {
          this.selectedItem = response.data;
        })
        .catch(error => {
          console.error(error);
          this.error = "获取垃圾详情失败，请稍后再试";
        });
    },
  }
};
</script>

<style scoped>
.garbage-classification {
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
}

.product-table th,
.product-table td {
  padding: 12px 15px;
  border-bottom: 1px solid #ddd;
  text-align: left;
}

.product-table th {
  background-color: #e8f5e9;
  color: #2e7d32;
}

.product-table tr:hover {
  background-color: #f1f8e9;
}

@media (max-width: 768px) {
  .garbage-classification {
    padding: 15px;
  }
  
  .category-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 480px) {
  .category-grid {
    grid-template-columns: 1fr;
  }
}

/* 新增样式 */
.product-container {
  display: flex;
  gap: 20px;
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

.category-badge {
  display: inline-block;
  padding: 5px 10px;
  background-color: #2e7d32;
  color: white;
  border-radius: 20px;
  margin-bottom: 15px;
}

.product-table tr.active {
  background-color: #f1f8e9 !important;
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
  transition: background-color 0.3s;
}

.search-box button:hover {
  background-color: #1b5e20;
}
</style>