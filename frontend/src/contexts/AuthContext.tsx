import { useState, useCallback, type ReactNode } from 'react'
import { authService } from '../services/auth'
import type { LoginRequest } from '../types'
import { AuthContext } from './createContext'

export function AuthProvider({ children }: { children: ReactNode }) {
  const [isAuthenticated, setIsAuthenticated] = useState(() => !!localStorage.getItem('accessToken'))

  const login = useCallback(async (data: LoginRequest) => {
    const response = await authService.login(data)
    localStorage.setItem('accessToken', response.data.accessToken)
    localStorage.setItem('refreshToken', response.data.refreshToken)
    setIsAuthenticated(true)
  }, [])

  const register = useCallback(async (email: string, senha: string) => {
    await authService.register({ email, senha })
  }, [])

  const logout = useCallback(() => {
    localStorage.clear()
    setIsAuthenticated(false)
  }, [])

  return (
    <AuthContext.Provider value={{ isAuthenticated, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  )
}
