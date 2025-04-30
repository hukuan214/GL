<template>
  <div class="admin-container">
    <div class="admin-header">
      <h1>管理员页面</h1>
      <button class="logout-btn" @click="logout">退出登录</button>
    </div>
    <!-- 选项卡导航 -->
    <div class="admin-tabs">
      <button 
        v-for="tab in tabs" 
        :key="tab.id"
        @click="currentTab = tab.id"
        :class="{ active: currentTab === tab.id }"
      >
        {{ tab.name }}
      </button>
    </div>

    <!-- 用户管理 -->
    <div v-if="currentTab === 'users'" class="admin-section">
      <h2>用户管理</h2>
      <div class="search-bar">
        <input v-model="userSearch" placeholder="搜索用户名" />
        <button @click="searchUsers">搜索</button>
        <!-- <button @click="showAddUser = true">添加用户</button> -->
        <button data-v-54c38a05="" @click="showAddUser = !showAddUser">添加用户</button>
      </div>
      
      <!-- 添加用户表单 -->
      <div v-if="showAddUser" class="form-container">
        <input v-model="newUser.username" placeholder="用户名" />
        <input v-model="newUser.password" type="password" placeholder="密码" />
        <!-- 修改添加用户表单中的管理员复选框 -->
       <template>
  <div class="checkbox-row">
    是否为管理员
    <input type="checkbox" v-model="newUser.isAdmin" :true-value="1" :false-value="0" />
  </div>
</template>
       
        <button @click="addUser">提交</button>
        <button @click="showAddUser = false">取消</button>
      </div>

      <!-- 用户列表 -->
      <table class="admin-table">
        <thead>
          <tr>
            <th>ID4</th>
            <th>用户名</th>
            <th>密码</th>
            <th>角色</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.id }}</td>
            <td>
              <span v-if="!user.editing">{{ user.username }}</span>
              <input v-else v-model="user.editData.username" />
            </td>
            <td>
              <span v-if="!user.editing">{{ user.password }}</span>
              <input v-else v-model="user.editData.password" type="password" />
            </td>
            <!-- 修改角色显示部分 -->
            <td>
              <span v-if="!user.editing">{{ user.isAdmin === 1 ? '管理员' : '普通用户' }}</span>
              <label v-else>
                <input type="checkbox" v-model="user.editData.isAdmin" :true-value="1" :false-value="0" />
                {{ user.editData.isAdmin === 1 ? '管理员' : '普通用户' }}
              </label>
            </td>
            <td>
              <button v-if="!user.editing" @click="startEdit(user)">编辑</button>
              <template v-else>
                <button @click="saveEdit(user)">保存</button>
                <button @click="cancelEdit(user)">取消</button>
              </template>
              <button @click="deleteUser(user.id)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 绿色产品管理部分 -->
    <div v-if="currentTab === 'products'" class="admin-section">
      <h2>绿色产品管理</h2>
      <div class="search-bar">
        <input v-model="productSearch" placeholder="搜索产品名称" />
        <button @click="searchProducts">搜索</button>
        <!-- <button @click="showAddProduct = true">添加产品</button> -->
        <button @click="showAddProduct = !showAddProduct">添加产品</button>
      </div>
      
      <!-- 添加产品表单 -->
      <div v-if="showAddProduct" class="form-container">
  <input v-model="newProduct.name" placeholder="产品名称" />
  <input v-model="newProduct.description" placeholder="产品描述" />
  <textarea v-model="newProduct.detailedDescription" placeholder="详细描述"></textarea>
  <div style="display: flex; flex-direction: column; gap: 8px;">
  <span style="color: #666; margin-bottom: 5px;">点击选择文件上传产品图片</span>
  <input type="file" @change="handleImageUpload" accept="image/*" />
</div>
  <!-- <input type="file" @change="handleImageUpload" accept="image/*" /> -->
  <button @click="addProduct">提交</button>
  <button @click="showAddProduct = false">取消</button>
</div>
    
      <!-- 产品列表 -->
      <table class="admin-table">
  <thead>
    <tr>
      <th>ID</th>
      <th>产品名称</th>
      <th>产品描述</th>
      <th>详细描述</th>
      <th>图片</th>
      <th>操作</th>
    </tr>
  </thead>
  <tbody>
    <tr v-for="product in products" :key="product.id">
      <td>{{ product.id }}</td>
      <td>
        <span v-if="!product.editing">{{ product.name }}</span>
        <input v-else v-model="product.editData.name" />
      </td>
      <td>
        <span v-if="!product.editing">{{ product.description }}</span>
        <input v-else v-model="product.editData.description" />
      </td>
      <td>
        <span v-if="!product.editing">{{ product.detailedDescription }}</span>
        <textarea v-else v-model="product.editData.detailedDescription"></textarea>
      </td>
      <td>
        <img v-if="product.image && !product.editing" 
             :src="'data:image/jpeg;base64,' + product.image" 
             style="max-width: 100px; max-height: 100px;" />
        <input v-else type="file" @change="handleEditImageUpload($event, product)" />
      </td>
      <td>
        <!-- 操作按钮保持不变 -->
        <div class="action-buttons">
    <button v-if="!product.editing" @click="startEditProduct(product)" class="btn-edit">编辑</button>
    <button v-if="!product.editing" @click="deleteProduct(product.id)" class="btn-delete">删除</button>
    <template v-else>
      <button @click="saveEditProduct(product)" class="btn-save">保存</button>
      <button @click="cancelEditProduct(product)" class="btn-cancel">取消</button>
    </template>
  </div>
      </td>
    </tr>
  </tbody>
