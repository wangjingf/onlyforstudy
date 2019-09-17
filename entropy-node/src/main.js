import Vue from 'vue'
import App from './App.vue'
import VueI18n from 'vue-i18n'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

Vue.use(ElementUI, { size: 'small'});


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
Vue.use(VueI18n)

Vue.config.productionTip = false
const i18n = new VueI18n({
  locale: 'en', // 设置地区
  messages, // 设置地区信息
})
new Vue({
  i18n,
  render: h => h(App),
}).$mount('#app')
