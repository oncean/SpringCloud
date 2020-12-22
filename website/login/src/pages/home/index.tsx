import React, { useEffect,useState } from 'react';
import styles from './index.less';
import { Form, Input, Button, Checkbox, Avatar } from 'antd';
import  gitHubService from '@/http/modules/githubService';


export default () => {
  const [info, setinfo] = useState({})


  const getGitInfo = ()=>{
    gitHubService('/user').then(result=>{
      setinfo(result)
    })
  }

  useEffect(() => {
    getGitInfo()
  }, [])
  return (
    <div className={styles.holl}>
      <div className={styles.loginView}>


        111
        <Avatar src={info.avatar_url}/>
      </div>
    </div>
  );
};
