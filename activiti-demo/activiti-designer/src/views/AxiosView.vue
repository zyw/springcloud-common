<template>
  <Axios/>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component';
import Axios from '@/components/axios.vue'

@Options({
  components: {
    Axios,
  },
})
export default class AxiosView extends Vue {}
</script>