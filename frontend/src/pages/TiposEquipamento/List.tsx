import { useEffect, useState } from 'react'
import { tipoEquipamentoService } from '../../services/tiposEquipamento'
import type { TipoEquipamento } from '../../types'

export function TiposEquipamentoList() {
  const [tipos, setTipos] = useState<TipoEquipamento[]>([])
  const [form, setForm] = useState('')
  const [editId, setEditId] = useState<number | null>(null)
  const [loading, setLoading] = useState(true)

  const load = () => {
    setLoading(true)
    tipoEquipamentoService.list().then((res) => setTipos(res.data)).finally(() => setLoading(false))
  }

  useEffect(load, [])

  const handleSave = async () => {
    if (!form.trim()) return
    if (editId) {
      await tipoEquipamentoService.update(editId, { id: editId, nomeTipo: form })
    } else {
      await tipoEquipamentoService.create({ nomeTipo: form })
    }
    setForm('')
    setEditId(null)
    load()
  }

  const handleEdit = (t: TipoEquipamento) => {
    setForm(t.nomeTipo)
    setEditId(t.id)
  }

  const handleDelete = async (id: number) => {
    if (confirm('Excluir este tipo?')) {
      await tipoEquipamentoService.delete(id)
      load()
    }
  }

  if (loading) return <div className="text-gray-500">Carregando...</div>

  return (
    <div className="max-w-lg mx-auto space-y-4">
      <h2 className="text-2xl font-bold text-slate-800">Tipos de Equipamento</h2>
      <div className="bg-white rounded-xl shadow-sm p-4 flex gap-2">
        <input value={form} onChange={(e) => setForm(e.target.value)} placeholder="Nome do tipo" className="flex-1 px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-slate-500 text-sm" />
        <button onClick={handleSave} className="px-4 py-2 bg-slate-800 text-white rounded-lg hover:bg-slate-700 text-sm whitespace-nowrap">
          {editId ? 'Atualizar' : 'Adicionar'}
        </button>
      </div>
      <div className="bg-white rounded-xl shadow-sm overflow-hidden">
        <table className="w-full text-sm">
          <thead className="bg-gray-50 text-gray-600">
            <tr>
              <th className="text-left px-4 py-3 font-medium">Nome</th>
              <th className="text-right px-4 py-3 font-medium">Ações</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {tipos.map((t) => (
              <tr key={t.id} className="hover:bg-gray-50">
                <td className="px-4 py-3 text-slate-800">{t.nomeTipo}</td>
                <td className="px-4 py-3 text-right space-x-2">
                  <button onClick={() => handleEdit(t)} className="text-blue-600 hover:text-blue-800">Editar</button>
                  <button onClick={() => handleDelete(t.id)} className="text-red-600 hover:text-red-800">Excluir</button>
                </td>
              </tr>
            ))}
            {tipos.length === 0 && (
              <tr><td colSpan={2} className="px-4 py-8 text-center text-gray-400">Nenhum tipo cadastrado</td></tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  )
}