</table>
    </div>

    <!-- 垃圾分类管理部分 -->
    <div v-if="currentTab === 'garbage'" class="admin-section">
      <h2>垃圾分类管理</h2>
      <div class="search-bar">
        <input v-model="garbageSearch" placeholder="搜索垃圾名称" />
        <button @click="searchGarbageItems">搜索</button>
        <!-- <button @click="showAddGarbage = true">添加垃圾项</button> -->
        <button @click="showAddGarbage = !showAddGarbage">添加垃圾项</button>
      </div>
      
      <!-- 添加垃圾项表单 -->
      <div v-if="showAddGarbage" class="form-container">
  <input v-model="newGarbageItem.name" placeholder="垃圾名称" />
  <select v-model="newGarbageItem.category">
    <option value="可回收物">可回收物</option>
    <option value="有害垃圾">有害垃圾</option>
    <option value="厨余垃圾">厨余垃圾</option>
    <option value="其他垃圾">其他垃圾</option>
  </select>
  <!-- 添加分类说明输入框 -->
  <input v-model="newGarbageItem.description" placeholder="分类说明" />
  <textarea v-model="newGarbageItem.detailedDescription" placeholder="详细描述"></textarea>
  <!-- <div style="display: flex; flex-direction: column; gap: 8px;">
  <span style="color: #666; margin-bottom: 5px;">点击选择文件上传垃圾图片</span>
  <input type="file" @change="handleImageUpload" accept="image/*" />
</div> -->
  <input type="file" @change="handleGarbageImageUpload" accept="image/*" />
  <button @click="addGarbageItem">提交</button>
  <button @click="showAddGarbage = false">取消</button>
</div>
    
      <!-- 垃圾项列表 -->
      <table class="admin-table">
  <thead>
    <tr>
      <th>ID</th>
      <th>垃圾名称</th>
      <th>所属分类</th>
      <th>分类说明</th>
      <th>详细描述</th>
      <th>图片</th>
      <th>操作</th>
    </tr>
  </thead>
  <tbody>
    <tr v-for="item in garbageItems" :key="item.id">
      <td>{{ item.id }}</td>
      <td>
        <span v-if="!item.editing">{{ item.name }}</span>
        <input v-else v-model="item.editData.name" />
      </td>
      <td>
        <span v-if="!item.editing">{{ item.category }}</span>
        <select v-else v-model="item.editData.category">
          <option value="可回收物">可回收物</option>
          <option value="有害垃圾">有害垃圾</option>
          <option value="厨余垃圾">厨余垃圾</option>
          <option value="其他垃圾">其他垃圾</option>
        </select>
      </td>
      <!-- 添加分类说明编辑框 -->
      <td>
        <span v-if="!item.editing">{{ item.description }}</span>
        <input v-else v-model="item.editData.description" />
      </td>
      <td>
        <span v-if="!item.editing">{{ item.detailedDescription }}</span>
        <textarea v-else v-model="item.editData.detailedDescription"></textarea>
      </td>
      <td>
        <img v-if="item.image && !item.editing" 
             :src="'data:image/jpeg;base64,' + item.image" 
             style="max-width: 100px; max-height: 100px;" />
        <input v-else type="file" @change="handleEditGarbageImageUpload($event, item)" />
      </td>
      <td>
        <div class="action-buttons">
          <button v-if="!item.editing" @click="startEditGarbageItem(item)" class="btn-edit">编辑</button>
          <button v-if="!item.editing" @click="deleteGarbageItem(item.id)" class="btn-delete">删除</button>
          <template v-else>
            <button @click="saveEditGarbageItem(item)" class="btn-save">保存</button>
            <button @click="cancelEditGarbageItem(item)" class="btn-cancel">取消</button>
          </template>
        </div>
      </td>
    </tr>
  </tbody>
</table>
    </div>

    <!-- 环保活动管理 -->
    <!-- 在template部分添加以下代码 -->
    <div v-if="currentTab === 'events'" class="admin-section">
    <h2>环保活动管理</h2>
    
    <div class="search-bar">
        <input v-model="eventSearch" placeholder="搜索活动..." />
        <button @click="searchEvents">搜索</button>
        <button @click="showAddEvent =!showAddEvent">添加新活动</button>
    </div>
    
    <div v-if="showAddEvent" class="add-form">
        <h3>添加环保活动</h3>
        <!-- 修改为使用 flex 布局，让输入框垂直排列 -->
        <div style="display: flex; flex-direction: column; gap: 10px;">
          <input v-model="newEvent.event_name" placeholder="活动名称" />
          <input v-model="newEvent.location" placeholder="活动地点" />
          <textarea v-model="newEvent.description" placeholder="活动描述"></textarea>
          <input v-model="newEvent.start_date" type="datetime-local" placeholder="开始日期" />
          <input v-model="newEvent.end_date" type="datetime-local" placeholder="结束日期" />
          <div style="display: flex; gap: 10px;">
            <button @click="addEvent">保存</button>
            <button @click="showAddEvent = false">取消</button>
          </div>
        </div>
    </div>
    
 
    
    <!-- 环保活动管理表格 -->
    <table class="admin-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>活动名称</th>
                <th>活动地点</th>
                <th>活动描述</th>
                <th>开始日期</th>
                <th>结束日期</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="event in filteredEvents" :key="event.id">
                <td>{{ event.id }}</td>
                <td>
                <span v-if="!event.editing">{{ event.event_name }}</span>
                <input v-else v-model="event.tempData.event_name" />
            </td>
            <td>
                <span v-if="!event.editing">{{ event.location }}</span>
                <input v-else v-model="event.tempData.location" />
            </td>
            <td>
                <span v-if="!event.editing">{{ event.description }}</span>
                <textarea v-else v-model="event.tempData.description"></textarea>
            </td>
            <td>
                <span v-if="!event.editing">{{ formatDate(event.start_date) }}</span>
                <input v-else v-model="event.tempData.start_date" type="datetime-local" />
            </td>
            <td>
                <span v-if="!event.editing">{{ formatDate(event.end_date) }}</span>
                <input v-else v-model="event.tempData.end_date" type="datetime-local" />
            </td>
            <td>
                <template v-if="!event.editing">
                    <button @click="editEvent(event)">编辑</button>
                    <button @click="deleteEvent(event.id)">删除</button>
                </template>
                <template v-else>
                    <button @click="saveEditEvent(event)">保存</button>
                    <button @click="cancelEditEvent(event)">取消</button>
                </template>
            </td>
            </tr>
        </tbody>
    </table>
</div>

    <!-- 闲置物品管理 -->
    <!-- 闲置物品管理 -->
