package ru.practicum.yandex;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

public class ListOfOrderTest{
    Model model;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        model = new Model();
    }

    @Test
    @DisplayName("Получить список заказов")
    @Description("Убедиться, что список заказов возвращает 200 код ответа")
    public void getListOfOrder() {
        Response response = model.sendGetRequest(model.getPathOrders());
        model.compareStatusCode(response, 200);
    }

}
