import { message } from 'antd';
import { useState, useEffect } from 'react';
import {Result} from './useRequest'

export type APIFunc<Result, P> = (params?: P) => Promise<Result>

function useInitial<T, P>(api: APIFunc<T, P>, _params?: P) {
  const [store, setStore] = useState({
    params: _params,
    loading: true,
    result: {},
    errMsg: ''
  })

  const { params, loading ,result,errMsg} = store;

  useEffect(() => {
    if (!loading) { return; }
    getData(params)
  }, [loading])

  function getData(params?: P) {
    console.info("query statr")
    api(params).then((result:Result<T>) => {
      setStore({
        ...store,
        errMsg: '',
        loading: false,
        result:result.data
      })
    })
      .catch(e => {
        message.error(e.message)
        setStore({
          ...store,
          errMsg: e.message,
          loading: false
        })
      })
  }
  
  return {
    loading,
    result,
    errMsg
  }
}


export default useInitial