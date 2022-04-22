<template>
  <CustomContextPad/>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';
import CustomContextPad from '@/components/custom-context-pad/index.vue'

@Options({
  components: {
    CustomContextPad,
  },
})
export default class ContextPadView extends Vue {}
</script>