import { useEffect, useState, type FormEvent } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { colaboradorService } from '../../services/colaboradores'
import type { ColaboradorRequest, ViaCEPResponse } from '../../types'

export function ColaboradorForm() {
  const { id } = useParams()
  const isEdit = !!id
  const navigate = useNavigate()
  const [loading, setLoading] = useState(false)
  const [buscandoCep, setBuscandoCep] = useState(false)
  const [form, setForm] = useState<ColaboradorRequest>({
    cpf: '', nome: '', email: '', cep: '', data_aniversario: '',
    data_contratacao_inicio: '', data_contratacao_recisao: '',
    especificacao_equipamento: '', numeroCasa: 0,
  })
  const [endereco, setEndereco] = useState<ViaCEPResponse | null>(null)

  useEffect(() => {
    if (isEdit) {
      colaboradorService.getById(Number(id)).then((res) => {
        const c = res.data
        setForm({
          cpf: '',
          nome: c.nome,
          email: c.email,
          cep: c.cep,
          data_aniversario: c.data_aniversario,
          data_contratacao_inicio: c.data_contratacao_inicio,
          data_contratacao_recisao: c.data_contratacao_recisao || '',
          especificacao_equipamento: c.especificacao_equipamento,
          numeroCasa: c.endereco?.numero || 0,
        })
        if (c.endereco) {
          setEndereco({
            cep: c.endereco.cep,
            logradouro: c.endereco.logradouro,
            complemento: c.endereco.complemento,
            bairro: c.endereco.bairro,
            localidade: c.endereco.localidade,
            uf: c.endereco.uf,
            estado: c.endereco.estado,
            regiao: c.endereco.regiao,
            ibge: c.endereco.ibge,
            gia: c.endereco.gia,
            ddd: c.endereco.ddd,
            siafi: c.endereco.siafi,
          })
        }
      })
    }
  }, [id, isEdit])

  const buscarCep = async (cep: string) => {
    const cepLimpo = cep.replace(/\D/g, '')
    if (cepLimpo.length !== 8) return
    setBuscandoCep(true)
    try {
      const res = await fetch(`https://viacep.com.br/ws/${cepLimpo}/json/`)
      const data: ViaCEPResponse = await res.json()
      if (!data.erro) setEndereco(data)
    } catch {
    } finally {
      setBuscandoCep(false)
    }
  }

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault()
    setLoading(true)
    try {
      if (isEdit) {
        await colaboradorService.update(Number(id), {
          nome: form.nome,
          email: form.email,
          cep: form.cep,
          equipamentoEspecificacao: form.especificacao_equipamento,
        })
      } else {
        await colaboradorService.create(form)
      }
      navigate('/colaboradores')
    } finally {
      setLoading(false)
    }
  }

  const set = (field: keyof ColaboradorRequest) => (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value
    setForm((prev) => ({ ...prev, [field]: value }))
    if (field === 'cep') buscarCep(value)
  }

  return (
    <div className="max-w-2xl mx-auto space-y-4">
      <h2 className="text-2xl font-bold text-slate-800">{isEdit ? 'Editar Colaborador' : 'Novo Colaborador'}</h2>
      <form onSubmit={handleSubmit} className="bg-white rounded-xl shadow-sm p-6 space-y-4">
        <div className="grid grid-cols-2 gap-4">
          <div className="col-span-2">
            <label className="block text-sm font-medium text-gray-700 mb-1">Nome</label>
            <input value={form.nome} onChange={set('nome')} required className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-slate-500" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
            <input type="email" value={form.email} onChange={set('email')} required className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-slate-500" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">CPF</label>
            <input value={form.cpf} onChange={set('cpf')} required={!isEdit} disabled={isEdit} className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-slate-500 disabled:bg-gray-100" />
          </div>
          <div className="col-span-2 border-t pt-4">
            <h3 className="text-sm font-semibold text-slate-700 mb-3">Endereço</h3>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">CEP</label>
            <input value={form.cep} onChange={set('cep')} required placeholder="00000-000" className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-slate-500" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Nº Casa</label>
            <input type="number" value={form.numeroCasa} onChange={set('numeroCasa')} required className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-slate-500" />
          </div>
          {buscandoCep && <div className="col-span-2 text-sm text-gray-500">Buscando endereço...</div>}
          {endereco && !buscandoCep && (
            <>
              <div className="col-span-2">
                <label className="block text-sm font-medium text-gray-700 mb-1">Logradouro</label>
                <input value={endereco.logradouro || ''} disabled className="w-full px-3 py-2 border border-gray-200 rounded-lg bg-gray-50 text-gray-600 text-sm" />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Bairro</label>
                <input value={endereco.bairro || ''} disabled className="w-full px-3 py-2 border border-gray-200 rounded-lg bg-gray-50 text-gray-600 text-sm" />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Complemento</label>
                <input value={endereco.complemento || ''} disabled className="w-full px-3 py-2 border border-gray-200 rounded-lg bg-gray-50 text-gray-600 text-sm" />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Cidade</label>
                <input value={endereco.localidade || ''} disabled className="w-full px-3 py-2 border border-gray-200 rounded-lg bg-gray-50 text-gray-600 text-sm" />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">UF</label>
                <input value={endereco.uf || ''} disabled className="w-full px-3 py-2 border border-gray-200 rounded-lg bg-gray-50 text-gray-600 text-sm" />
              </div>
            </>
          )}
          <div className="col-span-2 border-t pt-4">
            <h3 className="text-sm font-semibold text-slate-700 mb-3">Dados Pessoais</h3>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Data de Nascimento</label>
            <input type="date" value={form.data_aniversario} onChange={set('data_aniversario')} required className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-slate-500" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Data Contratação</label>
            <input type="date" value={form.data_contratacao_inicio} onChange={set('data_contratacao_inicio')} required className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-slate-500" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Data de Saída</label>
            <input type="date" value={form.data_contratacao_recisao} onChange={set('data_contratacao_recisao')} className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-slate-500" />
          </div>
          <div className="col-span-2">
            <label className="block text-sm font-medium text-gray-700 mb-1">Especificação de Equipamentos</label>
            <input value={form.especificacao_equipamento} onChange={set('especificacao_equipamento')} required className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-slate-500" />
          </div>
        </div>
        <div className="flex gap-3 pt-2">
          <button type="submit" disabled={loading} className="px-4 py-2 bg-slate-800 text-white rounded-lg hover:bg-slate-700 disabled:opacity-50">
            {loading ? 'Salvando...' : 'Salvar'}
          </button>
          <button type="button" onClick={() => navigate('/colaboradores')} className="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">
            Cancelar
          </button>
        </div>
      </form>
    </div>
  )
}
