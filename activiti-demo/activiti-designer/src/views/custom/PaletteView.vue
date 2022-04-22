<template>
  <CustomPalette/>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';
import CustomPalette from '@/components/custom-palette/index.vue'

@Options({
  components: {
    CustomPalette,
  },
})
export default class PaletteView extends Vue {}
</script>