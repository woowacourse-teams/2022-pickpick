package com.pickpick.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner implements InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;
    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        try (Session session = entityManager.unwrap(Session.class)) {
            session.doWork(this::extractTableNames);
        }
    }

    private void extractTableNames(Connection connection) throws SQLException {
        List<String> tableNames = new ArrayList<>();

        ResultSet tables = connection
                .getMetaData()
                .getTables(connection.getCatalog(), "PUBLIC", "%", new String[]{"TABLE"});

        try (tables) {
            while (tables.next()) {
                tableNames.add(tables.getString("table_name"));
            }

            this.tableNames = tableNames;
        }
    }

    public void clear() {
        try (Session session = entityManager.unwrap(Session.class)) {
            session.doWork(this::cleanUpDatabase);
        }
    }

    private void cleanUpDatabase(Connection conn) throws SQLException {
        try (Statement statement = conn.createStatement()) {

            statement.executeUpdate("SET REFERENTIAL_INTEGRITY FALSE");

            for (String tableName : tableNames) {

                statement.executeUpdate("TRUNCATE TABLE " + tableName);
                statement
                        .executeUpdate("ALTER TABLE " + tableName + " ALTER COLUMN id RESTART WITH 1");
            }

            statement.executeUpdate("SET REFERENTIAL_INTEGRITY TRUE");
        }
    }
}
