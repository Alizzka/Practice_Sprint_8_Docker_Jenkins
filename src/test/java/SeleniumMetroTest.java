/*У тебя уже есть Page Object. Его код хранится во вкладке MetroHomePage.java.
Теперь можно приступать к автотестам.
- Во вкладке SeleniumMetroTest.java напиши первый автотест.
Он должен проверять, что переключатель городов работает правильно.
- Напиши второй автотест. Пусть он проверяет, что время для построенного маршрута отображается корректно.
- Напиши третий автотест. Он должен проверить, что в карточке маршрута верно отображается станция «Откуда».
- Остался последний автотест. Нужно убедиться, что в карточке маршрута верно отображается станция «Куда».
 */

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SeleniumMetroTest {

    private WebDriver driver;
    private MetroHomePage metroPage;

    private static final String CITY_SAINTP = "Санкт-Петербург";
    private static final String STATION_SPORTIVNAYA = "Спортивная";
    private static final String STATION_LUBYANKA = "Лубянка";
    private static final String STATION_KRASNOGVARD = "Красногвардейская";

    @Before
    @Step("Открытие браузера и переход на страницу тестового приложения")
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--headless", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        driver.get("https://qa-metro.stand-2.praktikum-services.ru/");
        metroPage = new MetroHomePage(driver);
        metroPage.waitForLoadHomePage();
    }

    @Test
    @Description("Проверка выбора города из выпадающего списка")
    public void checkChooseCityDropdown() {
        chooseCity(CITY_SAINTP);
        verifyStationVisibility(STATION_SPORTIVNAYA);
    }

    @Step("Выбор города {city} из списка")
    private void chooseCity(String city) {
        metroPage.chooseCity(city);
    }

    @Step("Проверка видимости станции метро {station}")
    private void verifyStationVisibility(String station) {
        metroPage.waitForStationVisibility(station);
    }

    @Test
    @Description("Проверка отображения примерного времени поездки для маршрута")
    public void checkRouteApproxTimeIsDisplayed() {
        buildRoute(STATION_LUBYANKA, STATION_KRASNOGVARD);
        verifyApproximateRouteTime(0, "≈ 36 мин.");
    }

    @Step("Построение маршрута от {from} до {to}")
    private void buildRoute(String from, String to) {
        metroPage.buildRoute(from, to);
    }

    @Step("Проверка времени поездки для маршрута")
    private void verifyApproximateRouteTime(int routeIndex, String expectedTime) {
        Assert.assertEquals(expectedTime, metroPage.getApproximateRouteTime(routeIndex));
    }

    @Test
    @Description("Проверка корректности отображения станции 'Откуда' в маршруте")
    public void checkRouteStationFromIsCorrect() {
        buildRoute(STATION_LUBYANKA, STATION_KRASNOGVARD);
        verifyRouteStationFrom(STATION_LUBYANKA);
    }

    @Step("Проверка станции начала маршрута: ожидается {expectedStation}")
    private void verifyRouteStationFrom(String expectedStation) {
        Assert.assertEquals(expectedStation, metroPage.getRouteStationFrom());
    }

    @Test
    @Description("Проверка корректности отображения станции 'Куда' в маршруте")
    public void checkRouteStationToIsCorrect() {
        buildRoute(STATION_LUBYANKA, STATION_KRASNOGVARD);
        verifyRouteStationTo(STATION_KRASNOGVARD);
    }

    @Step("Проверка станции конца маршрута: ожидается {expectedStation}")
    private void verifyRouteStationTo(String expectedStation) {
        Assert.assertEquals(expectedStation, metroPage.getRouteStationTo());
    }

    @After
    @Step("Закрытие браузера")
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Attachment(value = "Скриншот на момент сбоя", type = "image/png")
    public byte[] takeScreenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}

