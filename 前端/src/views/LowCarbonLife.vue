<template>
  <div class="low-carbon-life">
    <h1>低碳生活</h1>
    <div class="content-wrapper">
      <!-- 环保活动部分 -->
      <div class="event-section">
        <h2>环保活动</h2>
        <div v-if="loading">加载中...</div>
        <div v-else-if="error">
          <p>获取数据时出错：{{ error }}</p>
        </div>
        <table v-else class="event-table">
          <thead>
            <tr>
              <th>活动名称</th>
              <th>活动地点</th>
              <th>活动开始时间</th>
              <th>活动倒计时</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="event in environmentalEvents" :key="event.id">
              <td>{{ event.event_name }}</td>
              <td>{{ event.location }}</td>
              <td>{{ formatDate(event.start_date) }}</td>
              <td>{{ calculateCountdown(event.start_date) }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <!-- 发布闲置物品部分 -->
      <div class="item-section">
        <h2>发布闲置物品</h2>
        <form @submit.prevent="publishItem">
  <input v-model="itemName" placeholder="物品名称" />
  <input v-model="phoneNumber" placeholder="联系电话" maxlength="11" />
  <input v-model="itemDescription" placeholder="物品描述" />
  <input 
    type="file" 
    @change="handleFileUpload" 
    accept="image/*" 
    ref="fileInput"
    id="fileInput"
    style="display: none"
  />
  <label for="fileInput" class="custom-file-upload">
    <span v-if="!selectedFile">点击上传图片描述</span>
    <span v-else>已选择文件: {{ selectedFile.name }}</span>
  </label>
  <button type="submit">发布</button>
</form>
        <!-- 查看发布的闲置物品 -->
        <h2>查看发布的闲置物品</h2>
        <div v-if="loadingSecondHandItems">加载中...</div>
        <div v-else-if="errorSecondHandItems">
          <p>获取闲置物品数据时出错：{{ errorSecondHandItems }}</p>
        </div>
        <!-- 修改表格和详情展示区域的布局 -->
        <div class="item-display-wrapper">
          <div class="item-list">
            <table class="second-hand-table">
              <thead>
                <tr>
                  <th>物品名称</th>
                  <th>发布日期</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in secondHandItems" 
                    :key="item.id" 
                    @click="selectItem(item.id)"
                    :class="{ 'active': selectedItemId === item.id }">
                  <td>{{ item.item_name }}</td>
                  <td>{{ formatDate(item.publish_date) }}</td>
                </tr>
              </tbody>
            </table>
          </div>
          
          <div class="item-detail" v-if="selectedItem">
            <h3>{{ selectedItem.item_name }}</h3>
            <p><strong>物品描述:</strong> {{ selectedItem.item_description }}</p>
            <p><strong>联系电话:</strong> {{ selectedItem.phone_number }}</p>
            <img v-if="selectedItem.image" 
                 :src="getImageUrl(selectedItem.image)" 
                 style="max-width: 200px; max-height: 200px;" />
          </div>
        </div>
        
       
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  // 在data中添加phoneNumber字段
  data() {
    return {
      environmentalEvents: [],
      itemName: '',
      itemDescription: '',
      loading: true,
      error: null,
      secondHandItems: [],
      loadingSecondHandItems: true,
      errorSecondHandItems: null,
      phoneNumber: '',
      selectedFile: null,
      selectedItem: null,
    selectedItemId: null
    };
  },
  mounted() {
    this.fetchEnvironmentalEvents();
    this.fetchSecondHandItems();
  },
  methods: {
    formatDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return date.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      });
    },
    fetchEnvironmentalEvents() {
      axios.get('/api/environmental-events')  // 保持原样
        .then(response => {
          this.environmentalEvents = response.data;
        })
        .catch(error => {
          this.error = error.message;
        })
        .finally(() => {
          this.loading = false;
        });
    },
    selectItem(id) {
    if (this.selectedItemId === id) {
      this.selectedItemId = null;
      this.selectedItem = null;
      return;
    }
    
    this.selectedItemId = id;
    // 直接从已加载的数据中查找
    const item = this.secondHandItems.find(i => i.id === id);
    if (item) {
        this.selectedItem = item;
    } else {
        console.error('未找到该闲置物品');
    }
    },
    calculateCountdown(endDate) {
      const now = new Date();
      const diff = new Date(endDate) - now;
      if (diff <= 0) return '已开始';
      
      const days = Math.floor(diff / (1000 * 60 * 60 * 24));
      const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
      const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
      
      return `${days}天${hours}小时${minutes}分钟`;
    },
    // 修改发布方法
    async publishItem() {
      // 非空验证
      if(!this.itemName || !this.itemDescription) {
        alert('请填写完整信息');
        return;
      }
      
      // 手机号验证
      if(!this.phoneNumber || !/^1[3-9]\d{9}$/.test(this.phoneNumber)) {
        alert('请输入正确的手机号');
        return;
      }
      
      try {
          // 1. 先创建物品
          const response = await axios.post('/api/second-hand-items', {
              item_name: this.itemName,
              item_description: this.itemDescription,
              phone_number: this.phoneNumber
          });
          
          console.log('物品创建成功:', response.data);
          
          // 2. 如果有图片则上传
          if (this.selectedFile) {
              const formData = new FormData();
              formData.append('file', this.selectedFile);
              
              const uploadResponse = await axios.post(
                  `/api/second-hand-items/${response.data.id}/image`, 
                  formData, 
                  {
                      headers: {
                          'Content-Type': 'multipart/form-data'
                      }
                  }
              );
              
              console.log('图片上传成功:', uploadResponse.data);
              // 立即更新图片显示
              if (uploadResponse.data.image) {
                  const itemIndex = this.secondHandItems.findIndex(i => i.id === response.data.id);
                  if (itemIndex !== -1) {
                      this.$set(this.secondHandItems[itemIndex], 'image', uploadResponse.data.image);
                  }
              }
          }
          
          // 3. 重置表单并刷新列表
          this.itemName = '';
          this.itemDescription = '';
          this.phoneNumber = '';
          this.selectedFile = null;
          this.$refs.fileInput.value = '';
          
          this.fetchSecondHandItems();
      } catch (error) {
          console.error('发布闲置物品时出错:', error);
          const errorMsg = error.response?.data?.message || 
                          error.response?.data || 
                          '发布失败，请检查网络或联系管理员';
          alert(errorMsg);
      }
    },
    fetchSecondHandItems() {
      axios.get('/api/second-hand-items')
        .then(response => {
          this.secondHandItems = response.data;
        })
        .catch(error => {
          this.errorSecondHandItems = error.message;
        })
        .finally(() => {
          this.loadingSecondHandItems = false;
        });
    },
    // 在methods中添加
    // formatPhoneNumber(phone) {
    //   return phone ? phone.replace(/(\d{3})(\d{4})(\d{4})/, '$1****$3') : '';
    // },
    // 添加这个方法处理文件上传
    handleFileUpload(event) {
      this.selectedFile = event.target.files[0];
    },
    getImageUrl(imageData) {
        if (!imageData) return '';
        // 如果已经是base64字符串直接返回
        if (typeof imageData === 'string') {
            return `data:image/jpeg;base64,${imageData}`;
        }
        // 如果是ArrayBuffer转换为base64
        return `data:image/jpeg;base64,${this.arrayBufferToBase64(imageData)}`;
    },
    
    arrayBufferToBase64(buffer) {
        let binary = '';
        const bytes = new Uint8Array(buffer);
        const len = bytes.byteLength;
        for (let i = 0; i < len; i++) {
            binary += String.fromCharCode(bytes[i]);
        }
        return window.btoa(binary);
    }
  }
};
</script>

