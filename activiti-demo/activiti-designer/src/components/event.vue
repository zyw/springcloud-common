<template>
  <div class="containers">
    <div class="loading" v-if="loading">
      Loading...
    </div>
    <template v-else>
      <div class="canvas" id="canvas" ref="canvas"></div>
      <div id="js-properties-panel" class="panel"></div>
      <ul class="buttons">
        <li>
          <a ref="saveDiagramBtn" href="javascript:" title="保存为bpmn">保存为bpmn</a>
        </li>
        <li>
          <a ref="saveSvgBtn" href="javascript:" title="保存为svg">保存为svg</a>
        </li>
      </ul>
    </template>
  </div>
</template>

<script>
import {defineComponent, toRefs, reactive, onMounted, nextTick, ref } from 'vue';
import axios from 'axios'
import { xmlStr } from '@/mock/xmlStr'
// 引入相关的依赖
import BpmnModeler from 'bpmn-js/lib/Modeler'
import {
  BpmnPropertiesPanelModule,
  BpmnPropertiesProviderModule,
} from 'bpmn-js-properties-panel';

export default defineComponent({
  name: "Event",
  setup() {
    const saveDiagramBtn = ref(null)
    const saveSvgBtn = ref(null)

    const state = reactive({
      bpmnModeler: null,
      loading: false,
      xmlUrl: '',
      defaultXmlStr: xmlStr
    })

    async function initView() {
      state.loading = true
      state.xmlUrl = await getXmlUrl()
      console.log(state.xmlUrl)
      state.loading = false
      await nextTick(() => {
        initBpmn()
      })
    }

    function getXmlUrl(){
      return new Promise(resolve => {
        setTimeout(() => {
          const url = 'https://hexo-blog-1256114407.cos.ap-shenzhen-fsi.myqcloud.com/bpmnMock.bpmn'
          resolve(url)
        }, 1000)
      })
    }

    function initBpmn() {
      // 建模
      state.bpmnModeler = new BpmnModeler({
        container: "#canvas",
        //添加控制板
        propertiesPanel: {
          parent: '#js-properties-panel'
        },
        additionalModules: [
          // 左边工具栏以及节点
          BpmnPropertiesProviderModule,
          BpmnPropertiesPanelModule
        ]
      })
      createNewDiagram()
    }

    async function createNewDiagram() {
      let bpmnXmlStr
      if (state.xmlUrl === '') {
        bpmnXmlStr = state.defaultXmlStr
        transformCanvas(bpmnXmlStr)
      } else {
        let res = await axios({
          method: 'get',
          timeout: 120000,
          url: state.xmlUrl,
          headers: { 'Content-Type': 'multipart/form-data' }
        })
        console.log(res)
        bpmnXmlStr = res['data']
        transformCanvas(bpmnXmlStr)
      }
    }

    // 将字符串转换成图并渲染
    function transformCanvas(bpmnXmlStr) {
      // 将字符串转换成图显示出来
      state.bpmnModeler.importXML(bpmnXmlStr, err => {
        if (err) {
          console.error(err)
        } else {
          success()
        }
        // 让图能自适应屏幕
        const canvas = state.bpmnModeler.get('canvas');
        canvas.zoom('fit-viewport')
      })
    }

    const success = () => {
      console.log('创建成功!')
      addBpmnListener()
      addModelerListener()
      addEventBusListener()
    }

    // 添加绑定事件
    const addBpmnListener  = () => {
      // 给图绑定事件，当图有发生改变就会触发这个事件
      state.bpmnModeler.on('commandStack.changed', function(){
        saveSVG(function(err, svg) {
          setEncoded(saveSvgBtn.value, 'diagram.svg', err ? null : svg)
        })
        saveDiagram(function(err, xml){
          setEncoded(saveDiagramBtn.value, 'diagram.bpmn', err ? null : xml)
        })
      })
    }

    // 监听 modeler
    const addModelerListener = () => {
      const events = ['shape.added', 'shape.move.end', 'shape.removed', 'connect.end', 'connection.create', 'connection.move']
      events.forEach(function(event) {
        state.bpmnModeler.on(event, e => {
          console.log(event, e)
          const elementRegistry = state.bpmnModeler.get('elementRegistry');
          const shape = e.element ? elementRegistry.get(e.element.id) : e.shape;
          console.log(shape)
          if (event === 'shape.added') {
            console.log('新增了shape')
          } else if (event === 'shape.move.end') {
            console.log('移动了shape')
          } else if (event === 'shape.removed') {
            console.log('删除了shape')
          } else if (event === 'connect.end') {
            console.log('连线了=====')
          } else if (event === 'connection.create') {
            console.log('新增了连线')
          } else if (event === 'connection.move') {
            console.log('移动了连线')
          }
        })
      })
    }

    // 监听 element
    const addEventBusListener = () => {
      const eventBus = state.bpmnModeler.get('eventBus')
      const modeling = state.bpmnModeler.get('modeling')
      const elementRegistry = state.bpmnModeler.get('elementRegistry')
      const eventTypes = ['element.click', 'element.changed']
      eventTypes.forEach(function (eventType) {
        eventBus.on(eventType, function (e){
          console.log(e)
          if (!e || e.element.type === 'bpmn:Process') return // 这里我的根元素是bpmn:Process
          if (eventType === 'element.changed') {
            elementChanged(e)
          } else if (eventType === 'element.click') {
            console.log('点击了element')
            const shape = e.element ? elementRegistry.get(e.element.id) : e.shape

            console.log(shape) // {Shape}
            console.log(e.element) // {Shape}
            console.log(JSON.stringify(shape)===JSON.stringify(e.element)) // true
            console.log(JSON.stringify(shape),"shape JSON")
            console.log(JSON.stringify(e.element),"element JSON")

            if (shape.type === 'bpmn:StartEvent') {
              modeling.updateProperties(shape, {
                name: '我是修改后的虚线节点',
                isInterrupting: false,
                customText: '我是自定义的text属性'
              })
            }
          }
        })
      })
    }

    const isInvalid = (param) => {
      // 判断是否是无效的值
      return param === null || param === undefined || param === ''
    }

    const isSequenceFlow = (type) => {
      // 判断是否是线
      return type === 'bpmn:SequenceFlow'
    }

    const elementChanged = (e) => {
      const shape = getShape(e.element.id)
      console.log(shape)
      if (!shape) {
        // 若是shape为null则表示删除，无论是shape还是connect删除都调用此处
        console.log('无效的shape')
        // 上面已经用shape.removed检测了shape的删除，要是删除shape的话这里还会再触发一次
        console.log('删除了shape和connect')
        return
      }
      if (!isInvalid(shape.type)) {
        if (isSequenceFlow(shape.type)) {
          console.log('改变了线')
        }
      }
    }

    const getShape = (id) => {
      const elementRegistry = state.bpmnModeler.get('elementRegistry')
      return elementRegistry.get(id)
    }

    // 下载为SVG格式,done是个函数，调用的时候传入的
    const saveSVG = (done) => {
      // 把传入的done再传给bpmn原型的saveSVG函数调用
      state.bpmnModeler.saveSVG(done)
    }
    // 下载为bpmn格式,done是个函数，调用的时候传入的
    const saveDiagram = (done) => {
      // 把传入的done再传给bpmn原型的saveXML函数调用
      state.bpmnModeler.saveXML({ format: true }, function(err, xml) {
        done(err, xml)
      })
    }

    // 当图发生改变的时候会调用这个函数，这个data就是图的xml
    const setEncoded = (link, name, data) => {
      // 把xml转换为URI, 下载要用到的
      const encodedData = encodeURIComponent(data)
      // 下载图的具体操作,改变a的属性，className令a标签可点击，href令能下载，download是下载的文件的名字
      console.log(link, name, data)
      let xmlFile = new File([data], 'test.bpmn')
      console.log(xmlFile)
      if (data) {
        link.className = 'active'
        link.href = "data:application/bpmn20-xml;charset=UTF-8," + encodedData
        link.download = name
      }
    }

    onMounted(() => {
      initView()
    })

    return {
      ...toRefs(state),
      saveDiagramBtn,
      saveSvgBtn
    }
  }
})
</script>

<style scoped>
.loading {
  font-size: 26px;
}
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