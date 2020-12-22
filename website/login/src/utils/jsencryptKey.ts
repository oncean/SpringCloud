
import JsEncrypt from 'jsencrypt'

export default function(str,key) {
  let encrypt = new JsEncrypt()
  encrypt.setPublicKey(key)
  return encrypt.encrypt(str)
}
