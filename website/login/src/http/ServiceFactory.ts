
import { extend } from 'umi-request';
import { responseHandle } from './handle';
import { defaultHttpConfig, noAuthHttpConfig } from './config';

interface ServiceConfig{
  prefix?:string;//前缀
  noAuth?:boolean
}

const BASIC_PREFIX = '/api'

function buildPrefix(prefix?:string) {
  if(!prefix){
    return BASIC_PREFIX
  }
  if(!prefix.startsWith('/')){
    prefix = '/' + prefix 
  }

  return BASIC_PREFIX + prefix
}

const ServiceFactory = {
  generate(config?:ServiceConfig){
    if(!config){
      config = {}
    }
    //取出header中的authorization字段，防止使用过期jwt请求白名单也会失败的问题
    const httpConfig = config.noAuth?noAuthHttpConfig:defaultHttpConfig
    const service =  extend({
      ...httpConfig,
      prefix: buildPrefix(config.prefix)
    })
    service.interceptors.response.use(responseHandle)
    return service
  }
}


export default ServiceFactory