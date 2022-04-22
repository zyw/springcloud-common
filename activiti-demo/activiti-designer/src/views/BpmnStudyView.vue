<template>
  <div class="content">
    <ul class="router_ul">
      <router-link v-for="link in links" :key="link.to" tag="li" active-class="activeClass" :to="link.to">&nbsp;
        <li>
          {{ link.title }}&nbsp;
        </li>
      </router-link>
    </ul>
    <router-view></router-view>
  </div>
</template>

<script lang="ts">
import { Options, Vue, setup } from 'vue-class-component'

@Options({
  data() {
    return {
      links:[
        { to: '/study', title: ' 基本使用 ' },
        { to: '/study/provider', title: ' 引入左侧工具栏以及节点 ' },
        { to: '/study/panel', title: ' 引入左侧工具栏和右侧属性栏 ' },
        { to: '/study/axios', title: ' 通过网络请求获取bpmn并渲染 ' },
        { to: '/study/save', title: ' 保存bpmn并发送给后台 ' },
        { to: '/study/event', title: ' 绑定event事件 ' }
      ]
    }
  }
})

export default class BpmnStudyView extends Vue {
  mounted() {
    this.$router.push('/study')
  }
}
</script>

<style scoped>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  /* margin-top: 10px; */
}
body {
  margin: 0;
}
.router_ul {
  display: flex;
  justify-content: center;
  margin: 0;
  list-style: none;
  padding: 10px 0;
}
.router_ul li {
  margin-left: 10px;
  padding: 5px 10px;
  font-size: 14px;
  border-radius: 5px;
  border: 1px solid #ccc;
  cursor: pointer;
  transition: all 0.3s;
}
.router_ul li.activeClass {
  background: #4d90fe;
  color: #fff;
  border: none;
}
a {
  text-decoration-line: none;
}
</style>
