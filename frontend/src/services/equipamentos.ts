import api from './api'
import type { EquipamentoRequest, EquipamentoResponse } from '../types'

export const equipamentoService = {
  list: () => api.get<EquipamentoResponse[]>('/equipamentos'),

  getById: (id: number) => api.get<EquipamentoResponse>(`/equipamentos/${id}`),

  create: (data: EquipamentoRequest) => api.post<EquipamentoResponse>('/equipamentos', data),

  update: (id: number, data: EquipamentoRequest) => api.put<EquipamentoResponse>(`/equipamentos/${id}`, data),

  delete: (id: number) => api.delete(`/equipamentos/${id}`),
}
