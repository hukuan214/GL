<template>
	<div class="container">
	    <aside class="sidebar">
	        <h2>左边栏</h2>
	        <ul>
	            <li v-for="menu in $store.getters.getMenus"><a  @click="toPage(menu.link)">{{menu.menuName}}</a></li>
	        </ul>
	    </aside>
	</div>
</template>

<script>
	export default{
		methods:{
			toPage(link){
				this.to(link)
			}
		}
	}
</script>


<style>
	.container {
	    display: flex;
	    flex-wrap: nowrap;
		min-height: 100vh;
	}
	
	.sidebar {
	    background-color: #354052;
	    width: 150px;
	    padding: 20px;
	    box-shadow: 2px 0 5px rgba(0,0,0,0.1);
		color: white;
	}
	
	.sidebar h2 {
	    margin-top: 0;
	}
	
	.sidebar ul {
	    list-style-type: none;
	    padding: 0;
	}
	
	.sidebar ul li {
	    margin-bottom: 10px;
	}
	
	.sidebar ul li a {
	    text-decoration: none;
	    color: white;
		display: block; /* 让链接充满整个li元素的宽度 */
	}
	
	.sidebar ul li a:hover {
	    text-decoration: underline;
		background-color: #49546a; /* 鼠标悬停时的背景颜色 */
	}
	
</style>