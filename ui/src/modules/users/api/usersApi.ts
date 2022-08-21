const baseUrl = `${process.env.REACT_APP_API_URL}/user`

export const usersApi = {

  async startLogin(email: string) {
    const payload = {
      email: email,
    }

    const response = await fetch(`${baseUrl}/start-login`, {
      method: 'POST',
      headers: {
        'Accept': '*/*',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload),
    })

    if (response.status === 400) {
      throw new Error(`Invalid email address`)
    }
    
    if (!response.ok) {
      throw new Error(`Failed to start login (HTTP status = ${response.status})`)
    }
  },

  async completeLogin(email: string, verificationCode: string) {
    const payload = {
      email: email,
      verificationCode: verificationCode,
    }

    const response = await fetch(`${baseUrl}/complete-login`, {
      method: 'POST',
      headers: {
        'Accept': '*/*',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload),
    })

    if (response.status === 400) {
      throw new Error(`Invalid verification code`)
    }

    if (!response.ok) {
      throw new Error(`Failed to complete login (HTTP status = ${response.status})`)
    }
  },

  async abortLogin(email: string) {
    const payload = {
      email: email,
    }

    const response = await fetch(`${baseUrl}/abort-login`, {
      method: 'POST',
      headers: {
        'Accept': '*/*',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload),
    })

    if (!response.ok) {
      throw new Error(`Failed to abort login (HTTP status = ${response.status})`)
    }
  },
}
