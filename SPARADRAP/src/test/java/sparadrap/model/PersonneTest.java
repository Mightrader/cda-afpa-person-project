package sparadrap.model;

import org.junit.jupiter.api.Test;
import sparadrap.exception.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

class PersonneTest {

    @Test
    void creationValideRenseigneLesChamps() {
        Personne p = new Personne(1, " Dupont ", " Jean ");

        assertEquals(1, p.getId());
        assertEquals("Dupont", p.getNom());
        assertEquals("Jean", p.getPrenom());
        assertEquals("Dupont Jean", p.getNomComplet());
    }

    @Test
    void setNomRefuseNomVide() {
        Personne p = new Personne();
        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> p.setNom("   ")
        );
        assertTrue(ex.getMessage().toLowerCase().contains("nom"));
    }

    @Test
    void setPrenomRefusePrenomVide() {
        Personne p = new Personne();
        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> p.setPrenom("   ")
        );
        assertTrue(ex.getMessage().toLowerCase().contains("prénom"));
    }

    @Test
    void setCodePostalValide() {
        Personne p = new Personne();
        p.setCodePostal("75001");
        assertEquals("75001", p.getCodePostal());
    }

    @Test
    void setCodePostalInvalideDeclencheException() {
        Personne p = new Personne();
        assertThrows(ValidationException.class,
                () -> p.setCodePostal("ABC"));
    }

    @Test
    void setTelephoneValide() {
        Personne p = new Personne();
        p.setTelephone("0102030405");
        assertEquals("0102030405", p.getTelephone());
    }

    @Test
    void setTelephoneInvalideDeclencheException() {
        Personne p = new Personne();
        assertThrows(ValidationException.class,
                () -> p.setTelephone("1234"));
    }

    @Test
    void setEmailValide() {
        Personne p = new Personne();
        p.setEmail("test@example.com");
        assertEquals("test@example.com", p.getEmail());
    }

    @Test
    void setEmailInvalideDeclencheException() {
        Personne p = new Personne();
        assertThrows(ValidationException.class,
                () -> p.setEmail("pas-un-email"));
    }
}
