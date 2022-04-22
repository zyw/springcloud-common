<template>
  <CustomRenderer/>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';
import CustomRenderer from '@/components/custom-renderer/custom-renderer.vue'

@Options({
  components: {
    CustomRenderer,
  },
})
export default class RendererView extends Vue {}
</script>