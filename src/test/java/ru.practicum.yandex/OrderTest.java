package ru.practicum.yandex;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class OrderTest {
    Model model;
    Order order = new Order();

    public OrderTest(String firstName, String lastName, String address, int metroStation, String phone, String deliveryDate, int rentTime, String[] color, String comment) {
        order.setFirstName(firstName);
        order.setLastName(lastName);
        order.setAddress(address);
        order.setMetroStation(metroStation);
        order.setPhone(phone);
        order.setDeliveryDate(deliveryDate);
        order.setRentTime(rentTime);
        order.setColor(color);
        order.setComment(comment);
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        model = new Model();
    }

    @Parameterized.Parameters
    public static Object[][] getDataEntry() {
        return new Object[][]{
                {"Имя1", "Фамилия1", "Адрес1", 1, "79990001122", "2023-12-21", 1, new String[]{"black"}, "Комментарий1"},
                {"Имя2", "Фамилия2", "Адрес2", 2, "79990003344", "2023-12-22", 2, new String[]{"grey"}, "Комментарий2"},
                {"Имя3", "Фамилия3", "Адрес3", 3, "79990005566", "2023-12-23", 3, new String[]{"grey", "black"}, "Комментарий3"},
                {"Имя4", "Фамилия4", "Адрес4", 4, "79990007788", "2023-12-24", 4, null, "Комментарий4"},
        };
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Проверить, что при создании заказа возвращается 201 код ответа и поле track не пустое")
    public void createOrder() {
        Response response = model.sendPostRequest(order, model.getPathOrders());
        model.compareResponseBodyNotNullValue(response, "track");
        model.compareStatusCode(response, 201);
    }
}
