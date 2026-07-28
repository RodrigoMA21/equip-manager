import { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { colaboradorService } from '../../services/colaboradores'
import type { ColaboradorResponse } from '../../types'

export function ColaboradoresList() {
  const [colaboradores, setColaboradores] = useState<ColaboradorResponse[]>([])
  const [loading, setLoading] = useState(true)
  const navigate = useNavigate()

  const load = () => {
    setLoading(true)
    colaboradorService.list().then((res) => setColaboradores(res.data)).finally(() => setLoading(false))
  }

  useEffect(load, [])

  const handleDelete = async (id: number) => {
    if (confirm('Excluir este colaborador?')) {
      await colaboradorService.delete(id)
      load()
    }
  }

  if (loading) return <div className="text-gray-500">Carregando...</div>

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h2 className="text-2xl font-bold text-slate-800">Colaboradores</h2>
        <Link to="/colaboradores/novo" className="px-4 py-2 bg-slate-800 text-white rounded-lg hover:bg-slate-700 text-sm">
          + Novo Colaborador
        </Link>
      </div>
      <div className="bg-white rounded-xl shadow-sm overflow-hidden">
        <table className="w-full text-sm">
          <thead className="bg-gray-50 text-gray-600">
            <tr>
              <th className="text-left px-4 py-3 font-medium">Nome</th>
              <th className="text-left px-4 py-3 font-medium">Email</th>
              <th className="text-left px-4 py-3 font-medium">CPF</th>
              <th className="text-left px-4 py-3 font-medium">Cidade</th>
              <th className="text-right px-4 py-3 font-medium">Ações</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {colaboradores.map((col) => (
              <tr key={col.id} className="hover:bg-gray-50">
                <td className="px-4 py-3 text-slate-800">{col.nome}</td>
                <td className="px-4 py-3 text-gray-600">{col.email}</td>
                <td className="px-4 py-3 text-gray-600">{col.endereco?.cep || '-'}</td>
                <td className="px-4 py-3 text-gray-600">{col.endereco?.localidade || '-'}/{col.endereco?.uf || '-'}</td>
                <td className="px-4 py-3 text-right space-x-2">
                  <button onClick={() => navigate(`/colaboradores/${col.id}`)} className="text-blue-600 hover:text-blue-800">Editar</button>
                  <button onClick={() => handleDelete(col.id)} className="text-red-600 hover:text-red-800">Excluir</button>
                </td>
              </tr>
            ))}
            {colaboradores.length === 0 && (
              <tr><td colSpan={5} className="px-4 py-8 text-center text-gray-400">Nenhum colaborador encontrado</td></tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  )
}
