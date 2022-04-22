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
  CamundaPlatformPropertiesProviderModule,
} from 'bpmn-js-properties-panel';
import CamundaBpmnModdle from 'camunda-bpmn-moddle/resources/camunda.json'
import myLeave from '../res/Leave.bpmn';

export default defineComponent({
    name: "Panel",
    data() {
        return {
            viewer: null
        }
    },
    setup() {
        const state = reactive({
            viewer: null
        });

        function initBpmn(){
            // 建模
            state.viewer = new BpmnModeler({
                container: '#canvas',
                //添加控制板
                propertiesPanel: {
                    parent: '#js-properties-panel'
                },
                additionalModules: [
                    // 左边工具栏以及节点
                    BpmnPropertiesPanelModule,
                    BpmnPropertiesProviderModule,
                    CamundaPlatformPropertiesProviderModule,
                ],
                moddleExtensions: {
                    camunda: CamundaBpmnModdle,
                }
            })
            // 将字符串转换成图显示出来
            state.viewer.importXML(myLeave, (err) => {
                console.log("==========================")
                if (!err) {
                    console.log('success!');
                    // viewer.get('canvas').zoom('fit-viewport');
                    addEventBusListener()
                } else {
                    console.log('something went wrong:', err);
                }
            })
        }

        function addEventBusListener() {
            // 监听 element
            let that = this
            const eventBus = state.viewer.get('eventBus')
            const eventTypes = ['element.click', 'element.changed', 'element.updateLabel']
            eventTypes.forEach(function(eventType) {
                eventBus.on(eventType, function(e) {
                    console.log(eventType)
                    if (!e || e.element.type == 'bpmn:Process') return
                    if (eventType === 'element.changed') {
                        // that.elementChanged(e)
                    } else if (eventType === 'element.click') {
                        console.log('点击了element', e)
                        // if (e.element.type === 'bpmn:Task') {
                        // }
                    } else if (eventType === 'element.updateLabel') {
                        console.log('element.updateLabel', e.element)
                    }
                })
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