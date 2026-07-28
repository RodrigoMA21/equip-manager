import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { emprestimoService } from '../../services/emprestimos'
import { colaboradorService } from '../../services/colaboradores'
import { equipamentoService } from '../../services/equipamentos'
import type { EmprestimoResponse, ColaboradorResponse, EquipamentoResponse } from '../../types'

export function EmprestimosList() {
  const [emprestimos, setEmprestimos] = useState<EmprestimoResponse[]>([])
  const [colaboradores, setColaboradores] = useState<ColaboradorResponse[]>([])
  const [equipamentos, setEquipamentos] = useState<EquipamentoResponse[]>([])
  const [loading, setLoading] = useState(true)

  const load = () => {
    setLoading(true)
    Promise.all([
      emprestimoService.list(),
      colaboradorService.list(),
      equipamentoService.list(),
    ]).then(([emp, col, eq]) => {
      setEmprestimos(emp.data)
      setColaboradores(col.data)
      setEquipamentos(eq.data)
    }).finally(() => setLoading(false))
  }

  useEffect(load, [])

  const getColaboradorNome = (id: number) => colaboradores.find((c) => c.id === id)?.nome || `#${id}`
  const getEquipamentoInfo = (id: number) => {
    const eq = equipamentos.find((e) => e.id === id)
    return eq ? `${eq.marca} ${eq.modelo} (${eq.numeroSerie})` : `#${id}`
  }

  const handleDevolver = async (emprestimo: EmprestimoResponse) => {
    if (confirm('Registrar devolução deste equipamento?')) {
      await emprestimoService.devolver(emprestimo.chaveCompostaEquipamentoColaborador)
      load()
    }
  }

  if (loading) return <div className="text-gray-500">Carregando...</div>

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h2 className="text-2xl font-bold text-slate-800">Empréstimos</h2>
        <Link to="/emprestimos/novo" className="px-4 py-2 bg-slate-800 text-white rounded-lg hover:bg-slate-700 text-sm">
          + Novo Empréstimo
        </Link>
      </div>
      <div className="bg-white rounded-xl shadow-sm overflow-hidden">
        <table className="w-full text-sm">
          <thead className="bg-gray-50 text-gray-600">
            <tr>
              <th className="text-left px-4 py-3 font-medium">Equipamento</th>
              <th className="text-left px-4 py-3 font-medium">Colaborador</th>
              <th className="text-left px-4 py-3 font-medium">Data Entrega</th>
              <th className="text-left px-4 py-3 font-medium">Previsão</th>
              <th className="text-center px-4 py-3 font-medium">Status</th>
              <th className="text-right px-4 py-3 font-medium">Ações</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {emprestimos.map((emp, i) => {
              const devolvido = !!emp.dataDevolucao
              return (
                <tr key={i} className="hover:bg-gray-50">
                  <td className="px-4 py-3 text-slate-800">{getEquipamentoInfo(emp.chaveCompostaEquipamentoColaborador.idEquipamento)}</td>
                  <td className="px-4 py-3 text-gray-600">{getColaboradorNome(emp.chaveCompostaEquipamentoColaborador.idColaborador)}</td>
                  <td className="px-4 py-3 text-gray-600">{emp.dataEntrega}</td>
                  <td className="px-4 py-3 text-gray-600">{emp.previsaoEntrega}</td>
                  <td className="px-4 py-3 text-center">
                    <span className={`inline-block px-2 py-0.5 rounded-full text-xs font-medium ${devolvido ? 'bg-green-100 text-green-700' : 'bg-amber-100 text-amber-700'}`}>
                      {devolvido ? 'Devolvido' : 'Em andamento'}
                    </span>
                  </td>
                  <td className="px-4 py-3 text-right">
                    {!devolvido && (
                      <button onClick={() => handleDevolver(emp)} className="text-green-600 hover:text-green-800 text-sm">
                        Registrar Devolução
                      </button>
                    )}
                  </td>
                </tr>
              )
            })}
            {emprestimos.length === 0 && (
              <tr><td colSpan={6} className="px-4 py-8 text-center text-gray-400">Nenhum empréstimo encontrado</td></tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  )
}
