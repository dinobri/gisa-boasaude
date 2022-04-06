package br.com.boasaude.gisa.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.boasaude.gisa.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrestadorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Prestador.class);
        Prestador prestador1 = new Prestador();
        prestador1.setId(1L);
        Prestador prestador2 = new Prestador();
        prestador2.setId(prestador1.getId());
        assertThat(prestador1).isEqualTo(prestador2);
        prestador2.setId(2L);
        assertThat(prestador1).isNotEqualTo(prestador2);
        prestador1.setId(null);
        assertThat(prestador1).isNotEqualTo(prestador2);
    }
}
