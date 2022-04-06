package br.com.boasaude.gisa.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DisponibilidadeExame.
 */
@Entity
@Table(name = "disponibilidade_exame")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DisponibilidadeExame implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "hora_inicio")
    private Instant horaInicio;

    @Column(name = "hora_fim")
    private Instant horaFim;

    @ManyToOne
    @JsonIgnoreProperties(value = { "enderecoAtendimento", "especialidade" }, allowSetters = true)
    private Prestador prestador;

    @ManyToOne
    @JsonIgnoreProperties(value = { "conveniados" }, allowSetters = true)
    private TipoExame tipoExame;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DisponibilidadeExame id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getHoraInicio() {
        return this.horaInicio;
    }

    public DisponibilidadeExame horaInicio(Instant horaInicio) {
        this.setHoraInicio(horaInicio);
        return this;
    }

    public void setHoraInicio(Instant horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Instant getHoraFim() {
        return this.horaFim;
    }

    public DisponibilidadeExame horaFim(Instant horaFim) {
        this.setHoraFim(horaFim);
        return this;
    }

    public void setHoraFim(Instant horaFim) {
        this.horaFim = horaFim;
    }

    public Prestador getPrestador() {
        return this.prestador;
    }

    public void setPrestador(Prestador prestador) {
        this.prestador = prestador;
    }

    public DisponibilidadeExame prestador(Prestador prestador) {
        this.setPrestador(prestador);
        return this;
    }

    public TipoExame getTipoExame() {
        return this.tipoExame;
    }

    public void setTipoExame(TipoExame tipoExame) {
        this.tipoExame = tipoExame;
    }

    public DisponibilidadeExame tipoExame(TipoExame tipoExame) {
        this.setTipoExame(tipoExame);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DisponibilidadeExame)) {
            return false;
        }
        return id != null && id.equals(((DisponibilidadeExame) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DisponibilidadeExame{" +
            "id=" + getId() +
            ", horaInicio='" + getHoraInicio() + "'" +
            ", horaFim='" + getHoraFim() + "'" +
            "}";
    }
}
