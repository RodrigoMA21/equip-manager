import type { LoginRequest } from '../types'

export interface AuthContextType {
  isAuthenticated: boolean
  login: (data: LoginRequest) => Promise<void>
  register: (email: string, senha: string) => Promise<void>
  logout: () => void
}
