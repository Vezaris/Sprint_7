package ru.practicum.yandex;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierLoginTest {
    Courier courier;
    Model model;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        courier = new Courier("loginForTest1", "testPassword1", "testFirstName");
        model = new Model();
    }

    @Test
    @DisplayName("Курьер успешно авторизуется")
    @Description("Убедиться, что созданный курьер может успешно авторизоваться")
    public void authCourierSuccess() {
        model.sendPostRequest(courier, model.getPathCreateCourier());
        Response response = model.sendPostRequest(courier, model.getPathLoginCourier());
        model.compareStatusCode(response, 200);
        response.then().assertThat().body("id", notNullValue());
    }
    @Test
    @DisplayName("Курьер не может авторизоваться c пустым логином")
    @Description("Убедиться, что если при авторизации указать пустой логин, то запрос вернет ошибку")
    public void getErrorCourierLoginWithoutLogin() {
        String login = courier.getLogin();
        model.sendPostRequest(courier, model.getPathCreateCourier());
        courier.setLogin("");
        Response response = model.sendPostRequest(courier, model.getPathLoginCourier());
        model.compareStatusCode(response, 400);
        model.compareErrorMessageLoginWithoutField(response, "message");
        courier.setLogin(login);
    }
    @Test
    @DisplayName("Курьер не может авторизоваться с пустым паролем")
    @Description("Убедиться, что если при авторизации указать пустой пароль, то запрос вернет ошибку")
    public void getErrorCourierLoginWithoutPassword() {
        String password = courier.getPassword();
        model.sendPostRequest(courier, model.getPathCreateCourier());
        courier.setPassword("");
        Response response = model.sendPostRequest(courier, model.getPathLoginCourier());
        model.compareStatusCode(response, 400);
        model.compareErrorMessageLoginWithoutField(response, "message");
        courier.setPassword(password);
    }
    @Test
    @DisplayName("Курьер не может авторизоваться при некорректном логине")
    @Description("Убедиться, что если при авторизации указать не верный логин, то получим ошибку")
    public void getErrorCourierLoginWithIncorrectLogin() {
        String login = courier.getLogin();
        model.sendPostRequest(courier, model.getPathCreateCourier());
        courier.setLogin(courier.getLogin() + "qwe");
        Response response = model.sendPostRequest(courier, model.getPathLoginCourier());
        model.compareStatusCode(response, 404);
        model.compareErrorMessageLoginIncorrectField(response, "message");
        courier.setLogin(login);
    }
    @Test
    @DisplayName("Курьер не может авторизоваться при некорректном пароле")
    @Description("Убедиться, что если при авторизации указать не верный пароль, то получим ошибку")
    public void getErrorCourierLoginWithIncorrectPassword() {
        String password = courier.getPassword();
        model.sendPostRequest(courier, model.getPathCreateCourier());
        courier.setPassword(courier.getPassword() + "qwe");
        Response response = model.sendPostRequest(courier, model.getPathLoginCourier());
        model.compareStatusCode(response, 404);
        model.compareErrorMessageLoginIncorrectField(response, "message");
        courier.setPassword(password);
    }

    @After()
    public void tearDown() {
        model.deleteCourier(courier);
    }
}
