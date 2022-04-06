package br.com.boasaude.gisa.domain;

import br.com.boasaude.gisa.domain.enumeration.CategoriaPlano;
import br.com.boasaude.gisa.domain.enumeration.TipoPlano;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Plano.
 */
@Entity
@Table(name = "plano")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Plano implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "codigo_ans")
    private String codigoANS;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria")
    private CategoriaPlano categoria;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoPlano tipo;

    @Column(name = "odonto")
    private Boolean odonto;

    @Column(name = "idade_min")
    private Integer idadeMin;

    @Column(name = "idade_max")
    private Integer idadeMax;

    @Column(name = "quantidade_consultas_ano")
    private Integer quantidadeConsultasAno;

    @Column(name = "quanatidade_exames_ano")
    private Integer quanatidadeExamesAno;

    @Column(name = "valor_mensalidade")
    private Double valorMensalidade;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Plano id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Plano nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigoANS() {
        return this.codigoANS;
    }

    public Plano codigoANS(String codigoANS) {
        this.setCodigoANS(codigoANS);
        return this;
    }

    public void setCodigoANS(String codigoANS) {
        this.codigoANS = codigoANS;
    }

    public CategoriaPlano getCategoria() {
        return this.categoria;
    }

    public Plano categoria(CategoriaPlano categoria) {
        this.setCategoria(categoria);
        return this;
    }

    public void setCategoria(CategoriaPlano categoria) {
        this.categoria = categoria;
    }

    public TipoPlano getTipo() {
        return this.tipo;
    }

    public Plano tipo(TipoPlano tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(TipoPlano tipo) {
        this.tipo = tipo;
    }

    public Boolean getOdonto() {
        return this.odonto;
    }

    public Plano odonto(Boolean odonto) {
        this.setOdonto(odonto);
        return this;
    }

    public void setOdonto(Boolean odonto) {
        this.odonto = odonto;
    }

    public Integer getIdadeMin() {
        return this.idadeMin;
    }

    public Plano idadeMin(Integer idadeMin) {
        this.setIdadeMin(idadeMin);
        return this;
    }

    public void setIdadeMin(Integer idadeMin) {
        this.idadeMin = idadeMin;
    }

    public Integer getIdadeMax() {
        return this.idadeMax;
    }

    public Plano idadeMax(Integer idadeMax) {
        this.setIdadeMax(idadeMax);
        return this;
    }

    public void setIdadeMax(Integer idadeMax) {
        this.idadeMax = idadeMax;
    }

    public Integer getQuantidadeConsultasAno() {
        return this.quantidadeConsultasAno;
    }

    public Plano quantidadeConsultasAno(Integer quantidadeConsultasAno) {
        this.setQuantidadeConsultasAno(quantidadeConsultasAno);
        return this;
    }

    public void setQuantidadeConsultasAno(Integer quantidadeConsultasAno) {
        this.quantidadeConsultasAno = quantidadeConsultasAno;
    }

    public Integer getQuanatidadeExamesAno() {
        return this.quanatidadeExamesAno;
    }

    public Plano quanatidadeExamesAno(Integer quanatidadeExamesAno) {
        this.setQuanatidadeExamesAno(quanatidadeExamesAno);
        return this;
    }

    public void setQuanatidadeExamesAno(Integer quanatidadeExamesAno) {
        this.quanatidadeExamesAno = quanatidadeExamesAno;
    }

    public Double getValorMensalidade() {
        return this.valorMensalidade;
    }

    public Plano valorMensalidade(Double valorMensalidade) {
        this.setValorMensalidade(valorMensalidade);
        return this;
    }

    public void setValorMensalidade(Double valorMensalidade) {
        this.valorMensalidade = valorMensalidade;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Plano)) {
            return false;
        }
        return id != null && id.equals(((Plano) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Plano{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", codigoANS='" + getCodigoANS() + "'" +
            ", categoria='" + getCategoria() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", odonto='" + getOdonto() + "'" +
            ", idadeMin=" + getIdadeMin() +
            ", idadeMax=" + getIdadeMax() +
            ", quantidadeConsultasAno=" + getQuantidadeConsultasAno() +
            ", quanatidadeExamesAno=" + getQuanatidadeExamesAno() +
            ", valorMensalidade=" + getValorMensalidade() +
            "}";
    }
}
