package br.com.boasaude.gisa.domain;

import br.com.boasaude.gisa.domain.enumeration.SituacaoAtendimento;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Exame.
 */
@Entity
@Table(name = "exame")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Exame implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao")
    private SituacaoAtendimento situacao;

    @JsonIgnoreProperties(value = { "prestador", "tipoExame" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private DisponibilidadeExame disponibilidadeExame;

    @ManyToOne
    @JsonIgnoreProperties(value = { "endereco", "plano" }, allowSetters = true)
    private Associado associado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Exame id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SituacaoAtendimento getSituacao() {
        return this.situacao;
    }

    public Exame situacao(SituacaoAtendimento situacao) {
        this.setSituacao(situacao);
        return this;
    }

    public void setSituacao(SituacaoAtendimento situacao) {
        this.situacao = situacao;
    }

    public DisponibilidadeExame getDisponibilidadeExame() {
        return this.disponibilidadeExame;
    }

    public void setDisponibilidadeExame(DisponibilidadeExame disponibilidadeExame) {
        this.disponibilidadeExame = disponibilidadeExame;
    }

    public Exame disponibilidadeExame(DisponibilidadeExame disponibilidadeExame) {
        this.setDisponibilidadeExame(disponibilidadeExame);
        return this;
    }

    public Associado getAssociado() {
        return this.associado;
    }

    public void setAssociado(Associado associado) {
        this.associado = associado;
    }

    public Exame associado(Associado associado) {
        this.setAssociado(associado);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Exame)) {
            return false;
        }
        return id != null && id.equals(((Exame) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Exame{" +
            "id=" + getId() +
            ", situacao='" + getSituacao() + "'" +
            "}";
    }
}
