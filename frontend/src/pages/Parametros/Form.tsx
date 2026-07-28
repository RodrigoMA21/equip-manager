import { useEffect, useState, type FormEvent } from 'react'
import { parametroService } from '../../services/parametros'
import type { ParametroSistemaRequest, PrevisaoFaltaResponse } from '../../types'

export function ParametrosForm() {
  const [parametro, setParametro] = useState<ParametroSistemaRequest>({
    tempoMedioReposicao: 0, tempoMedioConsumoEstoque: 0, tempoMedioEnvio: 0,
    taxaMediaEquipamentosDefeituosos: 0, estoqueMinimoSeguranca: 0,
  })
  const [previsoes, setPrevisoes] = useState<PrevisaoFaltaResponse[]>([])
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    parametroService.list().then((res) => {
      if (res.data.length > 0) {
        const p = res.data[0]
        setParametro({
          tempoMedioReposicao: p.tempoMedioReposicao,
          tempoMedioConsumoEstoque: p.tempoMedioConsumoEstoque,
          tempoMedioEnvio: p.tempoMedioEnvio,
          taxaMediaEquipamentosDefeituosos: p.taxaMediaEquipamentosDefeituosos,
          estoqueMinimoSeguranca: p.estoqueMinimoSeguranca,
        })
      }
    })
    loadPrevisoes()
  }, [])

  const loadPrevisoes = () => {
    parametroService.relatorioPrevisaoFalta().then((res) => setPrevisoes(res.data))
  }

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault()
    setLoading(true)
    try {
      const res = await parametroService.list()
      if (res.data.length > 0) {
        await parametroService.update(res.data[0].idParametro, parametro)
      } else {
        await parametroService.create(parametro)
      }
      loadPrevisoes()
    } finally {
      setLoading(false)
    }
  }

  const set = (field: keyof ParametroSistemaRequest) => (e: React.ChangeEvent<HTMLInputElement>) =>
    setParametro((prev) => ({ ...prev, [field]: field === 'taxaMediaEquipamentosDefeituosos' ? parseFloat(e.target.value) : parseInt(e.target.value) || 0 }))

  return (
    <div className="space-y-6">
      <h2 className="text-2xl font-bold text-slate-800">Parâmetros do Sistema</h2>
      <form onSubmit={handleSubmit} className="bg-white rounded-xl shadow-sm p-6 space-y-4 max-w-2xl">
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Tempo Médio de Reposição (dias)</label>
            <input type="number" value={parametro.tempoMedioReposicao} onChange={set('tempoMedioReposicao')} required className="w-full px-3 py-2 border border-gray-300 rounded-lg" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Tempo Médio Consumo (dias)</label>
            <input type="number" value={parametro.tempoMedioConsumoEstoque} onChange={set('tempoMedioConsumoEstoque')} required className="w-full px-3 py-2 border border-gray-300 rounded-lg" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Tempo Médio de Envio (dias)</label>
            <input type="number" value={parametro.tempoMedioEnvio} onChange={set('tempoMedioEnvio')} required className="w-full px-3 py-2 border border-gray-300 rounded-lg" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Taxa de Defeitos (0-1)</label>
            <input type="number" step="0.01" min="0" max="1" value={parametro.taxaMediaEquipamentosDefeituosos} onChange={set('taxaMediaEquipamentosDefeituosos')} required className="w-full px-3 py-2 border border-gray-300 rounded-lg" />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Estoque Mínimo de Segurança</label>
            <input type="number" value={parametro.estoqueMinimoSeguranca} onChange={set('estoqueMinimoSeguranca')} required className="w-full px-3 py-2 border border-gray-300 rounded-lg" />
          </div>
        </div>
        <button type="submit" disabled={loading} className="px-4 py-2 bg-slate-800 text-white rounded-lg hover:bg-slate-700 disabled:opacity-50">
          {loading ? 'Salvando...' : 'Salvar Parâmetros'}
        </button>
      </form>

      <div className="bg-white rounded-xl shadow-sm p-6">
        <h3 className="text-lg font-semibold text-slate-800 mb-4">Relatório de Previsão de Falta</h3>
        <table className="w-full text-sm">
          <thead className="bg-gray-50 text-gray-600">
            <tr>
              <th className="text-left px-4 py-3 font-medium">Tipo</th>
              <th className="text-right px-4 py-3 font-medium">Disponível</th>
              <th className="text-right px-4 py-3 font-medium">Em Uso</th>
              <th className="text-right px-4 py-3 font-medium">Defeituoso</th>
              <th className="text-right px-4 py-3 font-medium">Ponto Pedido</th>
              <th className="text-center px-4 py-3 font-medium">Risco</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {previsoes.map((p) => (
              <tr key={p.idTipoEquipamento} className="hover:bg-gray-50">
                <td className="px-4 py-3 text-slate-800">{p.nomeTipoEquipamento}</td>
                <td className="px-4 py-3 text-right text-gray-600">{p.quantidadeDisponivelAtual}</td>
                <td className="px-4 py-3 text-right text-gray-600">{p.quantidadeEmUsoAtual}</td>
                <td className="px-4 py-3 text-right text-gray-600">{p.quantidadeDefeituosaAtual}</td>
                <td className="px-4 py-3 text-right text-gray-600">{p.pontoDePedido}</td>
                <td className="px-4 py-3 text-center">
                  <span className={`inline-block px-2 py-0.5 rounded-full text-xs font-medium ${p.emRiscoDeFalta ? 'bg-red-100 text-red-700' : 'bg-green-100 text-green-700'}`}>
                    {p.emRiscoDeFalta ? 'Sim' : 'Não'}
                  </span>
                </td>
              </tr>
            ))}
            {previsoes.length === 0 && (
              <tr><td colSpan={6} className="px-4 py-8 text-center text-gray-400">Nenhum dado disponível</td></tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  )
}
