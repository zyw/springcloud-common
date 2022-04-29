<template>
  <button @click="basic()" type="button">Bpmn基础</button>
  <button @click="custom()" type="button">自定义palette</button>
  <button @click="renderer()" type="button">自定义renderer</button>
  <button @click="bpmn()" type="button">BPMN</button>
  <router-view></router-view>
</template>

<script lang="ts">

import { Options, Vue } from 'vue-class-component'

@Options({
  methods:{
    basic() {
      this.$router.push("/study")
    },
    custom() {
      this.$router.push("/custom")
    },
    renderer() {
      this.$router.push("/renderer")
    },
    bpmn() {
      console.log("bpmn----------------------")
    }
  }
})

export default class App extends Vue {
  mounted() {
    this.$router.push("/study")
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
</style>
