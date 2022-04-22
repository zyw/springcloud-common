const bpmn = {
    state: {
        nodeVisible: false,
        nodeInfo: {}
    },
    mutations: {
        TOGGLENODEVISIBLE: (state: any, visible: boolean) => {
            console.log("TOGGLENODEVISIBLE====", state, visible)
            state.nodeVisible = visible
        },
        SETNODEINFO: (state: any, info: any) => {
            console.log("SETNODEINFO=========", state, info)
            state.nodeInfo = info
        }
    },
    actions:{}
}

export default bpmn