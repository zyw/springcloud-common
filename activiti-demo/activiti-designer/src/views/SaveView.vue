<template>
  <Save/>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';
import Save from '@/components/save.vue'

@Options({
  components: {
    Save,
  },
})
export default class SaveView extends Vue {}
</script>