<template>
  <Provider/>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';
import Provider from '@/components/provider.vue'

@Options({
  components: {
    Provider,
  },
})
export default class ProviderView extends Vue {}
</script>