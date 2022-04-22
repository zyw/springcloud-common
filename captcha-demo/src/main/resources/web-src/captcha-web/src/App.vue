<template>
  <div style="text-align: center;padding: 100px 0 0 300px;">
    <captcha-demo></captcha-demo>
  </div>
  <div style="text-align: center;padding: 100px 0 0 300px;">
    <anji-plus-captcha></anji-plus-captcha>
  </div>
</template>

<script>
import CaptchaDemo from './components/CaptchaDemo.vue'
import AnjiPlusCaptcha from './components/AnjiPlusCaptcha.vue'

export default {
  name: 'App',
  components: {
    CaptchaDemo,
    AnjiPlusCaptcha
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
