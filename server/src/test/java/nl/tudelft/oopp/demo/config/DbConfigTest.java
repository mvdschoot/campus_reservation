package nl.tudelft.oopp.demo.config;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.opentest4j.TestAbortedException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@SpringBootTest
class DbConfigTest {

    @Mock
    private Environment environment;

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection mockConnection;

    @Mock
    private Statement mockStatement;

    /**
     * Test for dataSource method.
     */
    @Test
    void dataSourceTest() throws Exception {
        when(dataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockConnection.createStatement().executeUpdate(any())).thenReturn(1);

        verify(mockConnection, times(1)).createStatement();

        when(mockConnection.createStatement().executeUpdate(any())).thenThrow(new TestAbortedException());
        verify(mockConnection, times(2)).createStatement();
    }

    @Test
    void exceptionTest() {
        final DbConfig config = new DbConfig();

        when(environment.getProperty("jdbc.driverClassName")).thenReturn("com.mysql.jdbc.Driver");
        when(environment.getProperty("jdbc.url")).thenReturn("jdbc:mysql://oopp38.c2jruxzjdhjz.eu-"
                + "west-3.rds.amazonaws.com/OOPP");
        when(environment.getProperty("jdbc.user")).thenReturn("HELLO");
        when(environment.getProperty("jdbc.pass")).thenReturn("HELLOpass");
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://oopp38.c2jruxzjdhjz.eu-west-3.rds.amazonaws.com/OOPP");
        ds.setUsername("HELLO");
        ds.setPassword("HELLO");
        assertNull(config.dataSource());
    }
}
