import api from './api'
import type { TipoEquipamento, TipoEquipamentoRequest } from '../types'

export const tipoEquipamentoService = {
  list: () => api.get<TipoEquipamento[]>('/tipos-equipamento'),

  getById: (id: number) => api.get<TipoEquipamento>(`/tipos-equipamento/${id}`),

  create: (data: TipoEquipamentoRequest) => api.post<TipoEquipamento>('/tipos-equipamento', data),

  update: (id: number, data: TipoEquipamento) => api.put<TipoEquipamento>(`/tipos-equipamento/${id}`, data),

  delete: (id: number) => api.delete(`/tipos-equipamento/${id}`),
}
