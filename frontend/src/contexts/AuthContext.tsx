import { createContext, useContext, useState, useCallback, type ReactNode } from 'react'
import { authService } from '../services/auth'
import type { LoginRequest } from '../types'

interface AuthContextType {
  isAuthenticated: boolean
  login: (data: LoginRequest) => Promise<void>
  register: (email: string, senha: string) => Promise<void>
  logout: () => void
}

const AuthContext = createContext<AuthContextType | null>(null)

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

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) throw new Error('useAuth must be used within AuthProvider')
  return context
}
