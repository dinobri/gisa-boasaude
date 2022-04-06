package br.com.boasaude.gisa.domain;

import br.com.boasaude.gisa.domain.enumeration.Sexo;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Prestador.
 */
@Entity
@Table(name = "prestador")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Prestador implements Serializable {

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
    @Column(name = "sexo")
    private Sexo sexo;

    @Column(name = "numero_registro_profissional")
    private String numeroRegistroProfissional;

    @Column(name = "telefone")
    private String telefone;

    @OneToOne
    @JoinColumn(unique = true)
    private Endereco enderecoAtendimento;

    @ManyToOne
    private Especialidade especialidade;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Prestador id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Prestador nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return this.sobrenome;
    }

    public Prestador sobrenome(String sobrenome) {
        this.setSobrenome(sobrenome);
        return this;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public Instant getDataNascimento() {
        return this.dataNascimento;
    }

    public Prestador dataNascimento(Instant dataNascimento) {
        this.setDataNascimento(dataNascimento);
        return this;
    }

    public void setDataNascimento(Instant dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Sexo getSexo() {
        return this.sexo;
    }

    public Prestador sexo(Sexo sexo) {
        this.setSexo(sexo);
        return this;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public String getNumeroRegistroProfissional() {
        return this.numeroRegistroProfissional;
    }

    public Prestador numeroRegistroProfissional(String numeroRegistroProfissional) {
        this.setNumeroRegistroProfissional(numeroRegistroProfissional);
        return this;
    }

    public void setNumeroRegistroProfissional(String numeroRegistroProfissional) {
        this.numeroRegistroProfissional = numeroRegistroProfissional;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public Prestador telefone(String telefone) {
        this.setTelefone(telefone);
        return this;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Endereco getEnderecoAtendimento() {
        return this.enderecoAtendimento;
    }

    public void setEnderecoAtendimento(Endereco endereco) {
        this.enderecoAtendimento = endereco;
    }

    public Prestador enderecoAtendimento(Endereco endereco) {
        this.setEnderecoAtendimento(endereco);
        return this;
    }

    public Especialidade getEspecialidade() {
        return this.especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }

    public Prestador especialidade(Especialidade especialidade) {
        this.setEspecialidade(especialidade);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Prestador)) {
            return false;
        }
        return id != null && id.equals(((Prestador) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Prestador{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", sobrenome='" + getSobrenome() + "'" +
            ", dataNascimento='" + getDataNascimento() + "'" +
            ", sexo='" + getSexo() + "'" +
            ", numeroRegistroProfissional='" + getNumeroRegistroProfissional() + "'" +
            ", telefone='" + getTelefone() + "'" +
            "}";
    }
}
