import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
// 以下为bpmn工作流绘图工具的样式
import 'bpmn-js/dist/assets/diagram-js.css' // 左边工具栏以及编辑节点的样式
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css'
import 'bpmn-js-properties-panel/dist/assets/properties-panel.css' // 右边工具栏样式
import 'bpmn-js-properties-panel/dist/assets/element-templates.css' // 右边工具栏样式

import './css/app.css'

createApp(App)
    .use(router)
    .use(store)
    .mount('#app')
