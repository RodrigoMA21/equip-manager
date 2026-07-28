import api from './api'
import type { ColaboradorRequest, ColaboradorResponse, ColaboradorUpdate } from '../types'

export const colaboradorService = {
  list: () => api.get<ColaboradorResponse[]>('/colaboradores'),

  listInativos: () => api.get<ColaboradorResponse[]>('/colaboradores/inativos'),

  getById: (id: number) => api.get<ColaboradorResponse>(`/colaboradores/${id}`),

  create: (data: ColaboradorRequest) => api.post<ColaboradorResponse>('/colaboradores', data),

  update: (id: number, data: ColaboradorUpdate) => api.patch(`/colaboradores/${id}`, data),

  delete: (id: number) => api.delete(`/colaboradores/${id}`),

  reativar: (id: number) => api.patch(`/colaboradores/${id}/reativar`),
}
