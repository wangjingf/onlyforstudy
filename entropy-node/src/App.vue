<template>
  <div id="app">
  <!--  <p>{{ $t("message.hello") }}</p>-->
    <Main/>
  </div>
</template>

<script>
import Main from './components/Main.vue'
import '@/assets/css/style.scss';
export default {
  name: 'app', components: {
    Main
  }

}
</script>


