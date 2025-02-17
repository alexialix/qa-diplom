package ru.netology.helper;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;


public class DBHelper {
    private static QueryRunner request;
    private static Connection conn;
    private static final String url = System.getProperty("urlDB", "jdbc:mysql://localhost:3306/app");
    private static final String user = System.getProperty("userDB");
    private static final String password = System.getProperty("passwordDB");

    @SneakyThrows
    public static void runSQL() {
        request = new QueryRunner();
    }

    @SneakyThrows
    public static OrderEntity getOrderEntityData() {
        runSQL();
        var codeSQL = "SELECT * FROM order_entity ORDER BY created DESC LIMIT 1;";
        return request.query(conn, codeSQL, new BeanHandler<>(OrderEntity.class));
    }

    @SneakyThrows
    public static String getCreditStatus(String payment_id) {
        runSQL();
        var data = "SELECT status FROM credit_request_entity WHERE bank_id = ?";
        return request.query(conn, data, new ScalarHandler<>(), payment_id);
    }

    @SneakyThrows
    public static String getStatus(String payment_id) {
        runSQL();
        var data = "SELECT status FROM payment_entity WHERE transaction_id = ?";
        return request.query(conn, data, new ScalarHandler<>(), payment_id);
    }
    @SneakyThrows
    public static String getPaymentId(String payment_id) {
        runSQL();
        var data = "SELECT transaction_id FROM payment_entity WHERE transaction_id = ?";
        return request.query(conn, data, new ScalarHandler<>(), payment_id);
    }
    @SneakyThrows
    public static String getCreditId(String credit_Id) {
        runSQL();
        var data = "SELECT bank_id FROM credit_request_entity WHERE bank_id = ?";
        return request.query(conn, data, new ScalarHandler<>(), credit_Id);
    }
    @SneakyThrows
    public static String getCreatedDateFromPayment(String created) {
        runSQL();
        var data = "SELECT created FROM payment_entity WHERE created = ?";
        return request.query(conn, data, new ScalarHandler<>(), created);
    }
    @SneakyThrows
    public static String getCreatedDateFromCredit(String created) {
        runSQL();
        var data = "SELECT created FROM credit_request_entity WHERE created = ?";
        return request.query(conn, data, new ScalarHandler<>(), created);
    }
}
