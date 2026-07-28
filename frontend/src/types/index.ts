export interface LoginRequest {
  email: string
  senha: string
}

export interface TokenResponse {
  accessToken: string
  refreshToken: string
  expiracao: string
}

export interface RefreshTokenRequest {
  refreshToken: string
}

export interface UsuarioCriacao {
  email: string
  senha: string
}

export interface UsuarioResponse {
  id: number
  email: string
  emailConfirmado: boolean
  authorities: string[]
}

export interface ViaCEPResponse {
  cep: string
  logradouro: string
  complemento: string
  bairro: string
  localidade: string
  uf: string
  estado: string
  regiao: string
  ibge: string
  gia: string
  ddd: string
  siafi: string
  erro?: boolean
}

export interface ColaboradorRequest {
  cpf: string
  nome: string
  email: string
  cep: string
  data_aniversario: string
  data_contratacao_inicio: string
  data_contratacao_recisao?: string
  especificacao_equipamento: string
  numeroCasa: number
}

export interface ColaboradorUpdate {
  nome?: string
  email?: string
  cep?: string
  dataContratacaoInicio?: string
  dataContratacaoRecisao?: string
  equipamentoEspecificacao?: string
}

export interface EnderecoResponse {
  cep: string
  logradouro: string
  complemento: string
  unidade: string
  bairro: string
  localidade: string
  uf: string
  estado: string
  regiao: string
  ibge: string
  gia: string
  ddd: string
  siafi: string
  numero: number
}

export interface ColaboradorResponse {
  id: number
  nome: string
  email: string
  cep: string
  data_aniversario: string
  data_contratacao_inicio: string
  data_contratacao_recisao?: string
  especificacao_equipamento: string
  endereco: EnderecoResponse
}

export interface EquipamentoRequest {
  especificacoes: string
  numeroSerie: string
  marca: string
  modelo: string
  dataAquisicao: string
  tempoUso: number
  id_tipo_equipamento: number
  disponivel: boolean
}

export interface EquipamentoResponse {
  id: number
  especificacoes: string
  numeroSerie: string
  marca: string
  modelo: string
  dataAquisicao: string
  tempoUso: number
  id_tipo_equipamento: number
  disponivel: boolean
  estoque: EstoqueResumo
  previsaoEntrega: string
}

export interface EstoqueResumo {
  quantidadeDisponivel: number
  quantidadeEmUso: number
  quantidadeDefeituosa: number
}

export interface TipoEquipamento {
  id: number
  nomeTipo: string
}

export interface TipoEquipamentoRequest {
  nomeTipo: string
}

export interface IdEquipamentoToColaborador {
  idEquipamento: number
  idColaborador: number
}

export interface EmprestimoRequest {
  chaveCompostaEquipamentoColaborador: IdEquipamentoToColaborador
  dataEntrega: string
  dataDevolucao?: string
}

export interface EmprestimoResponse {
  dataEntrega: string
  dataDevolucao?: string
  previsaoEntrega: string
  chaveCompostaEquipamentoColaborador: IdEquipamentoToColaborador
}

export interface ParametroSistemaRequest {
  tempoMedioReposicao: number
  tempoMedioConsumoEstoque: number
  tempoMedioEnvio: number
  taxaMediaEquipamentosDefeituosos: number
  estoqueMinimoSeguranca: number
}

export interface ParametroSistemaResponse {
  idParametro: number
  tempoMedioReposicao: number
  tempoMedioConsumoEstoque: number
  tempoMedioEnvio: number
  taxaMediaEquipamentosDefeituosos: number
  estoqueMinimoSeguranca: number
}

export interface PrevisaoFaltaResponse {
  idTipoEquipamento: number
  nomeTipoEquipamento: string
  numeroSerie: string
  quantidadeDisponivelAtual: number
  quantidadeEmUsoAtual: number
  quantidadeDefeituosaAtual: number
  consumoDiarioEstimado: number
  leadTimeTotalDias: number
  estoqueMinimoSeguranca: number
  pontoDePedido: number
  emRiscoDeFalta: boolean
  mensagemAlerta: string
}

export interface Alerta {
  id: number
  tipoAlerta: string
  dataHoraGeracao: string
  status: string
  descricao: string
  tipoEquipamento: TipoEquipamento
}
