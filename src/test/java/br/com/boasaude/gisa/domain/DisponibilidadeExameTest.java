package br.com.boasaude.gisa.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.boasaude.gisa.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DisponibilidadeExameTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DisponibilidadeExame.class);
        DisponibilidadeExame disponibilidadeExame1 = new DisponibilidadeExame();
        disponibilidadeExame1.setId(1L);
        DisponibilidadeExame disponibilidadeExame2 = new DisponibilidadeExame();
        disponibilidadeExame2.setId(disponibilidadeExame1.getId());
        assertThat(disponibilidadeExame1).isEqualTo(disponibilidadeExame2);
        disponibilidadeExame2.setId(2L);
        assertThat(disponibilidadeExame1).isNotEqualTo(disponibilidadeExame2);
        disponibilidadeExame1.setId(null);
        assertThat(disponibilidadeExame1).isNotEqualTo(disponibilidadeExame2);
    }
}
