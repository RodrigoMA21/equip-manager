package br.com.projetofinal_grupo1.projetofinal_grupo1.enums;

public enum RegiaoEntrega {
    SUDESTE("Sudeste", 2),
    SUL("Sul", 4),
    NORDESTE("Nordeste", 6),
    CENTRO_OESTE("Centro-Oeste", 5),
    NORTE("Norte", 8);

    private final String nome;
    private final int diasEstimados;

    RegiaoEntrega(String nome, int diasEstimados) {
        this.nome = nome;
        this.diasEstimados = diasEstimados;
    }

    public String getNome() {
        return nome;
    }

    public int getDiasEstimados() {
        return diasEstimados;
    }
}