<div v-if="currentTab === 'items'" class="admin-section">
    <h2>闲置物品管理</h2>
    
    <div class="search-bar">
        <input v-model="itemSearch" placeholder="搜索物品名称" />
        <button @click="searchItems">搜索</button>
        <button @click="showAddItem = !showAddItem">添加物品</button>
    </div>
    
    <!-- 添加物品表单 -->
    <!-- 添加物品表单 -->
    <div v-if="showAddItem" class="form-container">
    <input v-model="newItem.item_name" placeholder="物品名称" />
    <textarea v-model="newItem.item_description" placeholder="物品描述"></textarea>
    <div style="display: flex; flex-direction: column; gap: 8px;">
  <span style="color: #666; margin-bottom: 5px;">点击选择文件上传物品图片</span>
  <input type="file" @change="handleItemImageUpload" accept="image/*" />
  <!-- <input type="file" @change="handleImageUpload" accept="image/*" /> -->
</div>
    <input v-model="newItem.phone_number" placeholder="联系电话" />
    <!-- 编辑物品时的图片上传 -->
<!-- <input type="file" @change="handleEditItemImageUpload($event, item)" accept="image/*" /> -->
    <button @click="addItem">提交</button>
    <button @click="showAddItem = false">取消</button>
</div>
    <!-- 物品列表 -->
    <table class="admin-table">
    <thead>
      <tr>
    <th>ID</th>
    <th>物品名称</th>
    <th>物品描述</th>
    <th>联系电话</th>
    <th>图片</th>
    <th>发布日期</th>
    <th>操作</th>
</tr>
    </thead>
    <tbody>
        <tr v-for="item in filteredItems" :key="item.id">
          <td>{{ item.id }}</td>
    <td>
        <span v-if="!item.editing">{{ item.item_name }}</span>
        <input v-else v-model="item.editData.item_name" />
    </td>
    <td>
        <span v-if="!item.editing">{{ item.item_description }}</span>
        <textarea v-else v-model="item.editData.item_description"></textarea>
    </td>
    <td>
        <span v-if="!item.editing">{{ item.phone_number }}</span>
        <input v-else v-model="item.editData.phone_number" />
    </td>
    <td>
        <img v-if="item.image && !item.editing" 
             :src="'data:image/jpeg;base64,' + item.image" 
             style="max-width: 100px; max-height: 100px;" />
        <input v-else type="file" @change="handleEditItemImageUpload($event, item)" />
    </td>
    <td>{{ formatDate(item.publish_date) }}</td>
    <td>
        <template v-if="!item.editing">
            <button @click="startEditItem(item)">编辑</button>
            <button @click="deleteItem(item.id)">删除</button>
        </template>
        <template v-else>
            <button @click="saveEditItem(item)">保存</button>
            <button @click="cancelEditItem(item)">取消</button>
        </template>
    </td>
        </tr>
    </tbody>
</table>
</div>

    <!-- 订单管理 -->
    <div v-if="currentTab === 'orders'" class="admin-section">
  <h2>订单管理</h2>
  <div class="search-bar">
    <input v-model="orderSearch" placeholder="搜索订单商品名称" />
    <button @click="searchOrders">搜索</button>
  </div>
  
  <table class="admin-table">
    <thead>
      <tr>
        <th>ID</th>
        <th>商品名称</th>
        <th>数量</th>
        <th>手机号</th>  <!-- 新增手机号列 -->
        <th>地址</th>
        <th>日期</th>
        <th>状态</th>
        <th>操作</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="order in filteredOrders" :key="order.id">
        <td>{{ order.id }}</td>
        <td>{{ order.product_name }}</td>
        <td>{{ order.quantity }}</td>
        <td>{{ order.phone_number }}</td>  <!-- 显示手机号 -->
        <td>{{ order.address }}</td>
        <td>{{ formatDate(order.order_date) }}</td>
        <td>{{ order.status }}</td>
        <td>
          <button v-if="order.status === '待审核'" @click="approveOrder(order.id)">通过</button>
          <button v-if="order.status === '待审核'" @click="rejectOrder(order.id)">拒绝</button>
        </td>
      </tr>
    </tbody>
  </table>
  
  <!-- 拒绝原因对话框 -->
  <div v-if="showRejectDialog" class="dialog-overlay">
    <div class="dialog">
      <h3>输入拒绝原因</h3>
      <textarea v-model="rejectReason"></textarea>
      <button @click="confirmReject">确认</button>
      <button @click="showRejectDialog = false">取消</button>
    </div>
  </div>
</div>
<!-- 减碳计算器 -->
<div v-if="currentTab === 'carbon'" class="admin-section">
  <h2>减碳计算器管理</h2>
  
  <button @click="showAddTravelType = !showAddTravelType">添加出行方式</button>
  
  <div v-if="showAddTravelType" class="form-container">
    <input v-model="newTravelType.travel_type" placeholder="出行方式名称" />
    <input v-model.number="newTravelType.carbon_reduction_per_unit" type="number" step="0.01" placeholder="每公里减碳量(kg)" />
    <button @click="addTravelType">提交</button>
    <button @click="showAddTravelType = false">取消</button>
  </div>

  <table class="admin-table">
    <thead>
      <tr>
        <th>ID</th>
        <th>出行方式</th>
        <th>每公里减碳量(kg)</th>
        <th>操作</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="type in travelTypes" :key="type.id">
        <td>{{ type.id }}</td>
  <td>
    <span v-if="!type.editing">{{ type.travel_type }}</span>
    <input v-else v-model="type.editData.travel_type" />
  </td>
  <td>
    <span v-if="!type.editing">{{ type.carbon_reduction_per_unit }}</span>
    <input v-else 
           v-model.number="type.editData.carbon_reduction_per_unit" 
           type="number" 
           step="0.01" />
  </td>
        <td>
  <template v-if="!type.editing">
    <button @click="editTravelType(type)">编辑</button>
    <button @click="deleteTravelType(type.id)">删除</button>
  </template>
  <template v-else>
    <button @click="saveTravelType(type)">保存</button>
    <button @click="cancelEditTravelType(type)">取消</button>
  </template>
</td>
      </tr>
    </tbody>
  </table>
</div>

    <!-- 论坛管理 -->
    <!-- 论坛管理 -->
