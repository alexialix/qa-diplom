package ru.netology.helper;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
    private static Connection conn;
    private static final QueryRunner request = new QueryRunner();

    private static void connect() {
        if (conn == null) {
            try {
                String url = System.getProperty("urlDB");
                String user = System.getProperty("userDB");
                String password = System.getProperty("passwordDB");

                if (url == null || user == null || password == null) {
                    throw new IllegalStateException("Не заданы параметры подключения к БД");
                }

                conn = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Ошибка при подключении к БД: " + e.getMessage(), e);
            }
        }
    }

    public static OrderEntity getOrderEntityData() {
        connect();
        String sql = "SELECT * FROM order_entity ORDER BY created DESC LIMIT 1;";
        try {
            return request.query(conn, sql, new BeanHandler<>(OrderEntity.class));
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при выполнении запроса getOrderEntityData", e);
        }
    }

    public static String getStatus(String paymentId) {
        connect();
        String sql = "SELECT status FROM payment_entity WHERE transaction_id = ?";
        try {
            return request.query(conn, sql, new ScalarHandler<>(), paymentId);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при выполнении запроса getStatus", e);
        }
    }

    public static String getPaymentId(String paymentId) {
        connect();
        String sql = "SELECT transaction_id FROM payment_entity WHERE transaction_id = ?";
        try {
            return request.query(conn, sql, new ScalarHandler<>(), paymentId);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при выполнении запроса getPaymentId", e);
        }
    }

    public static String getCreditId(String creditId) {
        connect();
        String sql = "SELECT bank_id FROM credit_request_entity WHERE bank_id = ?";
        try {
            return request.query(conn, sql, new ScalarHandler<>(), creditId);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при выполнении запроса getCreditId", e);
        }
    }

    public static String getCreditStatus(String status) {
        connect();
        String sql = "SELECT id FROM credit_request_entity WHERE status = ? ORDER BY created DESC LIMIT 1";
        try {
            return request.query(conn, sql, new ScalarHandler<>(), status);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при выполнении запроса getCreditStatus", e);
        }
    }
}
