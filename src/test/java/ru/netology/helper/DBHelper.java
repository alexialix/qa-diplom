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
    private static final String url = System.getProperty("urlDB");
    private static final String user = System.getProperty("userDB");
    private static final String password = System.getProperty("passwordDB");


    @SneakyThrows
    public static void runSQL() {
        request = new QueryRunner();
        conn = DriverManager.getConnection(url, user, password);
    }

    @SneakyThrows
    public static Order getOrderEntityData(){
        runSQL();
        var codeSQL = "SELECT * FROM order_entity ORDER BY created DESC LIMIT 1;";
        return request.query(conn,codeSQL, new BeanHandler<>(Order.class));
    }
    @SneakyThrows
    public static String getCreditStatus(String paymentId) {
        runSQL();
        var data = "SELECT status FROM credit_request_entity WHERE bank_id = ?";
        return request.query(conn, data, new ScalarHandler<>(), paymentId);
    }

    @SneakyThrows
    public static String getStatus(String paymentId) {
        runSQL();
        var data = "SELECT status FROM payment_entity WHERE transaction_id = ?";
        return request.query(conn, data, new ScalarHandler<>(), paymentId);
    }

}