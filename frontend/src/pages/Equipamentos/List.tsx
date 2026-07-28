import { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { equipamentoService } from '../../services/equipamentos'
import type { EquipamentoResponse } from '../../types'

export function EquipamentosList() {
  const [equipamentos, setEquipamentos] = useState<EquipamentoResponse[]>([])
  const [loading, setLoading] = useState(true)
  const navigate = useNavigate()

  const load = () => {
    setLoading(true)
    equipamentoService.list().then((res) => setEquipamentos(res.data)).finally(() => setLoading(false))
  }

  useEffect(load, [])

  const handleDelete = async (id: number) => {
    if (confirm('Excluir este equipamento?')) {
      await equipamentoService.delete(id)
      load()
    }
  }

  if (loading) return <div className="text-gray-500">Carregando...</div>

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h2 className="text-2xl font-bold text-slate-800">Equipamentos</h2>
        <Link to="/equipamentos/novo" className="px-4 py-2 bg-slate-800 text-white rounded-lg hover:bg-slate-700 text-sm">
          + Novo Equipamento
        </Link>
      </div>
      <div className="bg-white rounded-xl shadow-sm overflow-hidden">
        <table className="w-full text-sm">
          <thead className="bg-gray-50 text-gray-600">
            <tr>
              <th className="text-left px-4 py-3 font-medium">Nº Série</th>
              <th className="text-left px-4 py-3 font-medium">Marca</th>
              <th className="text-left px-4 py-3 font-medium">Modelo</th>
              <th className="text-left px-4 py-3 font-medium">Especificações</th>
              <th className="text-center px-4 py-3 font-medium">Disponível</th>
              <th className="text-right px-4 py-3 font-medium">Ações</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {equipamentos.map((eq) => (
              <tr key={eq.id} className="hover:bg-gray-50">
                <td className="px-4 py-3 text-slate-800 font-mono">{eq.numeroSerie}</td>
                <td className="px-4 py-3 text-gray-600">{eq.marca}</td>
                <td className="px-4 py-3 text-gray-600">{eq.modelo}</td>
                <td className="px-4 py-3 text-gray-600 max-w-xs truncate">{eq.especificacoes}</td>
                <td className="px-4 py-3 text-center">
                  <span className={`inline-block px-2 py-0.5 rounded-full text-xs font-medium ${eq.disponivel ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
                    {eq.disponivel ? 'Sim' : 'Não'}
                  </span>
                </td>
                <td className="px-4 py-3 text-right space-x-2">
                  <button onClick={() => navigate(`/equipamentos/${eq.id}`)} className="text-blue-600 hover:text-blue-800">Editar</button>
                  <button onClick={() => handleDelete(eq.id)} className="text-red-600 hover:text-red-800">Excluir</button>
                </td>
              </tr>
            ))}
            {equipamentos.length === 0 && (
              <tr><td colSpan={6} className="px-4 py-8 text-center text-gray-400">Nenhum equipamento encontrado</td></tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  )
}
