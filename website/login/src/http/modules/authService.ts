
import ServiceFactory from '../ServiceFactory';

const service = ServiceFactory.generate({
  prefix: 'auth-service',
  noAuth: true
})

/**
 * 获取github的clientID
 */
export const getGitHubClientId = ()=>{
  return service('/gitHub/getClientId')
}

/**
 * 获取github的clientID
 */
export const getGitHubCallback = (code:string)=>{
  return service('/gitHub/callback?code='+code)
}



/**
 * 获取rsa加密的key
 */
export const getRSAPublicKey = ()=>{
  return service('/getPublicKey')
}

/**
 * 获取rsa加密的key
 */
export const systemLogin = (encrypt:String)=>{
  return service.post('/system/login',{
    data:{
      encrypt
    }
  })
}