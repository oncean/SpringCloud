const config = (auth?:boolean) => {
  const token = localStorage.getItem('Authorization')
  return {
    prefix: '/api',
    timeout: 30000,
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      Authorization: auth?`bearer ${token}`:''
    }
  }
}

export const defaultHttpConfig = config(true)
export const noAuthHttpConfig = config()