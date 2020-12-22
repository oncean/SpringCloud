import { message } from 'antd'
import {useState} from 'react'

export interface Result {
  code:string;
  msg:string;
  data:any
}
export type APIFunc<Result> = (...params: any[])=>Promise<Result>

export default function useRequest<T>(api:APIFunc<Result>){
  const [loading, setloading] = useState(false)

  async function run(...params:any[]){
    let result:Result|null = null
    try {
      setloading(true)
      result = await api(...params)
    } catch (error) {
      message.error(error.message)
      setloading(false)
      throw error;
    }
    setloading(false)
    return result.data
  }

  return {
    loading,
    run
  }
}