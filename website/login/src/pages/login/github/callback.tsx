import React, { useEffect,useState } from 'react';
import { Form, Input, Button, Checkbox, Avatar, Spin, message } from 'antd';
import {GithubOutlined} from '@ant-design/icons'
import { useLocation } from 'umi';
import { getGitHubCallback } from '@/http/modules/authService';


export default () => {
  const location = useLocation()
  const code = location.query.code
  const loginError = ()=>{
    message.error("登录失败...")
    setTimeout(() => {
      window.location.href = '/login'
    }, 1000);
  }
  useEffect(() => {
    if(code){
      console.info(code);
      getGitHubCallback('/gitHub/callback?code='+code).then(result=>{
        const token = result
        if(token){
          window.localStorage.setItem(
            'Authorization',
            token
          )
          console.info('登录成功....')
          window.location.href="/home"
        }else{
          loginError()
        }
      })
    }else{
      loginError()
    }
  }, [])
  return (
    <div>
      <Spin/>
    </div>
  );
};
