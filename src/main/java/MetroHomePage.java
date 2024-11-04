/*Тебе предстоит написать несколько автотестов для учебного приложения Яндекс.Метро. Нужно проверить:
- работу переключателя городов;
- отображение времени для построенного маршрута;
- отображение станции «Откуда» в карточке маршрута;
- отображение станции «Куда» в карточке маршрута.
Для тестов понадобится класс Page Object. Начни с него:
- создай класс и внеси туда локаторы и методы, которые будешь использовать.
 */

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class MetroHomePage {
    // Поле с экземпляром веб-драйвера
    private final WebDriver driver;

    // Локатор кнопки выпадающего списка городов по имени класса
    private final By selectCityButton = By.className("select_metro__button");

    // Локатор поля ввода «Откуда» по XPATH, поиск по плейсхолдеру
    private final By fieldFrom = By.xpath(".//input[@placeholder='Откуда']");

    // Локатор поля ввода «Куда» по XPATH, поиск по плейсхолдеру
    private final By fieldTo = By.xpath(".//input[@placeholder='Куда']");

    // Локатор коллекций станций «Откуда» и «Куда» маршрута по имени класса
    private final By routeStationFromTo = By.className("route-details-block__terminal-station");

    // Локатор для отображения времени маршрута
    private final By routeTimes = By.className("route-list-item__time");

    // Конструктор класса MetroHomePage с параметром driver
    public MetroHomePage(WebDriver driver) {
        this.driver = driver;
    }

    // Метод ожидания загрузки страницы: проверяет видимость станции «Театральная»
    public void waitForLoadHomePage() {
        new WebDriverWait(driver, Duration.ofSeconds(8))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[text() = 'Театральная']")));
    }

    // Метод выбора города по переданному названию
    public void chooseCity(String cityName) {
        driver.findElement(selectCityButton).click();
        driver.findElement(By.xpath(String.format("//*[text()='%s']", cityName))).click();
    }

    // Метод ввода названия станции в поле «Откуда»
    public void setStationFrom(String stationFrom) {
        driver.findElement(fieldFrom).sendKeys(stationFrom, Keys.DOWN, Keys.ENTER);
    }

    // Метод ввода названия станции в поле «Куда»
    public void setStationTo(String stationTo) {
        driver.findElement(fieldTo).sendKeys(stationTo, Keys.DOWN, Keys.ENTER);
    }

    // Метод ожидания построения маршрута
    public void waitForLoadRoute() {
        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[text()='Получить ссылку на маршрут']")));
    }

    // Метод построения маршрута
    public void buildRoute(String stationFrom, String stationTo) {
        setStationFrom(stationFrom);
        setStationTo(stationTo);
        waitForLoadRoute();
    }

    // Метод получения имени станции «Откуда» для построенного маршрута
    public String getRouteStationFrom() {
        return driver.findElements(routeStationFromTo).get(0).getText();
    }

    // Метод получения имени станции «Куда» построенного маршрута
    public String getRouteStationTo() {
        return driver.findElements(routeStationFromTo).get(1).getText();
    }

    // Метод получения примерного времени маршрута
    public String getApproximateRouteTime(int routeNumber) {
        List<WebElement> routeTimeElements = driver.findElements(By.className("route-list-item__time"));
        if (routeNumber < 0 || routeNumber >= routeTimeElements.size()) {
            throw new IllegalArgumentException("Invalid route number.");
        }
        return routeTimeElements.get(routeNumber).getText();
    }

    // Метод проверки с ожиданием видимости станции метро
    public void waitForStationVisibility(String stationName) {
        new WebDriverWait(driver, Duration.ofSeconds(8))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format("//*[text()='%s']", stationName))));
    }
}