<style scoped>
.low-carbon-life {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.low-carbon-life h1 {
  text-align: center;
  color: #2e7d32; /* 统一绿色主题 */
  margin-bottom: 30px;
}

.content-wrapper {
  display: flex;
  gap: 20px;
}
.item-section form input[data-v-55066b9a] {
    width: 95%;
    padding: 10px;
    margin-bottom: 10px;
    border: 1px solid #ddd;
    border-radius: 4px;}
.event-section,
.item-section {
  flex: 1;
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); /* 统一阴影效果 */
}

h2 {
  color: #388e3c; /* 次级标题颜色 */
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #e0e0e0;
}

.event-table,
.second-hand-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 15px;
}

.event-table th,
.event-table td,
.second-hand-table th,
.second-hand-table td {
  padding: 12px 15px;
  border-bottom: 1px solid #e0e0e0;
  text-align: left;
}

.event-table th,
.second-hand-table th {
  background-color: #e8f5e9; /* 表头浅绿色背景 */
  color: #2e7d32;
}

.event-table tr:hover,
.second-hand-table tr:hover {
  background-color: #f1f8e9; /* 行悬停效果 */
}

.item-section form input {
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.item-section form input:required {
  border-left: 3px solid #4caf50;
}

.item-section form input::placeholder {
  color: #999;
  font-size: 0.9em;
}

.item-section form button {
  width: 100%;
  padding: 10px;
  background-color: #4caf50; /* 统一按钮颜色 */
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.item-section form button:hover {
  background-color: #388e3c; /* 按钮悬停颜色 */
}

.loading, .error {
  text-align: center;
  padding: 20px;
  background: #f5f5f5;
  border-radius: 4px;
  margin: 20px 0;
}

@media (max-width: 768px) {
  .low-carbon-life {
    padding: 15px;
  }
  
  .content-container {
    flex-direction: column;
  }
  
  .event-section, 
  .item-section {
    width: 100%;
  }
}
.second-hand-table tr:hover {
  background-color: #f1f8e9;
  cursor: pointer;
}

.second-hand-table tr.active {
  background-color: #e8f5e9;
}

.item-detail {
  margin-top: 20px;
  padding: 15px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0,0,0,0.1);
}        .item-display-wrapper {
          display: flex;
          gap: 20px;
        }
        
        .item-list {
          flex: 1;
        }
        
        .item-detail {
          flex: 1;
          padding: 15px;
          background: #fff;
          border-radius: 8px;
          box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        
        .second-hand-table {
          width: 100%;
          border-collapse: collapse;
        }
        
        .second-hand-table th, 
        .second-hand-table td {
          padding: 12px 15px;
          border-bottom: 1px solid #e0e0e0;
          text-align: left;
        }
        
        .second-hand-table th {
          background-color: #e8f5e9;
          color: #2e7d32;
        }
        
        .second-hand-table tr:hover {
          background-color: #f1f8e9;
          cursor: pointer;
        }
        
        .second-hand-table tr.active {
          background-color: #e8f5e9;
        }
</style>

<style scoped>
/* 美化文件上传按钮 */
.custom-file-upload {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 95%;
  padding: 12px;
  margin-bottom: 10px;
  background-color: #f8f9fa;
  color: #2e7d32;
  text-align: center;
  border: 2px dashed #4caf50;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.custom-file-upload:hover {
  background-color: #e8f5e9;
  border-color: #388e3c;
}

.custom-file-upload::before {
  content: "📤";
  margin-right: 8px;
  font-size: 1.2em;
}

.custom-file-upload span {
  font-weight: 500;
}
</style>