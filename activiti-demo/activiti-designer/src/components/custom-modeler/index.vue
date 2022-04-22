<template>
  <div class="containers">
    <div class="canvas" id="canvas" ref="canvas"></div>
    <div id="js-properties-panel" class="panel"></div>
    <div class="modal" v-if="bpmnNodeVisible" @click="closeInfo">
      <div class="modal-content">
        <div class="modal-ctx">
          <div class="modal-item">
            节点id: {{ bpmnNodeInfo.id }}
          </div>
          <div class="modal-item">
            节点type: {{ bpmnNodeInfo.type }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {computed, defineComponent, toRefs, reactive, onMounted } from 'vue';
import { mapState, mapMutations, useStore } from 'vuex'
// 引入相关的依赖
import CustomModeler from './index'

import myLeave from '@/res/Leave.bpmn';

export default defineComponent({
  name: "Provider",
  setup() {
    const state = reactive({

    });

    const store = useStore()  //记得加这一句

    function initBpmn(){
      // 建模
      const viewer = new CustomModeler({
        container: '#canvas',
        //添加控制板
        propertiesPanel: {
          parent: '#js-properties-panel'
        },
        additionalModules: []
      })
      // 将字符串转换成图显示出来
      viewer.importXML(myLeave, (err) => {
        console.log("==========================")
        if (!err) {
          console.log('success!');
          // viewer.get('canvas').zoom('fit-viewport');
        } else {
          console.log('something went wrong:', err);
        }
      })
    }

    onMounted(() => {
      initBpmn()
    })

    // const bpmnNodeInfo = computed(() => store.state.bpmn.nodeInfo)
    // const bpmnNodeVisible = computed(() => store.state.bpmn.nodeVisible)

    const closeInfo = () => {
      console.log("关闭信息框。。。。")
      store.commit("TOGGLENODEVISIBLE", false)
    }

    return {
      ...toRefs(state),
      // bpmnNodeInfo,
      // bpmnNodeVisible,
      closeInfo
    }
  },
  computed: {
    ...mapState({ // 解构
      bpmnNodeInfo: state => state.bpmn.nodeInfo,
      bpmnNodeVisible: state => state.bpmn.nodeVisible
    })
  },
  methods: {
    // 使用mapMutations
    ...mapMutations(['TOGGLENODEVISIBLE']),
    close() {
      this.TOGGLENODEVISIBLE(false)
    }
  }
})
</script>

<style scoped>
.containers {
  background-color: #ffffff;
  width: 100%;
  height: calc(100vh - 52px);
}
.canvas {
  width: 100%;
  height: 100%;
}
.panel {
  position: absolute;
  right: 0;
  top: 0;
  width: 300px;
}
.buttons {
  position: absolute;
  left: 20px;
  bottom: 20px;
}
.buttons li {
  display: inline-block;
  margin: 5px;
}
.buttons li a {
  color: #999;
  background: #eee;
  cursor: not-allowed;
  padding: 8px;
  border: 1px solid #ccc;
  text-decoration: none;
}
.buttons li a.active {
  color: #333;
  background: #fff;
  cursor: pointer;
}
.modal {
  background-color: rgba(0, 0, 0, 0.6);
  width: 100vw;
  height: 100vh;
  position: fixed;
  top: 0;
  left: 0;
}
.modal-content {
  width: 100%;
  height: 100%;
  position: relative;
}
.modal-ctx {
  position: absolute;
  width: 300px;
  height: 250px;
  background-color: #fff;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  box-shadow: 0px 0px 5px 2px rgba(225, 225, 225, 0.8);
}
.modal-item {
  padding: 10px;
  width: 100%;
}
</style>