<div v-if="currentTab === 'forum'" class="admin-section">
  <h2>论坛管理</h2>
  
  <div class="search-bar">
    <input v-model="forumSearch" placeholder="搜索帖子标题" />
    <button @click="searchForumPosts">搜索</button>
    <button @click="showAddForumPost = !showAddForumPost">添加新帖子</button>
  </div>
  
  <!-- 添加帖子表单 -->
  <div v-if="showAddForumPost" class="form-container">
    <input v-model="newForumPost.title" placeholder="帖子标题" />
    <textarea v-model="newForumPost.content" placeholder="帖子内容"></textarea>
    <div class="button-container">
      <button data-v-54c38a05="" @click="addForumPost">提交</button>
      <button data-v-54c38a05="" @click="showAddForumPost = false">取消</button>
    </div>
  </div>
  
  <!-- 编辑帖子表单 -->
  <div v-if="editingForumPost" class="form-container">
    <input v-model="editingForumPost.title" placeholder="帖子标题" />
    <textarea v-model="editingForumPost.content" placeholder="帖子内容"></textarea>
    <button @click="saveForumPost">保存</button>
    <button @click="editingForumPost = null">取消</button>
  </div>
  
  <!-- 帖子列表 -->
  <!-- 帖子列表 -->
<table class="admin-table forum-management-table">
  <thead>
    <tr>
      <th>ID</th>
      <th>标题</th>
      <th>内容</th>
      <th>发布时间</th>
      <th>操作</th>
    </tr>
  </thead>
  <tbody>
    <tr v-for="post in forumPosts" :key="post.id">
      <td>{{ post.id }}</td>
      <td>
        <template v-if="!post.editing">{{ post.title }}</template>
        <input v-else v-model="post.editData.title" />
      </td>
      <td>
        <template v-if="!post.editing">{{ post.content }}</template>
        <textarea v-else v-model="post.editData.content"></textarea>
      </td>
      <td>{{ formatDate(post.post_date) }}</td>
      <td>
        <template v-if="!post.editing">
          <button @click="startEditForumPost(post)">编辑</button>
          <button @click="deleteForumPost(post.id)">删除</button>
        </template>
        <template v-else>
          <button @click="saveEditForumPost(post)">保存</button>
          <button @click="cancelEditForumPost(post)">取消</button>
        </template>
      </td>
    </tr>
  </tbody>
