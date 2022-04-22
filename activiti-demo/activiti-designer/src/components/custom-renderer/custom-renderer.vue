<template>
  <div class="containers">
    <div class="canvas" id="canvas" ref="canvas"></div>
    <div id="js-properties-panel" class="panel"></div>
    <ul class="buttons">
      <li>
        <a ref="saveDiagram" href="javascript:" title="保存为bpmn">保存为bpmn</a>
      </li>
    </ul>
  </div>
</template>

<script>
import {defineComponent, toRefs, reactive, onMounted, ref } from 'vue';
// 引入相关的依赖
import BpmnModeler from 'bpmn-js/lib/Modeler'
import {
  BpmnPropertiesPanelModule,
  BpmnPropertiesProviderModule,
} from 'bpmn-js-properties-panel';
import diagram from '@/res/diagram.bpmn';
import customRenderer from './custom'

export default defineComponent({
  name: "customRenderer",
  setup() {
    const saveDiagram = ref(null)
    const state = reactive({
      // bpmn建模器
      bpmnModeler: null
    });

    function initBpmn(){
      // 建模
      state.bpmnModeler = new BpmnModeler({
        container: '#canvas',
        //添加控制板
        propertiesPanel: {
          parent: '#js-properties-panel'
        },
        additionalModules: [
          // 左边工具栏以及节点
          BpmnPropertiesPanelModule,
          BpmnPropertiesProviderModule,
          // 自定义的节点
          customRenderer
        ]
      })
      console.log(diagram)
      console.log(typeof diagram) // object
      console.log(diagram.default)
      // 将字符串转换成图显示出来
      state.bpmnModeler.importXML(diagram, (err) => {
        console.log("==========================")
        if (!err) {
          console.log('success!');
          // viewer.get('canvas').zoom('fit-viewport');
          success()
        } else {
          console.log('something went wrong:', err);
        }
      })
    }

    function success() {
      addBpmnListener()
    }

    function addBpmnListener() {
      state.bpmnModeler.on('commandStack.changed', function (){
        saveDiagramFun(function(err, xml) {
          setEncoded(saveDiagram.value, 'diagram.bpmn', err ? null : xml)
        })
      })
    }

    // 下载为bpmn格式,done是个函数，调用的时候传入的
    function saveDiagramFun(done) {
      // 把传入的done再传给bpmn原型的saveXML函数调用
      state.bpmnModeler.saveXML({ format: true }, function(err, xml) {
        done(err, xml)
      })
    }
    // 当图发生改变的时候会调用这个函数，这个data就是图的xml
    function setEncoded(link, name, data) {
      // 把xml转换为URI，下载要用到的
      const encodedData = encodeURIComponent(data)
      // 下载图的具体操作,改变a的属性，className令a标签可点击，href令能下载，download是下载的文件的名字
      console.log(link, name, data)
      let xmlFile = new File([data], 'test.bpmn')
      //   console.log(xmlFile)
      if (data) {
        link.className = 'active'
        link.href = 'data:application/bpmn20-xml;charset=UTF-8,' + encodedData
        link.download = name
      }
    }

    onMounted(() => {
      initBpmn()
    })

    return {
      ...toRefs(state),
      saveDiagram
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
</style>