package serviceTesting;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import utils.Security;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SecurityTest {

    @ParameterizedTest
    @ValueSource(strings = {"password", "mypassword", "such a good pass", "LKskdmsi38k", "9437834920", "CAPSTEST"})
    public void PasswordCheck(String testPassword) throws Exception {
        String hexed = Security.hashPassword(testPassword);
        assertTrue(Security.isPasswordCorrect(testPassword, hexed));
    }

    @ParameterizedTest
    @ValueSource(strings = {"password", "mypassword", "such a good pass", "LKskdmsi38k", "9437834920", "CAPSTEST"})
    public void PasswordValidationTest(String testPassword){
        assertTrue(Security.isPasswordValid(testPassword));
    }

    @ParameterizedTest
    @ValueSource(strings = {"email@gmail.com", "&3hds@gmail.com", "veryLongEmail@mail.ru"})
    public void EmailValidationTest(String testPassword){
        assertTrue(Security.isPasswordValid(testPassword));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "short", "this one is too long and has to be false skjfkjdshkfhkdshfjshjfhsjhfs"})
    public void PasswordNotValidTest(String testPassword){
        assertFalse(Security.isPasswordValid(testPassword));
    }

    @ParameterizedTest
    @ValueSource(strings = {"email", ""})
    public void EmailNotValidTest(String testPassword){
        assertFalse(Security.isPasswordValid(testPassword));
    }

    @Test
    public void PasswordNullTest(){
        assertFalse(Security.isPasswordValid(null));
    }

    @Test
    public void EmailNullTest(){
        assertFalse(Security.isPasswordValid(null));
    }
}
