package br.com.boasaude.gisa.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Especialidade.
 */
@Entity
@Table(name = "especialidade")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Especialidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Especialidade id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Especialidade nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Especialidade)) {
            return false;
        }
        return id != null && id.equals(((Especialidade) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Especialidade{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
