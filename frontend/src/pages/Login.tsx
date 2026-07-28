import { useState, type FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'

export function Login() {
  const [email, setEmail] = useState('')
  const [senha, setSenha] = useState('')
  const [showSenha, setShowSenha] = useState(false)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const { login } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      await login({ email, senha })
      navigate('/')
    } catch {
      setError('Email ou senha inválidos')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-800 to-slate-900 p-4">
      <div className="w-full max-w-4xl flex flex-col md:flex-row rounded-2xl shadow-xl overflow-hidden">
        <div className="md:w-1/2 bg-slate-800 p-8 md:p-10 flex flex-col justify-center text-white">
          <h1 className="text-3xl font-bold mb-4">EquipManager</h1>
          <p className="text-slate-300 leading-relaxed mb-4">
            Sistema de gestão de equipamentos para controle de inventário,
            empréstimos e manutenção de ativos. Gerencie colaboradores,
            equipamentos, tipos e estoque em um só lugar.
          </p>
          <ul className="space-y-2 text-sm text-slate-400">
            <li className="flex items-center gap-2">✓ Controle de estoque inteligente</li>
            <li className="flex items-center gap-2">✓ Empréstimos com previsão de entrega</li>
            <li className="flex items-center gap-2">✓ Alertas automáticos de falta</li>
            <li className="flex items-center gap-2">✓ Relatório de previsão de demanda</li>
          </ul>
        </div>
        <div className="md:w-1/2 bg-white p-8 md:p-10 flex flex-col justify-center">
          <h2 className="text-xl font-semibold text-slate-800 mb-6">Acessar o sistema</h2>
          {error && <div className="mb-4 p-3 bg-red-50 text-red-700 text-sm rounded-lg">{error}</div>}
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
              <input
                type="email" required value={email} onChange={(e) => setEmail(e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-slate-500"
                placeholder="seu@email.com"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Senha</label>
              <div className="relative">
                <input
                  type={showSenha ? 'text' : 'password'} required value={senha}
                  onChange={(e) => setSenha(e.target.value)}
                  className="w-full px-3 py-2 pr-10 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-slate-500"
                  placeholder="********"
                />
                <button type="button" onClick={() => setShowSenha(!showSenha)} className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600">
                  {showSenha ? (
                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.543 7a10.025 10.025 0 01-4.132 5.411m0 0L21 21" />
                    </svg>
                  ) : (
                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                    </svg>
                  )}
                </button>
              </div>
            </div>
            <button
              type="submit" disabled={loading}
              className="w-full py-2.5 bg-slate-800 text-white rounded-lg hover:bg-slate-700 disabled:opacity-50 transition-colors font-medium"
            >
              {loading ? 'Entrando...' : 'Entrar'}
            </button>
          </form>
        </div>
      </div>
    </div>
  )
}
