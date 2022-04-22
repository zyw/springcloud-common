import { createApp } from 'vue'
import MakeitCaptcha from 'makeit-captcha'
import 'makeit-captcha/dist/captcha.min.css'
import App from './App.vue'

createApp(App)
    .use(MakeitCaptcha)
    .mount('#app')
