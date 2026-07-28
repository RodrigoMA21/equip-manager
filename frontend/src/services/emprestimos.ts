import api from './api'
import type { EmprestimoRequest, EmprestimoResponse, IdEquipamentoToColaborador } from '../types'

export const emprestimoService = {
  list: () => api.get<EmprestimoResponse[]>('/emprestimos'),

  getById: (id: IdEquipamentoToColaborador) => api.get<EmprestimoResponse>('/emprestimos', { params: id }),

  create: (data: EmprestimoRequest) => api.post<EmprestimoResponse>('/emprestimos', data),

  devolver: (id: IdEquipamentoToColaborador) =>
    api.put(`/emprestimos/${id.idEquipamento}-${id.idColaborador}/devolver`),

  delete: (id: IdEquipamentoToColaborador) =>
    api.delete(`/emprestimos/${id.idEquipamento}-${id.idColaborador}`),
}
