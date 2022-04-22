<template>
  <Event/>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';
import Event from '@/components/event.vue'

@Options({
  components: {
    Event,
  },
})
export default class EventView extends Vue {}
</script>