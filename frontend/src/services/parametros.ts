import api from './api'
import type { ParametroSistemaRequest, ParametroSistemaResponse, PrevisaoFaltaResponse } from '../types'

export const parametroService = {
  list: () => api.get<ParametroSistemaResponse[]>('/parametros-sistema'),

  getById: (id: number) => api.get<ParametroSistemaResponse>(`/parametros-sistema/${id}`),

  create: (data: ParametroSistemaRequest) => api.post<ParametroSistemaResponse>('/parametros-sistema', data),

  update: (id: number, data: ParametroSistemaRequest) =>
    api.put<ParametroSistemaResponse>(`/parametros-sistema/${id}`, data),

  delete: (id: number) => api.delete(`/parametros-sistema/${id}`),

  relatorioPrevisaoFalta: () => api.get<PrevisaoFaltaResponse[]>('/parametros-sistema/relatorio-previsao-falta'),
}
