import { useEffect, useState, type FormEvent } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { equipamentoService } from '../../services/equipamentos'
import { tipoEquipamentoService } from '../../services/tiposEquipamento'
import type { EquipamentoRequest, TipoEquipamento } from '../../types'

export function EquipamentoForm() {
  const { id } = useParams()
  const isEdit = !!id
  const navigate = useNavigate()
  const [tipos, setTipos] = useState<TipoEquipamento[]>([])
  const [loading, setLoading] = useState(false)
  const [form, setForm] = useState<EquipamentoRequest>({
    especificacoes: '', numeroSerie: '', marca: '', modelo: '',
    dataAquisicao: '', tempoUso: 0, id_tipo_equipamento: 0, disponivel: true,
  })

  useEffect(() => {
    tipoEquipamentoService.list().then((res) => setTipos(res.data))
    if (isEdit) {
      equipamentoService.getById(Number(id)).then((res) => {
        const e = res.data
        setForm({
          especificacoes: e.especificacoes,
          numeroSerie: e.numeroSerie,
          marca: e.marca,
          modelo: e.modelo,
          dataAquisicao: e.dataAquisicao,
          tempoUso: e.tempoUso,
          id_tipo_equipamento: e.id_tipo_equipamento,
          disponivel: e.disponivel,
        })
      })
    }
  }, [id, isEdit])

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault()
    setLoading(true)
    try {
      if (isEdit) {
        await equipamentoService.update(Number(id), form)
      } else {
        await equipamentoService.create(form)
      }
      navigate('/equipamentos')
    } finally {
      setLoading(false)
    }
  }

  const set = (field: keyof EquipamentoRequest) => (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) =>
    setForm((prev) => ({ ...prev, [field]: e.target.value }))

  return (
    <div className="max-w-2xl mx-auto space-y-4">
      <h2 className="text-2xl font-bold text-slate-800">{isEdit ? 'Editar Equipamento' : 'Novo Equipamento'}</h2>
      <form onSubmit={handleSubmit} className="bg-white rounded-xl shadow-sm p-6 space-y-4">
        <div className="grid grid-cols-2 gap-4">
          <div className="col-span-2">
            <label className="block text-sm font-medium text-gray-700 mb-1">Especificações</label>
            <input value={form.especificacoes} onChange={set('especificacoes')} required className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-slate-500" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Nº Série</label>
            <input value={form.numeroSerie} onChange={set('numeroSerie')} required className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-slate-500" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Marca</label>
            <input value={form.marca} onChange={set('marca')} required className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-slate-500" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Modelo</label>
            <input value={form.modelo} onChange={set('modelo')} required className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-slate-500" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Data Aquisição</label>
            <input type="date" value={form.dataAquisicao} onChange={set('dataAquisicao')} required className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-slate-500" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Tempo de Uso (dias)</label>
            <input type="number" value={form.tempoUso} onChange={set('tempoUso')} required className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-slate-500" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Tipo de Equipamento</label>
            <select value={form.id_tipo_equipamento} onChange={set('id_tipo_equipamento')} required className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-slate-500">
              <option value={0}>Selecione...</option>
              {tipos.map((t) => <option key={t.id} value={t.id}>{t.nomeTipo}</option>)}
            </select>
          </div>
          <div className="flex items-center gap-2 pt-6">
            <input type="checkbox" id="disponivel" checked={form.disponivel} onChange={(e) => setForm((prev) => ({ ...prev, disponivel: e.target.checked }))} className="w-4 h-4" />
            <label htmlFor="disponivel" className="text-sm font-medium text-gray-700">Disponível</label>
          </div>
        </div>
        <div className="flex gap-3 pt-2">
          <button type="submit" disabled={loading} className="px-4 py-2 bg-slate-800 text-white rounded-lg hover:bg-slate-700 disabled:opacity-50">
            {loading ? 'Salvando...' : 'Salvar'}
          </button>
          <button type="button" onClick={() => navigate('/equipamentos')} className="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">
            Cancelar
          </button>
        </div>
      </form>
    </div>
  )
}
