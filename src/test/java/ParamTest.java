/*Ты пишешь игру про сапёра. Ему предстоит обезвредить бомбу.
Для этого нужно перерезать два провода из трёх.
Если выбрать провод не того цвета, бомба взорвётся.
Допиши тестовый класс ParamTest: он проверяет, что будет, если ввести разные цвета.
Проверь варианты, когда пользователь вводит «красный», «зелёный», «чёрный», «жёлтый».
Класс Bomb для бомбы уже написан — он хранится в отдельной вкладке рядом с заданием.
 */

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ParamTest {

    private final String color;
    private final String expectedString;

    // Конструктор, принимающий параметры для каждого тестового случая
    public ParamTest(String color, String expectedString) {
        this.color = color;
        this.expectedString = expectedString;
    }

    // Источник данных для параметризированного теста
    @Parameterized.Parameters
    public static Collection<Object[]> dataForTest() {
        return Arrays.asList(new Object[][]{
                {"красный", "Произошёл взрыв!"},
                {"зелёный", "Фух! Осталось обрезать ещё один провод."},
                {"чёрный", "Отлично! Бомба почти обезврежена!"},
                {"жёлтый", "Ты не можешь обрезать провод, которого нет!"}
        });
    }

    @Test
    @DisplayName("Проверка обрезания проводов для обезвреживания бомбы")
    @Description("Тест проверяет реакцию бомбы на обрезание проводов разных цветов")
    public void paramTest() {
        Bomb bomb = new Bomb();
        verifyBombResponse(bomb);
    }

    @Step("Обрезаем провод цвета {color} и проверяем реакцию бомбы")
    private void verifyBombResponse(Bomb bomb) {
        String actualResponse = bomb.cutTheWire(color);
        Assert.assertEquals(expectedString, actualResponse);
        System.out.println("Аккуратно режь " + color + " провод. " + expectedString);
    }
}

