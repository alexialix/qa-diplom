package ru.netology.helper;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/app?useSSL=false&serverTimezone=UTC";
    private static final String USER = System.getProperty("userDB", "root");
    private static final String PASSWORD = System.getProperty("passwordDB", "123");

    private static Connection conn;
    private static final QueryRunner request = new QueryRunner();

    private static void connect() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
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

    public static PaymentEntity getPaymentEntityData() {
        connect();
        String sql = "SELECT * FROM payment_entity ORDER BY created DESC LIMIT 1;";
        try {
            return request.query(conn, sql, new BeanHandler<>(PaymentEntity.class));
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при выполнении запроса getPaymentEntityData", e);
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

    public static String getCreatedDateFromPayment(String created) {
        connect();
        String sql = "SELECT created FROM payment_entity WHERE created = ?";
        try {
            return request.query(conn, sql, new ScalarHandler<>(), created);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при выполнении запроса getCreatedDateFromPayment", e);
        }
    }

    public static String getCreatedDateFromCredit(String created) {
        connect();
        String sql = "SELECT created FROM credit_request_entity WHERE created = ?";
        try {
            return request.query(conn, sql, new ScalarHandler<>(), created);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при выполнении запроса getCreatedDateFromCredit", e);
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
