package br.com.boasaude.gisa.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.boasaude.gisa.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DisponibilidadeConsultaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DisponibilidadeConsulta.class);
        DisponibilidadeConsulta disponibilidadeConsulta1 = new DisponibilidadeConsulta();
        disponibilidadeConsulta1.setId(1L);
        DisponibilidadeConsulta disponibilidadeConsulta2 = new DisponibilidadeConsulta();
        disponibilidadeConsulta2.setId(disponibilidadeConsulta1.getId());
        assertThat(disponibilidadeConsulta1).isEqualTo(disponibilidadeConsulta2);
        disponibilidadeConsulta2.setId(2L);
        assertThat(disponibilidadeConsulta1).isNotEqualTo(disponibilidadeConsulta2);
        disponibilidadeConsulta1.setId(null);
        assertThat(disponibilidadeConsulta1).isNotEqualTo(disponibilidadeConsulta2);
    }
}