</table>
</div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Admin',
  data() {
    return {
      currentTab: 'users',
      tabs: [
        { id: 'users', name: '用户管理' },
        { id: 'products', name: '绿色产品' },
        { id: 'garbage', name: '垃圾分类' },
        { id: 'events', name: '环保活动' },
        { id: 'items', name: '闲置物品' },
        { id: 'orders', name: '订单管理' },
        { id: 'carbon', name: '减碳计算器' },
        { id: 'forum', name: '论坛管理' }
      ],
      users: [], // 用户列表数据
      userSearch: '', // 添加搜索关键词
      showAddUser: false, // 控制添加用户表单显示
      newUser: {
        username: '',
        password: '',
        isAdmin: 0 // 默认设为0（普通用户）
      },
      // 添加以下绿色产品管理相关的响应式属性
      
        originalProducts: [], // 原始产品列表
        productSearch: '',
      products: [], // 产品列表数据
      productSearch: '', // 产品搜索关键词
      showAddProduct: false, // 控制添加产品表单显示
      newProduct: { // 新产品表单数据
        name: '',
        description: '',
        detailedDescription: '',
        image: null
      },
      // 添加以下垃圾分类管理相关的响应式属性
        originalGarbageItems: [], // 原始垃圾项列表
        garbageSearch: '', // 垃圾搜索关键词
      garbageItems: [], // 垃圾项列表数据
    garbageSearch: '', // 垃圾搜索关键词
    showAddGarbage: false, // 控制添加垃圾项表单显示
    newGarbageItem: { // 新垃圾项表单数据
      name: '',
      category: '可回收物',
      description: '',
      image: null
    },
    events: [], // 环保活动列表数据
        eventSearch: '', // 活动搜索关键词
        showAddEvent: false, // 控制添加活动表单显示
        newEvent: { // 新活动表单数据
            event_name: '',
            start_date: '',
            end_date: '',
            
        },
        events: [], // 环保活动列表数据
        filteredEvents: [], // 从computed移到data中
        
        newEvent: { // 新活动表单数据
            event_name: '',
            start_date: '',
            end_date: '',
            location: '', // 新增活动地点
            description: '' // 新增活动描述
        },
        items: [], // 闲置物品列表
        filteredItems: [], // 添加这行初始化filteredItems
        itemSearch: '', // 物品搜索关键词
        showAddItem: false, // 控制添加物品表单显示
        newItem: { // 新物品表单数据
            item_name: '',
            item_description: '',
            phone_number: '',
            image: null
        },
        // 订单管理相关属性
        orders: [], // 订单列表
        orderSearch: '', // 订单搜索关键词
        filteredOrders: [], // 添加这行初始化filteredOrders
        showAddOrder: false, // 控制添加订单表单显示
        selectedOrderId: null,
        rejectReason: '', // 添加这行
        showRejectDialog: false, // 添加这行
    selectedOrderId: null,
    selectedItemImage: null,
    rejectReason: '',
        newOrder: { // 新订单表单数据
            user_id: '', // 用户ID
            order_date: '', // 订单日期
            total_amount: 0 // 订单总额
        },
        // 新增减碳计算器相关数据
    travelTypes: [],
    newTravelType: {
      travel_type: '',
      carbon_reduction_per_unit: 0
    },
    showAddTravelType: false, // 控制添加出行方式表单显示
  

        // 论坛管理相关属性

        forumPosts: [], // 论坛帖子列表
    forumSearch: '', // 论坛搜索关键词
    showAddForumPost: false, // 控制添加帖子表单显示
    newForumPost: { // 新帖子表单数据
      title: '',
      content: ''
    },
    editingForumPost: null // 当前编辑的帖子
    
    };
  },
  computed: {
    // 移除或修改filteredEvents计算属性
    // filteredEvents() {
    //     return this.filteredEvents; // 直接返回原始数据，不进行过滤
    // }
},
  mounted() {
    this.fetchUsers();
    this.fetchProducts(); // 添加这行
    this.fetchGarbageItems(); // 添加这行
    this.fetchEvents(); // 添加这行
    this.fetchForumPosts(); // 添加这行
  },
  methods: {
    //管理员页面退出登录
    logout() {
      localStorage.removeItem('token');
      localStorage.removeItem('userRole');
      this.$router.push('/login');
    },
    // 用户管理方法
    fetchUsers() {
      axios.get('/api/admin/users')
        .then(response => {
          this.users = response.data.map(user => ({
            ...user,
            editing: false,
            editData: null
          }));
        });
    },
    searchUsers() {
      axios.get(`/api/admin/users?search=${this.userSearch}`)
        .then(response => {
          this.users = response.data;
        });
    },
    addUser() {
    // 检查用户名是否已存在
    const usernameExists = this.users.some(user => 
      user.username.toLowerCase() === this.newUser.username.toLowerCase()
    );
    
    if (usernameExists) {
      alert('该用户名已存在，请使用其他用户名');
      return;
    }

    axios.post('/api/admin/users', this.newUser)
      .then(() => {
        this.fetchUsers();
        this.showAddUser = false;
        this.newUser = { username: '', password: '', isAdmin: false };
      })
      .catch(error => {
        console.error('添加用户失败:', error);
        alert('添加用户失败，请重试');
      });
  },
    editUser(user) {
      // 编辑用户逻辑
    },
    deleteUser(id) {
      if (confirm('确定要删除这个用户吗？')) {
        axios.delete(`/api/admin/users/${id}`)
          .then(() => {
            this.fetchUsers();
          });
      }
    },
    
    // 开始编辑用户
    startEdit(user) {
      this.$set(user, 'editing', true);
      this.$set(user, 'editData', {
        username: user.username,
        password: user.password,
        isAdmin: user.isAdmin
      });
    },
    
    // 保存编辑
    saveEdit(user) {
      axios.put(`/api/admin/users/${user.id}`, user.editData)
        .then(() => {
          Object.assign(user, user.editData);
          user.editing = false;
          this.fetchUsers(); // 刷新列表
        })
        .catch(error => {
          console.error('更新用户失败:', error);
        });
    },
    
    // 取消编辑
    cancelEdit(user) {
      user.editing = false;
    },
    
    // 获取用户列表
    fetchUsers() {
      axios.get('/api/admin/users')
        .then(response => {
          this.users = response.data.map(user => ({
            ...user,
            editing: false,
            editData: null
          }));
        });
    },
    // 垃圾分类管理方法
    fetchGarbageItems() {
    axios.get('/api/garbage-items')
        .then(response => {
            this.originalGarbageItems = response.data.map(item => ({
                ...item,
                editing: false,
                editData: null
            }));
            this.garbageItems = [...this.originalGarbageItems]; // 初始化显示列表
        });
},
  searchGarbageItems() {
        if (!this.garbageSearch) {
            this.garbageItems = [...this.originalGarbageItems];
            return;
        }
        this.garbageItems = this.originalGarbageItems.filter(item => 
            item.name && item.name.toLowerCase().includes(this.garbageSearch.toLowerCase())
        );
    },
  // 处理垃圾图片上传
  handleGarbageImageUpload(event) {
    const file = event.target.files[0];
    this.convertToBase64(file).then(base64 => {
      this.newGarbageItem.image = base64;
    });
  },
  
  handleEditGarbageImageUpload(event, item) {
    const file = event.target.files[0];
    this.convertToBase64(file).then(base64 => {
      item.editData.image = base64;
    });
  },
  
  // 修改添加垃圾项方法
  addGarbageItem() {
    axios.post('/api/garbage-items', this.newGarbageItem)
      .then(() => {
        this.fetchGarbageItems();
        this.newGarbageItem = { 
          name: '', 
          category: '可回收物',
          description: '',
          detailedDescription: '',
          image: null
        };
      });
  },
  
  // 修改开始编辑方法
  startEditGarbageItem(item) {
    this.$set(item, 'editing', true);
    this.$set(item, 'editData', {
      name: item.name,
      category: item.category,
      description: item.description,
      detailedDescription: item.detailedDescription,
      image: item.image
    });
  },
  
  // 修改保存编辑方法
  saveEditGarbageItem(item) {
    axios.put(`/api/garbage-items/${item.id}`, item.editData)
      .then(() => {
        Object.assign(item, item.editData);
        item.editing = false;
        this.fetchGarbageItems();
      });
  },
  
  cancelEditGarbageItem(item) {
    item.editing = false;
  },
  deleteGarbageItem(id) {
    if (confirm('确定要删除这个垃圾项吗？')) {
      axios.delete(`/api/garbage-items/${id}`)
        .then(() => {
          this.fetchGarbageItems();
        });
    }
  },
    // 产品管理方法
    fetchProducts() {
    axios.get('/api/admin/products')
        .then(response => {
            this.originalProducts = response.data.map(product => ({
                ...product,
                editing: false,
                editData: null
            }));
            this.products = [...this.originalProducts]; // 初始化显示列表
        });
},
    searchProducts() {
        if (!this.productSearch) {
            this.products = [...this.originalProducts];
            return;
        }
        this.products = this.originalProducts.filter(product => 
            product.name && product.name.toLowerCase().includes(this.productSearch.toLowerCase())
        );
    },
     // 处理图片上传
  handleImageUpload(event) {
    const file = event.target.files[0];
    this.convertToBase64(file).then(base64 => {
      this.newProduct.image = base64;
    });
  },
  
  handleEditImageUpload(event, product) {
    const file = event.target.files[0];
    this.convertToBase64(file).then(base64 => {
      product.editData.image = base64;
    });
  },
  
  convertToBase64(file) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result.split(',')[1]);
      reader.onerror = error => reject(error);
    });
  },
  
  // 修改添加产品方法
  addProduct() {
    axios.post('/api/admin/products', this.newProduct)
      .then(() => {
        this.fetchProducts();
        this.newProduct = { 
          name: '', 
          description: '',
          detailedDescription: '',
          image: null
        };
      });
  },
  
  // 修改保存编辑方法
  saveEditProduct(product) {
    axios.put(`/api/admin/products/${product.id}`, product.editData)
      .then(() => {
        Object.assign(product, product.editData);
        product.editing = false;
        this.fetchProducts();
      });
  },
  
  // 修改开始编辑方法
  startEditProduct(product) {
    this.$set(product, 'editing', true);
    this.$set(product, 'editData', {
      name: product.name,
      description: product.description,
      detailedDescription: product.detailedDescription,
      image: product.image
    });
  },
    cancelEditProduct(product) {
      product.editing = false;
    },
    deleteProduct(id) {
      if (confirm('确定要删除这个产品吗？')) {
        axios.delete(`/api/admin/products/${id}`)
          .then(() => {
            this.fetchProducts();
          });
      }
    },
    // 环保活动管理方法
    fetchEvents() {
    axios.get('/api/admin/events')
        .then(response => {
            this.events = response.data.map(event => ({
                ...event,
                editing: false,
                tempData: {}
            }));
            this.filteredEvents = [...this.events]; // 初始化filteredEvents
        })
        .catch(error => {
            console.error('获取环保活动数据时出错:', error);
        });
},
    
    addEvent() {
        axios.post('/api/admin/events', this.newEvent)
            .then(() => {
                this.fetchEvents();
                this.showAddEvent = false;
                this.newEvent = { event_name: '', start_date: '', end_date: '' };
            })
            .catch(error => {
                console.error('添加环保活动时出错:', error);
            });
    },
    
    editEvent(event) {
        event.editing = true;
        event.tempData = {
            event_name: event.event_name,
            start_date: event.start_date,
            end_date: event.end_date
        };
    },
    
    saveEditEvent(event) {
        axios.put(`/api/admin/events/${event.id}`, {
            event_name: event.tempData.event_name,
            start_date: event.tempData.start_date,
            end_date: event.tempData.end_date
        })
        .then(() => {
            event.editing = false;
            this.fetchEvents();
        })
        .catch(error => {
            console.error('更新环保活动时出错:', error);
        });
    },
    
    cancelEditEvent(event) {
        event.editing = false;
        Object.assign(event, event.tempData);
    },
    searchEvents() {
    if (!this.eventSearch) {
        this.filteredEvents = [...this.events];
        return;
    }
    this.filteredEvents = this.events.filter(event => 
        event.event_name && event.event_name.toLowerCase().includes(this.eventSearch.toLowerCase())
    );
},
    deleteEvent(id) {
        if (confirm('确定要删除这个环保活动吗？')) {
            axios.delete(`/api/admin/events/${id}`)
                .then(() => {
                    this.fetchEvents();
                })
                .catch(error => {
                    console.error('删除环保活动时出错:', error);
                });
        }
    },
    editEvent(event) {
        event.editing = true;
        event.tempData = {
            event_name: event.event_name,
            location: event.location, // 新增
            description: event.description, // 新增
            start_date: event.start_date,
            end_date: event.end_date
        };
    },
    
    saveEditEvent(event) {
        axios.put(`/api/admin/events/${event.id}`, {
            event_name: event.tempData.event_name,
            location: event.tempData.location, // 新增
            description: event.tempData.description, // 新增
            start_date: event.tempData.start_date,
            end_date: event.tempData.end_date
        })
        .then(() => {
            event.editing = false;
            this.fetchEvents();
        })
        .catch(error => {
            console.error('更新环保活动时出错:', error);
        });
    },
    
    addEvent() {
        axios.post('/api/admin/events', this.newEvent)
            .then(() => {
                this.fetchEvents();
                this.showAddEvent = false;
                this.newEvent = { 
                    event_name: '', 
                    location: '', // 新增
                    description: '', // 新增
                    start_date: '', 
                    end_date: '' 
                };
            })
            .catch(error => {
                console.error('添加环保活动时出错:', error);
            });
    },
    formatDate(dateString) {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleString(); // 或者使用更精确的格式化方式
    },
    // 闲置物品管理方法
    fetchItems() {
        axios.get('/api/second-hand-items')
            .then(response => {
                this.items = response.data;
                this.filteredItems = [...this.items]; // 初始化filteredItems
            });
    },
    searchItems() {
        if (!this.itemSearch) {
            this.filteredItems = [...this.items];
            return;
        }
        this.filteredItems = this.items.filter(item => 
            item.item_name && item.item_name.includes(this.itemSearch)
        );
    },
    startEditItem(item) {
        this.$set(item, 'editing', true);
        this.$set(item, 'editData', {
            item_name: item.item_name,
            item_description: item.item_description
        });
    },
    
    saveEditItem(item) {
        axios.put(`/api/second-hand-items/${item.id}`, item.editData)
            .then(() => {
                Object.assign(item, item.editData);
                item.editing = false;
                this.fetchItems();
            });
    },
    
    cancelEditItem(item) {
        item.editing = false;
    },
    
    deleteItem(id) {
        if (confirm('确定要删除这个物品吗？')) {
            axios.delete(`/api/second-hand-items/${id}`)
                .then(() => {
                    this.fetchItems();
                });
        }
    },
    addItem() {
        axios.post('/api/second-hand-items', this.newItem)
            .then(() => {
                this.fetchItems();
                this.showAddItem = false;
                this.newItem = { 
                    item_name: '', 
                    item_description: '',
                    phone_number: '' // 重置手机号字段
                };
            });
    },
     
    handleEditItemImageUpload(event, item) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = (e) => {
                const base64Image = e.target.result.split(',')[1];
                this.$set(item.editData, 'image', base64Image);
            };
            reader.readAsDataURL(file);
        }
    },
    
    // 确保这个方法也存在
    handleItemImageUpload(event) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = (e) => {
                this.newItem.image = e.target.result.split(',')[1];
            };
            reader.readAsDataURL(file);
        }
    },
    
    // 开始编辑物品
    startEditItem(item) {
        this.$set(item, 'editing', true);
        this.$set(item, 'editData', {
            item_name: item.item_name,
            item_description: item.item_description,
            phone_number: item.phone_number,
            image: item.image // 确保图片也被初始化
        });
    },
    
    // 保存编辑
    async saveEditItem(item) {
        try {
            // 1. 先更新基本数据
            await axios.put(`/api/second-hand-items/${item.id}`, {
                item_name: item.editData.item_name,
                item_description: item.editData.item_description,
                phone_number: item.editData.phone_number
            });
            
            // 2. 如果有新图片则上传
            if (item.editData.image && item.editData.image !== item.image) {
                const formData = new FormData();
                const blob = await fetch(`data:image/jpeg;base64,${item.editData.image}`)
                    .then(res => res.blob());
                formData.append('file', blob);
                
                await axios.post(`/api/second-hand-items/${item.id}/image`, formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                });
            }
            
            // 3. 更新本地数据
            Object.assign(item, item.editData);
            item.editing = false;
            this.fetchItems(); // 刷新列表
        } catch (error) {
            console.error('保存编辑失败:', error);
            alert('保存失败: ' + (error.response?.data?.message || error.message));
        }
    },
    
    addItem() {
    axios.post('/api/second-hand-items', this.newItem)
        .then(response => {
            this.fetchItems();
            this.showAddItem = false;
            this.newItem = { 
                item_name: '', 
                item_description: '' 
            };
        })
        .catch(error => {
            console.error('添加闲置物品时出错:', error.response?.data || error.message);
            alert('添加失败: ' + (error.response?.data || error.message));
        });
},
// fetchOrders() {
//     axios.get('/api/admin/reservation-orders')
//       .then(response => {
//         // 排序：未审核(status='待审核')的排在最前面
//         this.orders = response.data.sort((a, b) => {
//           if (a.status === '待审核' && b.status !== '待审核') return -1;
//           if (a.status !== '待审核' && b.status === '待审核') return 1;
//           return 0;
//         });
//         this.filteredOrders = [...this.orders];
//       });
// },
  
