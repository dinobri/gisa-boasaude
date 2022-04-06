package br.com.boasaude.gisa.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TipoExame.
 */
@Entity
@Table(name = "tipo_exame")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TipoExame implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @ManyToMany(mappedBy = "tipoExames")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "endereco", "tipoExames" }, allowSetters = true)
    private Set<Conveniado> conveniados = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TipoExame id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public TipoExame nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Conveniado> getConveniados() {
        return this.conveniados;
    }

    public void setConveniados(Set<Conveniado> conveniados) {
        if (this.conveniados != null) {
            this.conveniados.forEach(i -> i.removeTipoExame(this));
        }
        if (conveniados != null) {
            conveniados.forEach(i -> i.addTipoExame(this));
        }
        this.conveniados = conveniados;
    }

    public TipoExame conveniados(Set<Conveniado> conveniados) {
        this.setConveniados(conveniados);
        return this;
    }

    public TipoExame addConveniado(Conveniado conveniado) {
        this.conveniados.add(conveniado);
        conveniado.getTipoExames().add(this);
        return this;
    }

    public TipoExame removeConveniado(Conveniado conveniado) {
        this.conveniados.remove(conveniado);
        conveniado.getTipoExames().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoExame)) {
            return false;
        }
        return id != null && id.equals(((TipoExame) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoExame{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
