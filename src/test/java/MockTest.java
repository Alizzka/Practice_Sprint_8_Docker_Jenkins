/*
1. Тебе нужно написать автотест для единственного в мире малыша Котопса.
Проверь: методы createHalfCat() и createHalfDog() вызываются по одному разу. Используй моки.
Тебе понадобится класс кота, пса и Котопса: они хранятся во вкладках рядом с заданием.

2. Теперь добавь к тесту стабы. Они должны возвращать половину кота и пса, когда ты вызываешь соотвествующие методы с параметрами:
половина кота — две лапы, говорит: «Я самый умный»;
половина пса — две лапы, говорит: «Я самый весёлый».
Затем проверь, что у Котопса получилось четыре лапы и он говорит: «Единственный в мире малыш Котопёс».
 */

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MockTest {

    @Mock
    private Cat cat;
    @Mock
    private Dog dog;

    @Test
    @DisplayName("Проверка вызова методов создания половинных объектов")
    @Description("Тест проверяет, что методы createHalfCat и createHalfDog были вызваны один раз")
    public void test() {
        createHalfCatAndDog();
        verifyCatDogMethodsCalledOnce();
    }

    @Test
    @DisplayName("Проверка создания объекта CatDog")
    @Description("Тест проверяет, что CatDog создается корректно из половины кота и половины пса")
    public void test2() {
        setCatDogStubs();
        Cat halfCat = cat.createHalfCat();
        Dog halfDog = dog.createHalfDog();

        CatDog catDog = createCatDog(halfCat, halfDog);
        verifyCatDog(catDog);
    }

    @Step("Создаем половину кота и половину пса")
    private void createHalfCatAndDog() {
        cat.createHalfCat();
        dog.createHalfDog();
    }

    @Step("Проверка, что методы createHalfCat и createHalfDog были вызваны один раз")
    private void verifyCatDogMethodsCalledOnce() {
        Mockito.verify(cat, Mockito.times(1)).createHalfCat();
        Mockito.verify(dog, Mockito.times(1)).createHalfDog();
    }

    @Step("Задаем стаб для половины кота и половины пса")
    private void setCatDogStubs() {
        Mockito.when(cat.createHalfCat()).thenReturn(new Cat(2, "Я самый умный"));
        Mockito.when(dog.createHalfDog()).thenReturn(new Dog(2, "Я самый весёлый"));
    }

    @Step("Создаем объект CatDog из половины кота и половины пса")
    private CatDog createCatDog(Cat halfCat, Dog halfDog) {
        return new CatDog(halfCat, halfDog);
    }

    @Step("Проверяем свойства объекта CatDog")
    private void verifyCatDog(CatDog catDog) {
        Assert.assertEquals(4, catDog.getLegsCount());
        Assert.assertEquals("Единственный в мире малыш Котопёс", catDog.getSound());
    }
}

