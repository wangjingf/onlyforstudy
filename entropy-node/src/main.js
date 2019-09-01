import Vue from 'vue'
import App from './App.vue'
import HelloWorld from './components/HelloWorld';
//import VueI18n from 'vue-i18n'
//
Vue.component("HelloWorld",HelloWorld)
const messages = {
  en: {
    message: {
      hello: 'hello world'
    }
  },
  ja: {
    message: {
      hello: 'こんにちは、世界'
    }
  }
}
//Vue.use(VueI18n)

Vue.config.productionTip = false
/*const i18n = new VueI18n({
  locale: 'en', // 设置地区
  messages, // 设置地区信息
})*/
new Vue({
  //i18n,
  render: h => h(App),
}).$mount('#app')
