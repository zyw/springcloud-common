<template>
  <mi-captcha theme-color="#be6be0"
      @init="initAfter"
      @checked="checkedAfter"
      @success="successAfter"
      :verify-params="verifyParams"
      init-action="/api/v1/captcha/init"
      check-action="/api/v1/captcha/check"
      verify-action="/api/v1/captcha/verification"
  ></mi-captcha>
</template>

<script>
export default {
  name: "CaptchaDemo",
  data() {
    return {
      verifyParams: {}
    }
  },
  methods: {
    initAfter(res) {
      console.log(res);
      console.log("11111111111111111111111111111111111111");
      if(res.ret.code === 1) {
        localStorage.setItem('mi-captcha-key', res.data.key)
        this.verifyParams = {key: res.data.key}
      }
    },
    checkedAfter(data) {
      console.log(data)
      console.log("===========================================")
    },
    successAfter(data) {
      console.log(data)
      console.log("000000000000000000000000000000001")
    }
  }
}
</script>

<style scoped>

</style>