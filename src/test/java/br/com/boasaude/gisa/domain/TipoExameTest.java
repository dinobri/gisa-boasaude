package br.com.boasaude.gisa.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.boasaude.gisa.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoExameTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoExame.class);
        TipoExame tipoExame1 = new TipoExame();
        tipoExame1.setId(1L);
        TipoExame tipoExame2 = new TipoExame();
        tipoExame2.setId(tipoExame1.getId());
        assertThat(tipoExame1).isEqualTo(tipoExame2);
        tipoExame2.setId(2L);
        assertThat(tipoExame1).isNotEqualTo(tipoExame2);
        tipoExame1.setId(null);
        assertThat(tipoExame1).isNotEqualTo(tipoExame2);
    }
}
