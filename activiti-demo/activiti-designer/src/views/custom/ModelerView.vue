<template>
  <CustomModeler />
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';
import CustomModeler from '@/components/custom-modeler/index.vue'

@Options({
  components: {
    CustomModeler,
  },
})
export default class ModelerView extends Vue {}
</script>