// searchOrders() {
//     if (!this.orderSearch) {
//       this.filteredOrders = [...this.orders];
//       return;
//     }
//     // 先过滤再排序
//     this.filteredOrders = this.orders.filter(order => 
//       order.product_name && order.product_name.includes(this.orderSearch)
//     ).sort((a, b) => {
//       if (a.status === '待审核' && b.status !== '待审核') return -1;
//       if (a.status !== '待审核' && b.status === '待审核') return 1;
//       return 0;
//     });
// },
  
  approveOrder(id) {
    axios.put(`/api/admin/reservation-orders/${id}/approve`)
      .then(() => {
        this.fetchOrders();
      });
  },
  
  rejectOrder(id) {
    this.selectedOrderId = id;
    this.showRejectDialog = true;
  },
  
  confirmReject() {
  axios.put(`/api/admin/reservation-orders/${this.selectedOrderId}/reject`, 
    { reason: this.rejectReason }, // 修改为发送JSON对象
    {
      headers: {
        'Content-Type': 'application/json' // 明确指定Content-Type
      }
    }
  )
    .then(() => {
      this.showRejectDialog = false;
      this.rejectReason = '';
      this.fetchOrders();
    })
    .catch(error => {
      console.error('拒绝订单失败:', error);
    });
},
  // 订单管理方法
  fetchOrders() {
    axios.get('/api/admin/reservation-orders')
      .then(response => {
        // 排序：未审核(status='待审核')的排在最前面，其余按日期降序
        this.orders = response.data.sort((a, b) => {
          if (a.status === '待审核' && b.status !== '待审核') return -1;
          if (a.status !== '待审核' && b.status === '待审核') return 1;
          return new Date(b.order_date) - new Date(a.order_date);
        });
        this.filteredOrders = [...this.orders];
      });
},
searchOrders() {
    if (!this.orderSearch) {
      this.filteredOrders = [...this.orders];
      return;
    }
    // 先过滤再排序
    this.filteredOrders = this.orders.filter(order => 
      order.product_name && order.product_name.includes(this.orderSearch)
    ).sort((a, b) => {
      if (a.status === '待审核' && b.status !== '待审核') return -1;
      if (a.status !== '待审核' && b.status === '待审核') return 1;
      return new Date(b.order_date) - new Date(a.order_date);
    });
},
  
  approveOrder(id) {
    axios.put(`/api/admin/reservation-orders/${id}/approve`)
      .then(() => {
        this.fetchOrders();
      });
  },
  
  rejectOrder(id) {
    this.selectedOrderId = id;
    this.showRejectDialog = true;
  },
  
  confirmReject() {
  axios.put(`/api/admin/reservation-orders/${this.selectedOrderId}/reject`, 
    { reason: this.rejectReason }, // 发送JSON对象
    {
      headers: {
        'Content-Type': 'application/json' // 明确指定Content-Type
      }
    }
  )
  .then(() => {
    this.showRejectDialog = false;
    this.rejectReason = '';
    this.fetchOrders();
  })
  .catch(error => {
    console.error('拒绝订单失败:', error);
  });
},
//减碳计算器
// 开始编辑出行方式
editTravelType(type) {
    this.$set(type, 'editing', true);
    this.$set(type, 'editData', {
      travel_type: type.travel_type,
      carbon_reduction_per_unit: type.carbon_reduction_per_unit
    });
  },

  // 保存编辑的出行方式
  saveTravelType(type) {
    axios.put(`/api/admin/carbon-calculator/${type.id}`, type.editData)
      .then(() => {
        Object.assign(type, type.editData);
        type.editing = false;
        this.fetchTravelTypes();
      });
  },

  // 取消编辑
  cancelEditTravelType(type) {
    type.editing = false;
  },

  // 删除出行方式
  deleteTravelType(id) {
    if (confirm('确定要删除这个出行方式吗？')) {
      axios.delete(`/api/admin/carbon-calculator/${id}`)
        .then(() => {
          this.fetchTravelTypes();
        });
    }
  },
