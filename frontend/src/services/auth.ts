import api from './api'
import type { LoginRequest, TokenResponse, UsuarioCriacao, UsuarioResponse } from '../types'

export const authService = {
  login: (data: LoginRequest) => api.post<TokenResponse>('/auth/login', data),

  register: (data: UsuarioCriacao) => api.post<string>('/usuarios', data),

  confirmEmail: (token: string) => api.get<string>('/auth/confirmar', { params: { token } }),

  forgotPassword: (email: string) => api.post<string>('/auth/esqueci minha senha', null, { params: { email } }),

  resetPassword: (token: string, novaSenha: string) =>
    api.post<string>('/auth/redefinir senha', { token, novaSenha }),

  changePassword: (email: string, senhaAtual: string, novaSenha: string) =>
    api.post<string>('/auth/alterar senha', { email, senhaAtual, novaSenha }),

  refreshToken: (refreshToken: string) => api.post<TokenResponse>('/auth/refresh-token', { refreshToken }),

  getUsers: () => api.get<UsuarioResponse[]>('/usuarios'),

  getUser: (id: number) => api.get<UsuarioResponse>(`/usuarios/${id}`),

  updateUser: (id: number, data: { email?: string; senha?: string }) =>
    api.put<UsuarioResponse>(`/usuarios/${id}`, data),

  deleteUser: (id: number) => api.delete(`/usuarios/${id}`),
}
