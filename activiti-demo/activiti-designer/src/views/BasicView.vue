<template>
  <BpmnModeler/>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';
import BpmnModeler from '@/components/basic.vue'

@Options({
  components: {
    BpmnModeler,
  },
})
export default class BasicView extends Vue {}
</script>