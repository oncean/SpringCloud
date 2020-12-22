import { defineConfig } from 'umi';

export default defineConfig({
  devServer:{
    port:8001
  },
  nodeModulesTransform: {
    type: 'none',
  },
  routes: [
    { path: '/', redirect: '/login' },
    { path: '/login/github/callback', component: '@/pages/login/github/callback' },
    { path: '/login', component: '@/pages/login' },
    { path: '/home', component: '@/pages/home' },
  ],
  proxy:{
    '/api':{
      target:'http://localhost:5000',
      'changeOrigin': true,
      'pathRewrite': { '^/api' : '' },
    },
    '/github':{
      target:'https://api.github.com',
      'changeOrigin': true,
      'pathRewrite': { '^/github' : '' },
    }
  }
});
