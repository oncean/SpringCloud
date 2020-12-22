import React, { useEffect, useState } from 'react';
import styles from './index.less';
import { Form, Input, Button, Checkbox, Spin, Space, message } from 'antd';
import { GithubOutlined, WeiboCircleOutlined } from '@ant-design/icons';
import useInitial from '@/http/useInitial';
import { getGitHubClientId, getRSAPublicKey, systemLogin } from '@/http/modules/authService';
import JsEncrypt from '@/utils/jsencryptKey';
import useRequest from '@/http/useRequest';

const layout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 16 },
};
const tailLayout = {
  wrapperCol: { offset: 8, span: 16 },
};

export default () => {
  const { loading: gitHubLoading, result: client_id } = useInitial(
    getGitHubClientId,
  );
  const { loading: rsaLoading, result: rsaPublicKey } = useInitial(
    getRSAPublicKey,
  );

  const { loading: submitLoading, run: login } = useRequest(systemLogin);
  //提交表单
  const onSubmit = async (values: any) => {
    if (!rsaPublicKey) {
      message.error('获取加密密钥失败。。。');
      return;
    }
    let encryptString = JsEncrypt(JSON.stringify(values), rsaPublicKey);
    const result = await login(encryptString);
    message.success("登录成功")
    localStorage.setItem("wangsheng-token",JSON.stringify(result))
    window.location.href="http://wangsheng.com/home"
  };

  //表单校验失败
  const onFinishFailed = (errorInfo: any) => {
    console.log('Failed:', errorInfo);
  };
  return (
    <div className={styles.holl}>
      <div className={styles.loginView}>
        <div
          style={{
            fontSize: 20,
            fontWeight: 900,
            textAlign: 'center',
            margin: 30,
          }}
        >
          登录
        </div>
        <Form
          {...layout}
          name="basic"
          initialValues={{ remember: true }}
          onFinish={onSubmit}
          onFinishFailed={onFinishFailed}
        >
          <Form.Item
            label="Username"
            name="username"
            rules={[{ required: true, message: 'Please input your username!' }]}
          >
            <Input />
          </Form.Item>

          <Form.Item
            label="Password"
            name="password"
            rules={[{ required: true, message: 'Please input your password!' }]}
          >
            <Input.Password />
          </Form.Item>

          <Form.Item {...tailLayout} name="remember" valuePropName="checked">
            <Checkbox>Remember me</Checkbox>
          </Form.Item>

          <Form.Item {...tailLayout}>
            <Spin spinning={submitLoading}>
              <Button type="primary" htmlType="submit">
                Submit
              </Button>
            </Spin>
          </Form.Item>
        </Form>

        <Space>
          <Spin spinning={gitHubLoading}>
            <GithubOutlined
              style={{ fontSize: 40 }}
              onClick={() => {
                if (!client_id) {
                  message.error('获取github 授权码失败');
                  return;
                }
                window.location.href = `https://github.com/login/oauth/authorize?client_id=${client_id}`;
              }}
            />
          </Spin>
          <Spin spinning={false}>
            <WeiboCircleOutlined style={{ fontSize: 40 }} />
          </Spin>
        </Space>
      </div>
    </div>
  );
};
