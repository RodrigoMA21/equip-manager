import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { AuthProvider } from './contexts/AuthContext'
import { ProtectedRoute } from './components/ProtectedRoute'
import { Layout } from './components/Layout'
import { Login } from './pages/Login'
import { Dashboard } from './pages/Dashboard'
import { ColaboradoresList } from './pages/Colaboradores/List'
import { ColaboradorForm } from './pages/Colaboradores/Form'
import { EquipamentosList } from './pages/Equipamentos/List'
import { EquipamentoForm } from './pages/Equipamentos/Form'
import { TiposEquipamentoList } from './pages/TiposEquipamento/List'
import { EmprestimosList } from './pages/Emprestimos/List'
import { EmprestimoForm } from './pages/Emprestimos/Form'
import { ParametrosForm } from './pages/Parametros/Form'

const queryClient = new QueryClient()

export default function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <AuthProvider>
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route element={<ProtectedRoute><Layout /></ProtectedRoute>}>
              <Route path="/" element={<Dashboard />} />
              <Route path="/colaboradores" element={<ColaboradoresList />} />
              <Route path="/colaboradores/novo" element={<ColaboradorForm />} />
              <Route path="/colaboradores/:id" element={<ColaboradorForm />} />
              <Route path="/equipamentos" element={<EquipamentosList />} />
              <Route path="/equipamentos/novo" element={<EquipamentoForm />} />
              <Route path="/equipamentos/:id" element={<EquipamentoForm />} />
              <Route path="/tipos-equipamento" element={<TiposEquipamentoList />} />
              <Route path="/emprestimos" element={<EmprestimosList />} />
              <Route path="/emprestimos/novo" element={<EmprestimoForm />} />
              <Route path="/parametros" element={<ParametrosForm />} />
            </Route>
          </Routes>
        </AuthProvider>
      </BrowserRouter>
    </QueryClientProvider>
  )
}
