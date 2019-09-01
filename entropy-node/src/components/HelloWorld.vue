<template>
  <div class="hello">
     <div id="hello"></div>
     <button v-on:click="trigger">点击</button>
  </div>
</template>

<script>
  import Vue from 'vue';
export default {
  name: 'HelloWorld',
  props: {
    msg: String
  },
    data(){
      return {
          data:1
      }
    },
    mounted(){
        new Vue({
            template:"<div>Vue template</div>"
        }).$mount("#hello")
    },
    methods:{
      trigger(){
          this.data = 2;
          Vue.component("test",{template:'<div>change</div>'})
      }
    }
}
</script>

