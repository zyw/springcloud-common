<template>
  <Panel/>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';
import Panel from '@/components/panel.vue'

@Options({
  components: {
    Panel,
  },
})
export default class PanelView extends Vue {}
</script>