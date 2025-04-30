<template>
  <div>
    <h1>绿色论坛</h1>
    <h2>发表帖子</h2>
    <form @submit.prevent="postMessage">
      <!-- 为输入框添加最大长度限制 -->
      <div class="input-group">
        <input v-model="title" placeholder="帖子标题" maxlength="30" />
        <span class="word-count">{{ title.length }}/30</span>
      </div>
      <div class="input-group">
        <textarea v-model="content" placeholder="帖子内容" maxlength="255"></textarea>
        <span class="word-count">{{ content.length }}/255</span>
      </div>
      <button type="submit">发表</button>
    </form>
    <div v-if="postSuccessMessage" class="success-message">{{ postSuccessMessage }}</div>
    <div v-if="postErrorMessage" class="error-message">{{ postErrorMessage }}</div>
    <h2>帖子列表</h2>
    <div v-if="loading">加载中...</div>
    <div v-else-if="error">
      <p>获取数据时出错：{{ error }}</p>
    </div>
    <ul v-else>
      <li v-for="post in sortedPosts" :key="post.id">
        <h3>{{ post.title }}</h3>
        <p>{{ post.content }}</p>
        <!-- 显示格式化后的帖子发布时间 -->
        <small>发布时间: {{ formatDate(post.post_date) }}</small>
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      title: '',
      content: '',
      posts: [],
      loading: true,
      error: null,
      postSuccessMessage: '',
      postErrorMessage: ''
    };
  },
  computed: {
    sortedPosts() {
      return this.posts.slice().sort((a, b) => {
        return new Date(b.post_date) - new Date(a.post_date);
      });
    }
  },
  mounted() {
    this.fetchPosts();
  },
  methods: {
    fetchPosts() {
      axios.get('/api/forum-posts')
        .then(response => {
          this.posts = response.data;
        })
        .catch(error => {
          this.error = error.message;
        })
        .finally(() => {
          this.loading = false;
        });
    },
    postMessage() {
      // 增加标题和内容不能为空以及长度限制的验证
      if (!this.title || !this.content) {
        this.postErrorMessage = '标题和内容不能为空，请填写完整！';
        this.postSuccessMessage = '';
        return;
      }
      if (this.title.length > 30) {
        this.postErrorMessage = '标题不能超过 30 字，请修改！';
        this.postSuccessMessage = '';
        return;
      }
      if (this.content.length > 255) {
        this.postErrorMessage = '内容不能超过 255 字，请修改！';
        this.postSuccessMessage = '';
        return;
      }
      axios.post('/api/forum-postsqd', {
        title: this.title,
        content: this.content
      })
      .then(response => {
        console.log('帖子发布成功:', response.data);
        this.title = '';
        this.content = '';
        this.fetchPosts();
        // 显示成功提示信息
        this.postSuccessMessage = '帖子发布成功！';
        this.postErrorMessage = '';
        // 一段时间后隐藏提示信息
        setTimeout(() => {
          this.postSuccessMessage = '';
        }, 3000);
      })
      .catch(error => {
        console.error('发布帖子时出错:', error);
        // 显示失败提示信息
        this.postErrorMessage = '帖子发布失败，请稍后重试！';
        this.postSuccessMessage = '';
        // 一段时间后隐藏提示信息
        setTimeout(() => {
          this.postErrorMessage = '';
        }, 3000);
      });
    },
    formatDate(date) {
      if (!date) return '';
      return new Intl.DateTimeFormat('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      }).format(new Date(date));
    }
  }
};
</script>

<style scoped>
/* 修改标题样式 */
h1 {
  color: #2e7d32;
  margin-top: 0; /* 去掉默认的上边距 */
  padding-top: 20px; /* 添加适当的上内边距 */
}

/* 让页面文字居中 */
div {
  text-align: center;
}

h1, h2 {
  color: #2e7d32; /* 修改为绿色主题色 */
}

form {
  margin-bottom: 20px;
  /* 让表单内元素居中显示 */
  display: flex;
  flex-direction: column;
  align-items: center;
}

.input-group {
  position: relative;
  width: 50%;
}

input,
textarea {
  /* 调整宽度 */
  width: 100%; 
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

/* 调整文本框高度 */
textarea {
  height: 150px; 
}

.word-count {
  position: absolute;
  bottom: 15px;
  right: 10px;
  font-size: 12px;
  color: #999;
}

button {
  padding: 10px 20px;
  background-color: #4caf50; /* 修改为绿色主题按钮 */
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #388e3c; /* 修改为深绿色悬停效果 */
}

ul {
  list-style-type: none;
  padding: 0;
  /* 让帖子列表居中 */
  display: flex;
  flex-direction: column;
  align-items: center;
}

li {
  border: 1px solid #ddd;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 4px;
  /* 调整帖子列表项宽度为 100% */
  width: 90%; 
  position: relative; /* 为了让发布时间定位在右下角 */
  /* 添加透明度设置，这里设置为 0.7，你可以根据需要调整 */
  /* opacity: 1;  */
  background-color: rgba(255, 255, 255, 0.7);
}

h3 {
  text-align: center; /* 标题居中 */
  color: #2e7d32; /* 修改为绿色主题色 */
}

p {
  text-indent: 2em; /* 帖子内容首行缩进两字符 */
  text-align: left; /* 内容左对齐 */
  /* 添加以下两行，让文本自动换行 */
  word-wrap: break-word;
  word-break: break-all;
}

small {
  position: absolute; /* 绝对定位 */
  bottom: 10px; /* 距离底部 10px */
  right: 10px; /* 距离右侧 10px */
  color: #2e7d32; /* 修改为绿色主题色 */
}

/* 新增提示信息样式 */
.success-message {
  color: #2e7d32; /* 修改为绿色主题色 */
  background-color: #e8f5e9; /* 添加浅绿色背景 */
  padding: 10px;
  border-radius: 4px;
  margin-bottom: 10px;
}

.error-message {
  color: #d32f2f;
  background-color: #ffebee;
  padding: 10px;
  border-radius: 4px;
  margin-bottom: 10px;
}
</style>
