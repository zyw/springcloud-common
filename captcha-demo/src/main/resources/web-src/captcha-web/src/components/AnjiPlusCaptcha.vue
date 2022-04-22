<template>
  <div class="hello">
    <button @click="onShow('blockPuzzle')">滑块</button>
    <button @click="onShow('clickWord')">点击文字</button>
    <Verify
        mode="pop"
        :captchaType="captchaType"
        :imgSize="{width:'400px',height:'200px'}"
        ref="verify"
    ></Verify>
  </div>
</template>

<script>
import Verify from '@/components/verifition/Verify'
import { ref } from "vue";
export default {
  name: 'AnjiPlusCaptcha',
  components: {
    Verify
  },
  props: {
    msg: String
  },
  //props,context
  setup(){
    const verify = ref(null)
    const captchaType = ref('')
    const onShow = (type)=>{
      captchaType.value = type
      verify.value.show()
    }
    const handleSuccess = (res)=>{
      console.log(res);
      console.log('sucess');
    }
    return {
      verify,
      handleSuccess,
      onShow,
      captchaType
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
