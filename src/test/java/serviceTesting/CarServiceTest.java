package serviceTesting;

import model.service.CarService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.Assert.assertEquals;

public class CarServiceTest {

    @ParameterizedTest
    @CsvSource({"cheap,1","comfort,2","business,3"})
    public void typeIdTest(String type, int expected){
        assertEquals(expected, CarService.getTypeId(type));
    }
}
