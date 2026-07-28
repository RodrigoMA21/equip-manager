import { useEffect, useState } from 'react'
import { colaboradorService } from '../services/colaboradores'
import { equipamentoService } from '../services/equipamentos'
import { emprestimoService } from '../services/emprestimos'
import { parametroService } from '../services/parametros'
import type { ColaboradorResponse, EquipamentoResponse, EmprestimoResponse, PrevisaoFaltaResponse } from '../types'

export function Dashboard() {
  const [colaboradores, setColaboradores] = useState<ColaboradorResponse[]>([])
  const [equipamentos, setEquipamentos] = useState<EquipamentoResponse[]>([])
  const [emprestimos, setEmprestimos] = useState<EmprestimoResponse[]>([])
  const [previsoes, setPrevisoes] = useState<PrevisaoFaltaResponse[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    Promise.all([
      colaboradorService.list(),
      equipamentoService.list(),
      emprestimoService.list(),
      parametroService.relatorioPrevisaoFalta(),
    ]).then(([col, eq, emp, prev]) => {
      setColaboradores(col.data)
      setEquipamentos(eq.data)
      setEmprestimos(emp.data)
      setPrevisoes(prev.data)
    }).finally(() => setLoading(false))
  }, [])

  if (loading) {
    return <div className="flex items-center justify-center h-64 text-gray-500">Carregando...</div>
  }

  const ativos = colaboradores.filter((c) => !c.data_contratacao_recisao).length
  const disponiveis = equipamentos.filter((e) => e.disponivel).length
  const emprestados = emprestimos.filter((e) => !e.dataDevolucao).length
  const emRisco = previsoes.filter((p) => p.emRiscoDeFalta).length

  const cards = [
    { label: 'Colaboradores Ativos', value: ativos, color: 'bg-blue-500' },
    { label: 'Equipamentos Disponíveis', value: disponiveis, color: 'bg-green-500' },
    { label: 'Equipamentos Emprestados', value: emprestados, color: 'bg-amber-500' },
    { label: 'Em Risco de Falta', value: emRisco, color: 'bg-red-500' },
  ]

  return (
    <div className="space-y-6">
      <h2 className="text-2xl font-bold text-slate-800">Dashboard</h2>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {cards.map((card) => (
          <div key={card.label} className="bg-white rounded-xl shadow-sm p-6">
            <div className={`w-3 h-3 rounded-full ${card.color} mb-3`} />
            <p className="text-3xl font-bold text-slate-800">{card.value}</p>
            <p className="text-sm text-gray-500 mt-1">{card.label}</p>
          </div>
        ))}
      </div>

      {previsoes.filter((p) => p.emRiscoDeFalta).length > 0 && (
        <div className="bg-white rounded-xl shadow-sm p-6">
          <h3 className="text-lg font-semibold text-slate-800 mb-4">Alertas de Estoque</h3>
          <div className="space-y-3">
            {previsoes.filter((p) => p.emRiscoDeFalta).map((p) => (
              <div key={p.idTipoEquipamento} className="p-3 bg-red-50 border border-red-200 rounded-lg">
                <p className="text-sm font-medium text-red-800">{p.nomeTipoEquipamento}</p>
                <p className="text-xs text-red-600 mt-1">{p.mensagemAlerta}</p>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  )
}
