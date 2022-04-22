<template>
  <div class="containers">
    <div class="canvas" id="canvas" ref="canvas"></div>
    <div id="js-properties-panel" class="panel"></div>
  </div>
</template>

<script>
import {defineComponent, toRefs, reactive, onMounted } from 'vue';
// 引入相关的依赖
import BpmnModeler from 'bpmn-js/lib/Modeler'
import {
  BpmnPropertiesPanelModule,
  BpmnPropertiesProviderModule,
} from 'bpmn-js-properties-panel';
import myLeave from '@/res/Leave.bpmn';
import customContextPad from './custom'

export default defineComponent({
  name: "ContextPad",
  setup() {
    const state = reactive({

    });

    function initBpmn(){
      // 建模
      const viewer = new BpmnModeler({
        container: '#canvas',
        //添加控制板
        propertiesPanel: {
          parent: '#js-properties-panel'
        },
        additionalModules: [
          // 左边工具栏以及节点
          BpmnPropertiesPanelModule,
          // BpmnPropertiesProviderModule,
          // 自定义的节点
          customContextPad
        ]
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

    return {
      ...toRefs(state)
    }
  }
})
</script>

<style scoped>
.containers{
  background-color: #ffffff;
  width: 100%;
  height: calc(100vh - 52px);
}
.canvas{
  width: 100%;
  height: 100%;
}
.panel{
  position: absolute;
  right: 0;
  top: 0;
  width: 300px;
}
</style>