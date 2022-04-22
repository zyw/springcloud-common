<template>
  <div class="containers">
    <div class="loading" v-if="loading">
      Loading...
    </div>
    <template v-else>
      <div class="canvas" id="canvas" ref="canvas"></div>
      <div id="js-properties-panel" class="panel"></div>
    </template>
  </div>
</template>

<script>
import {defineComponent, toRefs, reactive, onMounted, nextTick } from 'vue';
import axios from 'axios'
import { xmlStr } from '../mock/xmlStr'
// 引入相关的依赖
import BpmnModeler from 'bpmn-js/lib/Modeler'
import {
  BpmnPropertiesPanelModule,
  BpmnPropertiesProviderModule,
} from 'bpmn-js-properties-panel';

export default defineComponent({
    name: "Provider",
    setup() {
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
            nextTick(() => {
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
            let bpmnXmlStr = ''
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

        function transformCanvas(bpmnXmlStr) {
            // 将字符串转换成图显示出来
            state.bpmnModeler.importXML(bpmnXmlStr, err => {
                if (err) {
                    console.error(err)
                } else {
                    console.log('创建成功!')
                }
                // 让图能自适应屏幕
                var canvas = state.bpmnModeler.get('canvas')
                canvas.zoom('fit-viewport')
            })
        }

        onMounted(() => {
            initView()
        })

        return {
            ...toRefs(state)
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
</style>