fetchTravelTypes() {
    axios.get('/api/admin/carbon-calculator')
      .then(response => {
        this.travelTypes = response.data;
      })
      .catch(error => {
        console.error('获取出行方式数据失败:', error);
      });
  },
  addTravelType() {
    axios.post('/api/admin/carbon-calculator', this.newTravelType)
      .then(() => {
        this.fetchTravelTypes();
        this.showAddTravelType = false;
        this.newTravelType = { travel_type: '', carbon_reduction_per_unit: 0 };
      });
  },

  // 论坛管理方法
  // 获取论坛帖子列表

addForumPost() {
        axios.post('/api/admin/forum-posts', this.newForumPost)
            .then(() => {
                this.fetchForumPosts();
                this.showAddForumPost = false;
                this.newForumPost = { title: '', content: '' };
            })
            .catch(error => {
                console.error('添加帖子失败:', error);
            });
    },

    // 保存编辑的帖子
    saveEditForumPost(post) {
        axios.put(`/api/admin/forum-posts/${post.id}`, post.editData)
            .then(() => {
                this.fetchForumPosts();
                post.editing = false;
            })
            .catch(error => {
                console.error('更新帖子失败:', error);
            });
    },

    // 删除帖子
    deleteForumPost(id) {
        axios.delete(`/api/admin/forum-posts/${id}`)
            .then(() => {
                this.fetchForumPosts();
            })
            .catch(error => {
                console.error('删除帖子失败:', error);
            });
    },



  fetchForumPosts() {
    axios.get('/api/admin/forum-posts')
      .then(response => {
        this.forumPosts = response.data.map(post => ({
          ...post,
          editing: false,
          editData: null
        }));
      });
  },

  // 搜索论坛帖子
  searchForumPosts() {
    if (!this.forumSearch) {
      this.fetchForumPosts();
      return;
    }
    this.forumPosts = this.forumPosts.filter(post => 
      post.title && post.title.includes(this.forumSearch)
    );
  },

  // 添加新帖子
  addForumPost() {
    axios.post('/api/admin/forum-posts', this.newForumPost)
      .then(() => {
        this.fetchForumPosts();
        this.showAddForumPost = false;
        this.newForumPost = { title: '', content: '' };
      });
  },

  // 开始编辑帖子
  startEditForumPost(post) {
    this.$set(post, 'editing', true);
    this.$set(post, 'editData', {
      title: post.title,
      content: post.content
    });
  },

  // 保存编辑
  saveEditForumPost(post) {
    axios.put(`/api/admin/forum-posts/${post.id}`, post.editData)
      .then(() => {
        Object.assign(post, post.editData);
        post.editing = false;
        this.fetchForumPosts();
      });
  },

  // 取消编辑
  cancelEditForumPost(post) {
    post.editing = false;
  },

  // 删除帖子
  deleteForumPost(id) {
    if (confirm('确定要删除这个帖子吗？')) {
      axios.delete(`/api/admin/forum-posts/${id}`)
        .then(() => {
          this.fetchForumPosts();
        });
    }
  }

},
created() {
    this.fetchItems();
    this.fetchOrders();
},
// 减碳计算器方法，增加后解决打开网页不显示数据的问题
created() {
    this.fetchItems();
    this.fetchOrders();
    this.fetchTravelTypes(); // 新增这行
},
};
</script>

