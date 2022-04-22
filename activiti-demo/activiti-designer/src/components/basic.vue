<template>
  <div class="containers">
    <div class="canvas" id="canvas" ref="canvas"></div>
  </div>
</template>

<script>
import {defineComponent, toRefs, reactive, onMounted } from 'vue';
import BpmnViewer from 'bpmn-js';
import pizzaDiagram from '../res/pizza-collaboration.bpmn';
import myLeave from '../res/Leave.bpmn';
export default defineComponent({
  name: "BpmnModeler",
  setup() {
      const state = reactive({
        
      });

      function initBpmn(){
        console.log("----------------------")
        var viewer = new BpmnViewer({
          container: '#canvas'
        });

        viewer.importXML(pizzaDiagram, function(err) {
          console.log("==========================")
          if (!err) {
            console.log('success!');
            viewer.get('canvas').zoom('fit-viewport');
          } else {
            console.log('something went wrong:', err);
          }
        });
      }

      onMounted(() => {
        initBpmn()
      })
      
      return {
        ...toRefs(state),
      };
  }
})
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.containers{
	position: absolute;
	background-color: #ffffff;
	width: 100%;
	height: 100%;
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
