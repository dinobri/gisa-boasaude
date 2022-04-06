package br.com.boasaude.gisa.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Conveniado.
 */
@Entity
@Table(name = "conveniado")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Conveniado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "telefone")
    private String telefone;

    @OneToOne
    @JoinColumn(unique = true)
    private Endereco endereco;

    @ManyToMany
    @JoinTable(
        name = "rel_conveniado__tipo_exame",
        joinColumns = @JoinColumn(name = "conveniado_id"),
        inverseJoinColumns = @JoinColumn(name = "tipo_exame_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "conveniados" }, allowSetters = true)
    private Set<TipoExame> tipoExames = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Conveniado id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Conveniado nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public Conveniado telefone(String telefone) {
        this.setTelefone(telefone);
        return this;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Endereco getEndereco() {
        return this.endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Conveniado endereco(Endereco endereco) {
        this.setEndereco(endereco);
        return this;
    }

    public Set<TipoExame> getTipoExames() {
        return this.tipoExames;
    }

    public void setTipoExames(Set<TipoExame> tipoExames) {
        this.tipoExames = tipoExames;
    }

    public Conveniado tipoExames(Set<TipoExame> tipoExames) {
        this.setTipoExames(tipoExames);
        return this;
    }

    public Conveniado addTipoExame(TipoExame tipoExame) {
        this.tipoExames.add(tipoExame);
        tipoExame.getConveniados().add(this);
        return this;
    }

    public Conveniado removeTipoExame(TipoExame tipoExame) {
        this.tipoExames.remove(tipoExame);
        tipoExame.getConveniados().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Conveniado)) {
            return false;
        }
        return id != null && id.equals(((Conveniado) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Conveniado{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", telefone='" + getTelefone() + "'" +
            "}";
    }
}
