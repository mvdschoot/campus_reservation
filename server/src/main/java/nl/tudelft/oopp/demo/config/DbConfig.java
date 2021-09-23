package nl.tudelft.oopp.demo.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableJpaRepositories("nl.tudelft.oopp.demo.repositories")
@PropertySource("classpath:application-dev.properties")
@EnableTransactionManagement
public class DbConfig {

    @Autowired
    private Environment environment;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Set up the connection to the database.
     */
    @Bean
    public DataSource dataSource() {
        try {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(environment.getProperty("jdbc.driverClassName"));
            dataSource.setUrl(environment.getProperty("jdbc.url"));
            dataSource.setUsername(environment.getProperty("jdbc.user"));
            dataSource.setPassword(environment.getProperty("jdbc.pass"));

            logger.info("Database: credentials configured");

            return dataSource;
        } catch (Exception e) {
            logger.error("Database: error while configuring credentials", e);
        }
        return null;
    }

}
