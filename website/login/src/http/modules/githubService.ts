
import { extend } from 'umi-request';

const token = localStorage.getItem('Authorization')
export default extend({
  prefix: '/github',
  timeout: 3000,
  headers: {
    'Authorization': `bearer ${token}`
  },
});