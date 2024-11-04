/*Ты проверишь регистрацию и авторизацию в сервисе Mesto.
Должен получиться такой сценарий:
- Зарегистрироваться в Mesto.
- Авторизоваться с теми же параметрами.
- Попробовать зарегистрироваться с теми же параметрами ещё раз.
- Это один большой тест, но ты будешь писать его постепенно — по одному запросу за раз.
- Сначала проверишь регистрацию, потом добавишь авторизацию и попытку регистрации с данными,
которые уже существуют.
- У тебя есть две ручки типа POST. Для регистрации — /signup.
Для авторизации — /signin. В теле обоих запросов нужно передать такой JSON:

        {
        "email": "какой-то email",
        "password": "password"
        }
Помимо вызовов, нужно проверить:
- Статус ответа при успешной регистрации — 201.
- Статус ответа при успешной авторизации — 200.
- При успешной авторизации в теле ответа приходит токен token. Он не пустой.
- При попытке регистрации с почтой, которая уже есть, статус ответа — 409.
Обрати внимание: нужно сделать так, чтобы у каждого нового пользователя
была уникальная почта, иначе тест будет падать. Поможет генерация случайного числа.
Посмотри, как это делается:

        Random random = new Random();
        String email = "something" + random.nextInt(10000000) + "@yandex.ru";
        В строке email получится something32423435@yandex.ru.
        Это значение с высокой вероятностью будет разным при каждом запуске.
- Напиши первый запрос — он проверяет регистрацию.
Остальные запросы ты напишешь в следующих заданиях:
- Напиши второй запрос: авторизация с адресом и паролем, которые только что зарегистрированы.
- Напиши третий запрос: попытка зарегистрироваться с адресом, который уже есть.
 */

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.util.Random;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class Registration {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-mesto.praktikum-services.ru";
    }

    @Test
    @Description("Тест регистрации и авторизации пользователя с последующей проверкой уникальности email")
    public void registrationAndAuth() {
        // Составляем уникальный email
        String email = generateUniqueEmail();

        // Составляем JSON для тела запроса
        String json = createRequestBody(email, "aaa");

        // Выполняем регистрацию
        registerUser(json);

        // Выполняем авторизацию
        authorizeUser(json);

        // Проверяем повторную регистрацию с тем же email
        checkDuplicateRegistration(json);
    }

    @Step("Генерация уникального email")
    private String generateUniqueEmail() {
        Random random = new Random();
        return "something" + random.nextInt(10000000) + "@yandex.ru";
    }

    @Step("Создание тела запроса с email: {email} и password")
    private String createRequestBody(String email, String password) {
        return "{\"email\": \"" + email + "\", \"password\": \"" + password + "\" }";
    }

    @Step("Регистрация нового пользователя")
    private void registerUser(String json) {
        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/signup")
                .then()
                .statusCode(201);
    }

    @Step("Авторизация пользователя")
    private void authorizeUser(String json) {
        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/signin");

        response.then().assertThat()
                .body("token", notNullValue())
                .and()
                .statusCode(200);
    }

    @Step("Проверка повторной регистрации с уже существующим email")
    private void checkDuplicateRegistration(String json) {
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/signup")
                .then()
                .statusCode(409);
    }
}
