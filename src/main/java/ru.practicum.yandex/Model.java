package ru.practicum.yandex;
import io.restassured.response.Response;
import io.qameta.allure.Step;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class Model {
    final String PATH_ORDERS = "/api/v1/orders";
    final String PATH_LOGIN_COURIER = "/api/v1/courier/login";
    final String PATH_CREATE_OR_DELETE_COURIER = "/api/v1/courier/";
    final String ERROR_MESSAGE_CREATE_COURIER_WITHOUT_FIELD = "Недостаточно данных для создания учетной записи";
    final String ERROR_MESSAGE_DOUBLE_CREATE_COURIER = "Этот логин уже используется. Попробуйте другой.";
    final String ERROR_MESSAGE_LOGIN_WITHOUT_FIELD = "Недостаточно данных для входа";
    final String ERROR_MESSAGE_LOGIN_INCORRECT_FIELD = "Учетная запись не найдена";


    public String getPathOrders() {
        return PATH_ORDERS;
    }

    public String getPathLoginCourier() {
        return PATH_LOGIN_COURIER;
    }

    public String getPathCreateCourier() {
        return PATH_CREATE_OR_DELETE_COURIER;
    }

    @Step("Отправить GET запрос")
    public Response sendGetRequest(String path) {
        return  given()
                .header("Content-type", "application/json")
                .get(path);
    }
    @Step("Отправить POST запрос")
    public Response sendPostRequest(Object object, String path) {
        return  given()
                .header("Content-type", "application/json")
                .body(object)
                .when()
                .post(path);
    }
    @Step("Отправить DELETE запрос")
    public Response sendDeleteRequest(int id) {
        return
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .delete(PATH_CREATE_OR_DELETE_COURIER + id);
    }
    @Step("Проверить код ответа")
    public void compareStatusCode(Response response, int statusCode) {
        response.then().statusCode(statusCode);
    }
    @Step("Проверить, что в теле ответа получено какое-либо значение")
    public void compareResponseBodyNotNullValue(Response response , String field) {
        response.then().assertThat().body(field, notNullValue());
    }

    @Step("Проверить, что в теле ответа получено true")
    public void compareResponseBodyTrue(Response response , String field) {
        response.then().assertThat().body(field, equalTo(true));
    }
    @Step("Проверить текст ошибки при создании курьера без обязательных полей")
    public void compareErrorMessageCreateCourierWithoutField(Response response , String field) {
        response.then().assertThat().body(field, equalTo(ERROR_MESSAGE_CREATE_COURIER_WITHOUT_FIELD));
    }
    @Step("Проверить текст ошибки при создании курьера, который уже существует")
    public void compareErrorMessageDoubleCreateCourier(Response response , String field) {
        response.then().assertThat().body(field, equalTo(ERROR_MESSAGE_DOUBLE_CREATE_COURIER));
    }
    @Step("Проверить текст ошибки при авторизации без обязательного поля")
    public void compareErrorMessageLoginWithoutField(Response response , String field) {
        response.then().assertThat().body(field, equalTo(ERROR_MESSAGE_LOGIN_WITHOUT_FIELD));
    }
    @Step("Проверить текст ошибки при авторизации с некорректными данными")
    public void compareErrorMessageLoginIncorrectField(Response response , String field) {
        response.then().assertThat().body(field, equalTo(ERROR_MESSAGE_LOGIN_INCORRECT_FIELD));
    }
    @Step("Получить id существующего курьера")
    public int getId(Object object) {
        return sendPostRequest(object, PATH_LOGIN_COURIER).then().extract().path("id");
    }
    @Step("Удалить курьера")
    public void deleteCourier(Object object) {
        try {
            Response response = sendDeleteRequest(getId(object));
            response.then().statusCode(200);
        } catch (NullPointerException exception) {
            System.out.println("Не найден");
        }
    }
}

