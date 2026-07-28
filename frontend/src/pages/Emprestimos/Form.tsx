import { useEffect, useState, type FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import { emprestimoService } from '../../services/emprestimos'
import { colaboradorService } from '../../services/colaboradores'
import { equipamentoService } from '../../services/equipamentos'
import type { ColaboradorResponse, EquipamentoResponse } from '../../types'

export function EmprestimoForm() {
  const navigate = useNavigate()
  const [colaboradores, setColaboradores] = useState<ColaboradorResponse[]>([])
  const [equipamentos, setEquipamentos] = useState<EquipamentoResponse[]>([])
  const [loading, setLoading] = useState(false)
  const [idColaborador, setIdColaborador] = useState('')
  const [idEquipamento, setIdEquipamento] = useState('')
  const [dataEntrega, setDataEntrega] = useState(new Date().toISOString().split('T')[0])

  useEffect(() => {
    Promise.all([
      colaboradorService.list(),
      equipamentoService.list(),
    ]).then(([col, eq]) => {
      setColaboradores(col.data)
      setEquipamentos(eq.data.filter((e) => e.disponivel))
    })
  }, [])

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault()
    if (!idColaborador || !idEquipamento) return
    setLoading(true)
    try {
      await emprestimoService.create({
        chaveCompostaEquipamentoColaborador: {
          idEquipamento: Number(idEquipamento),
          idColaborador: Number(idColaborador),
        },
        dataEntrega,
      })
      navigate('/emprestimos')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="max-w-2xl mx-auto space-y-4">
      <h2 className="text-2xl font-bold text-slate-800">Novo Empréstimo</h2>
      <form onSubmit={handleSubmit} className="bg-white rounded-xl shadow-sm p-6 space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Colaborador</label>
          <select value={idColaborador} onChange={(e) => setIdColaborador(e.target.value)} required className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-slate-500">
            <option value="">Selecione...</option>
            {colaboradores.map((c) => (
              <option key={c.id} value={c.id}>{c.nome} - {c.email}</option>
            ))}
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Equipamento</label>
          <select value={idEquipamento} onChange={(e) => setIdEquipamento(e.target.value)} required className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-slate-500">
            <option value="">Selecione...</option>
            {equipamentos.map((e) => (
              <option key={e.id} value={e.id}>{e.marca} {e.modelo} - {e.numeroSerie}</option>
            ))}
          </select>
          {equipamentos.length === 0 && (
            <p className="text-xs text-amber-600 mt-1">Nenhum equipamento disponível no momento.</p>
          )}
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Data de Entrega</label>
          <input type="date" value={dataEntrega} onChange={(e) => setDataEntrega(e.target.value)} required className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-slate-500" />
        </div>
        <div className="flex gap-3 pt-2">
          <button type="submit" disabled={loading} className="px-4 py-2 bg-slate-800 text-white rounded-lg hover:bg-slate-700 disabled:opacity-50">
            {loading ? 'Salvando...' : 'Registrar Empréstimo'}
          </button>
          <button type="button" onClick={() => navigate('/emprestimos')} className="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">
            Cancelar
          </button>
        </div>
      </form>
    </div>
  )
}
