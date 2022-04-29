const flowAction = { // 线
    type: 'global-connect-tool',
    action: ['bpmn:SequenceFlow', 'tools', 'icon-custom icon-custom-flow', '连接线']
}
const customShapeAction = [ //shape
    {
        type: 'create.start-event',
        action: ['bpmn:StartEvent', 'event', 'icon-custom icon-custom-start', '开始节点']
    },
    {
        type: 'create.end-event',
        action: ['bpmn:EndEvent', 'event', 'icon-custom icon-custom-end', '结束节点']
    },
    {
        type: 'create.task',
        action: ['bpmn:Task', 'activity', 'icon-custom icon-custom-task', '普通任务']
    },
    {
        type: 'create.businessRule-task',
        action: ['bpmn:BusinessRuleTask', 'activity', 'icon-custom icon-custom-businessRule', 'businessRule任务']
    },
    {
        type: 'create.exclusive-gateway',
        action: ['bpmn:ExclusiveGateway', 'activity', 'icon-custom icon-custom-exclusive-gateway', '网关']
    },
    {
        type: 'create.dataObjectReference',
        action: ['bpmn:DataObjectReference', 'activity', 'icon-custom icon-custom-data', '变量']
    }
]
const customFlowAction = [
    flowAction
]

/**
 * 循环创建出一系列的元素
 * @param { Array } actions 元素集合
 * @param { Function } fn 处理的函数
 */
export function batchCreateCustom(actions, fn) {
    const customs = {}
    actions.forEach(item => {
        customs[item['type']] = fn(...item['action'])
    })
    return customs
}

export { customFlowAction, customShapeAction }