package ru.practicum.yandex;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CourierCreateTest {
    Courier courier;
    Model model;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        courier = new Courier("loginForTest1", "testPassword1", "testFirstName");
        model = new Model();
    }

    @Test
    @DisplayName("Курьер создается")
    @Description("Убедиться, что запрос создания курьера возвращает 201 и значение в теле соответствует ожидаемому")
    public void createCourierSuccess() {
        Response response = model.sendPostRequest(courier, model.getPathCreateCourier());
        model.compareStatusCode(response, 201);
        model.compareResponseBodyTrue(response, "ok");
    }
    @Test
    @DisplayName("Курьер создается без заполненного имени")
    @Description("Убедиться, что если при создании курьера не указали имя, то курьер все равно создается")
    public void createCourierWithoutFirstName() {
        courier.setFirstName("");
        Response response = model.sendPostRequest(courier, model.getPathCreateCourier());
        model.compareStatusCode(response, 201);
        model.compareResponseBodyTrue(response, "ok");
    }
    @Test
    @DisplayName("Ошибка при создании курьера без пароля")
    @Description("Убедиться, что если при создании курьера не указали пароль, то получим ошибку")
    public void getErrorCreateWithoutPassword() {
        courier.setPassword("");
        Response response = model.sendPostRequest(courier, model.getPathCreateCourier());
        model.compareStatusCode(response, 400);
        model.compareErrorMessageCreateCourierWithoutField(response, "message");
    }
    @Test
    @DisplayName("Ошибка при создании курьера без логина")
    @Description("Убедиться, что если при создании курьера не указали логин, то получим ошибку")
    public void getErrorCreateWithoutLogin() {
        courier.setLogin("");
        Response response = model.sendPostRequest(courier, model.getPathCreateCourier());
        model.compareStatusCode(response, 400);
        model.compareErrorMessageCreateCourierWithoutField(response, "message");
    }
    @Test
    @DisplayName("Ошибка при создании двух одинаковых курьеров")
    @Description("Убедиться, что при создании курьера, который уже существует, получаем ошибку")
    public void createCourierAlreadyExists() {
        model.sendPostRequest(courier, model.getPathCreateCourier());
        Response response = model.sendPostRequest(courier, model.getPathCreateCourier());
        model.compareStatusCode(response, 409);
        model.compareErrorMessageDoubleCreateCourier(response, "message");
    }

    @After()
    public void tearDown() {
        model.deleteCourier(courier);
    }
}
