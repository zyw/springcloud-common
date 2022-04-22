import store from '@/store'

export default function ContextPadProvider(contextPad, config, injector, translate, bpmnFactory, elementFactory, create, modeling, connect) {
    this.create = create
    this.elementFactory = elementFactory
    this.translate = translate
    this.bpmnFactory = bpmnFactory
    this.modeling = modeling
    this.connect = connect
    config = config || {}
    if (config.autoPlace !== false) {
        this._autoPlace = injector.get('autoPlace', false)
    }
    contextPad.registerProvider(this)
}

ContextPadProvider.$inject = [
    'contextPad',
    'config',
    'injector',
    'translate',
    'bpmnFactory',
    'elementFactory',
    'create',
    'modeling',
    'connect'
]

ContextPadProvider.prototype.getContextPadEntries = function(element) {
    const {
        autoPlace,
        create,
        elementFactory,
        translate,
        modeling
    } = this;

    function appendTask(event, element) {
        if (autoPlace) {
            const shape = elementFactory.createShape({ type: 'bpmn:Task' });
            autoPlace.append(element, shape);
        } else {
            appendTaskStart(event, element);
        }
    }

    function appendTaskStart(event) {
        const shape = elementFactory.createShape({ type: 'bpmn:Task' });
        create.start(event, shape, element);
    }

    function removeElement(e) { // 点击的时候实现删除功能
        modeling.removeElements([element])
    }

    function deleteElement() { // 创建垃圾桶
        return {
            group: 'edit',
            className: 'icon-custom icon-custom-delete',
            title: translate('删除'),
            action: {
                click: removeElement
            }
        }
    }

    function clickElement(e) {
        console.log(e)
        store.commit('SETNODEINFO', element)
        store.commit('TOGGLENODEVISIBLE', true)
        console.log("======================================")
    }

    function editElement() {
        return {
            group: 'edit',
            className: 'icon-custom icon-custom-edit',
            title: translate('编辑'),
            action: {
                click: clickElement
            }
        }
    }

    return {
        'append.lindaidai-task': {
            group: 'model',
            className: 'icon-custom lindaidai-task',
            title: translate('创建一个类型为lindaidai-task的任务节点'),
            action: {
                click: appendTask,
                dragstart: appendTaskStart
            }
        },
        'delete': deleteElement(), // 返回值加上删除的功能
        'edit': editElement() // 返回值加上编辑功能
    };
}