package br.com.boasaude.gisa.domain;

import br.com.boasaude.gisa.domain.enumeration.EstadoCivil;
import br.com.boasaude.gisa.domain.enumeration.Sexo;
import br.com.boasaude.gisa.domain.enumeration.SituacaoAssociado;
import br.com.boasaude.gisa.domain.enumeration.UF;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Associado.
 */
@Entity
@Table(name = "associado")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Associado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "sobrenome")
    private String sobrenome;

    @Column(name = "data_nascimento")
    private Instant dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_civil")
    private EstadoCivil estadoCivil;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo")
    private Sexo sexo;

    @Enumerated(EnumType.STRING)
    @Column(name = "naturalidade_uf")
    private UF naturalidadeUf;

    @Column(name = "naturalidade_cidade")
    private String naturalidadeCidade;

    @Column(name = "numero_documento")
    private String numeroDocumento;

    @Enumerated(EnumType.STRING)
    @Column(name = "uf_documento")
    private UF ufDocumento;

    @Column(name = "orgao_documento")
    private String orgaoDocumento;

    @Column(name = "data_documento")
    private Instant dataDocumento;

    @Column(name = "nome_mae")
    private String nomeMae;

    @Column(name = "nome_pai")
    private String nomePai;

    @Column(name = "email")
    private String email;

    @Column(name = "telefone")
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(name = "sitaucao")
    private SituacaoAssociado sitaucao;

    @OneToOne
    @JoinColumn(unique = true)
    private Endereco endereco;

    @ManyToOne
    private Plano plano;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Associado id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Associado nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return this.sobrenome;
    }

    public Associado sobrenome(String sobrenome) {
        this.setSobrenome(sobrenome);
        return this;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public Instant getDataNascimento() {
        return this.dataNascimento;
    }

    public Associado dataNascimento(Instant dataNascimento) {
        this.setDataNascimento(dataNascimento);
        return this;
    }

    public void setDataNascimento(Instant dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public EstadoCivil getEstadoCivil() {
        return this.estadoCivil;
    }

    public Associado estadoCivil(EstadoCivil estadoCivil) {
        this.setEstadoCivil(estadoCivil);
        return this;
    }

    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public Sexo getSexo() {
        return this.sexo;
    }

    public Associado sexo(Sexo sexo) {
        this.setSexo(sexo);
        return this;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public UF getNaturalidadeUf() {
        return this.naturalidadeUf;
    }

    public Associado naturalidadeUf(UF naturalidadeUf) {
        this.setNaturalidadeUf(naturalidadeUf);
        return this;
    }

    public void setNaturalidadeUf(UF naturalidadeUf) {
        this.naturalidadeUf = naturalidadeUf;
    }

    public String getNaturalidadeCidade() {
        return this.naturalidadeCidade;
    }

    public Associado naturalidadeCidade(String naturalidadeCidade) {
        this.setNaturalidadeCidade(naturalidadeCidade);
        return this;
    }

    public void setNaturalidadeCidade(String naturalidadeCidade) {
        this.naturalidadeCidade = naturalidadeCidade;
    }

    public String getNumeroDocumento() {
        return this.numeroDocumento;
    }

    public Associado numeroDocumento(String numeroDocumento) {
        this.setNumeroDocumento(numeroDocumento);
        return this;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public UF getUfDocumento() {
        return this.ufDocumento;
    }

    public Associado ufDocumento(UF ufDocumento) {
        this.setUfDocumento(ufDocumento);
        return this;
    }

    public void setUfDocumento(UF ufDocumento) {
        this.ufDocumento = ufDocumento;
    }

    public String getOrgaoDocumento() {
        return this.orgaoDocumento;
    }

    public Associado orgaoDocumento(String orgaoDocumento) {
        this.setOrgaoDocumento(orgaoDocumento);
        return this;
    }

    public void setOrgaoDocumento(String orgaoDocumento) {
        this.orgaoDocumento = orgaoDocumento;
    }

    public Instant getDataDocumento() {
        return this.dataDocumento;
    }

    public Associado dataDocumento(Instant dataDocumento) {
        this.setDataDocumento(dataDocumento);
        return this;
    }

    public void setDataDocumento(Instant dataDocumento) {
        this.dataDocumento = dataDocumento;
    }

    public String getNomeMae() {
        return this.nomeMae;
    }

    public Associado nomeMae(String nomeMae) {
        this.setNomeMae(nomeMae);
        return this;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public String getNomePai() {
        return this.nomePai;
    }

    public Associado nomePai(String nomePai) {
        this.setNomePai(nomePai);
        return this;
    }

    public void setNomePai(String nomePai) {
        this.nomePai = nomePai;
    }

    public String getEmail() {
        return this.email;
    }

    public Associado email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public Associado telefone(String telefone) {
        this.setTelefone(telefone);
        return this;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public SituacaoAssociado getSitaucao() {
        return this.sitaucao;
    }

    public Associado sitaucao(SituacaoAssociado sitaucao) {
        this.setSitaucao(sitaucao);
        return this;
    }

    public void setSitaucao(SituacaoAssociado sitaucao) {
        this.sitaucao = sitaucao;
    }

    public Endereco getEndereco() {
        return this.endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Associado endereco(Endereco endereco) {
        this.setEndereco(endereco);
        return this;
    }

    public Plano getPlano() {
        return this.plano;
    }

    public void setPlano(Plano plano) {
        this.plano = plano;
    }

    public Associado plano(Plano plano) {
        this.setPlano(plano);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Associado)) {
            return false;
        }
        return id != null && id.equals(((Associado) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Associado{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", sobrenome='" + getSobrenome() + "'" +
            ", dataNascimento='" + getDataNascimento() + "'" +
            ", estadoCivil='" + getEstadoCivil() + "'" +
            ", sexo='" + getSexo() + "'" +
            ", naturalidadeUf='" + getNaturalidadeUf() + "'" +
            ", naturalidadeCidade='" + getNaturalidadeCidade() + "'" +
            ", numeroDocumento='" + getNumeroDocumento() + "'" +
            ", ufDocumento='" + getUfDocumento() + "'" +
            ", orgaoDocumento='" + getOrgaoDocumento() + "'" +
            ", dataDocumento='" + getDataDocumento() + "'" +
            ", nomeMae='" + getNomeMae() + "'" +
            ", nomePai='" + getNomePai() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefone='" + getTelefone() + "'" +
            ", sitaucao='" + getSitaucao() + "'" +
            "}";
    }
}
