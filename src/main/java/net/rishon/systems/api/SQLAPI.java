package net.rishon.systems.api;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;

import java.sql.Connection;

@Data
public class SQLAPI {

    private final HikariDataSource databasePool;
    private HikariConfig databaseConfig = new HikariConfig();
    private Connection connection;

    public SQLAPI(String host, String username, String password, String database, int port) {
        this.databaseConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        this.databaseConfig.setUsername(username);
        this.databaseConfig.setPassword(password);
        this.databasePool = new HikariDataSource(databaseConfig);
    }

    public void createUsersTable() {
        // TO:DO
    }

    public Connection getConnection() {
        try {
            synchronized (this) {
                if (connection != null && !connection.isClosed()) {
                    return connection;
                } else {
                    connection = this.databasePool.getConnection();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