<style scoped>
/* 管理员页面退出登录 */
.admin-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.logout-btn {
  padding: 8px 16px;
  background-color: #ff5722;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.logout-btn:hover {
  background-color: #e64a19;
}

.checkbox-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.admin-container {
  padding: 20px;
}

.admin-tabs {
  display: flex;
  margin-bottom: 20px;
  border-bottom: 1px solid #ddd;
}

.admin-tabs button {
  padding: 10px 20px;
  background: none;
  border: none;
  cursor: pointer;
  margin-right: 5px;
}

.admin-tabs button.active {
  background: #007bff;
  color: white;
  border-radius: 5px 5px 0 0;
}

.admin-section {
  background: white;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}

.search-bar {
  margin-bottom: 20px;
}

.search-bar input {
  padding: 8px;
  margin-right: 10px;
  width: 200px;
}

.admin-table {
  width: 100%;
  border-collapse: collapse;
}

.admin-table th, .admin-table td {
  padding: 12px 15px;
  border: 1px solid #ddd;
  text-align: left;
}

.admin-table th {
  background-color: #f2f2f2;
}

.admin-table tr:hover {
  background-color: #f5f5f5;
}

.form-container {
  background: #f9f9f9;
  padding: 15px;
  margin-bottom: 20px;
  border-radius: 5px;
}
input[data-v-54c38a05],
textarea[data-v-54c38a05] {
  width: 23%;
  box-sizing: border-box; /* 确保内边距和边框不会增加元素的总宽度 */
}
input[data-v-54c38a05][type="checkbox"] {
  width: 13px; /* 调整宽度 */
  height: 13px; /* 调整高度 */
  
}
input[data-v-54c38a05][type="checkbox"] {
  margin-bottom: 1px;
}
.form-container input {
  display: block;
  margin-bottom: 10px;
  padding: 8px;
}

.form-container button {
  margin-right: 10px;
}
/* // 订单管理样式 */
.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.dialog {
  background: white;
  padding: 20px;
  border-radius: 5px;
  min-width: 300px;
}

.dialog textarea {
  width: 100%;
  min-height: 100px;
  margin: 10px 0;
}
.admin-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.admin-table th, 
.admin-table td {
  padding: 12px 15px;
  border: 1px solid #ddd;
  text-align: left;
}

/* 论坛管理表格特定样式 */
.forum-management-table th:nth-child(2), /* 标题列 */
.forum-management-table td:nth-child(2) {
  width: 20%;
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.forum-management-table th:nth-child(3), /* 内容列 */
.forum-management-table td:nth-child(3) {
  width: 30%;
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.forum-management-table th:nth-child(4), /* 操作列 */
.forum-management-table td:nth-child(4) {
  width: 15%;
  min-width: 150px;
}
.action-buttons {
  display: flex;
  gap: 8px;
}
/* 按钮样式 */
/* 
.btn-edit {
  background-color: #4CAF50;
  color: white;
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-delete {
  background-color: #f44336;
  color: white;
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-save {
  background-color: #2196F3;
  color: white;
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-cancel {
  background-color: #ff9800;
  color: white;
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
} */
</style>