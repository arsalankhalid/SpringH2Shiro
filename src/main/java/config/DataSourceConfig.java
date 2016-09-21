package config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import javax.sql.DataSource;


/**
 * Created by arsalan.khalid on 9/19/2016.
 */
@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource(hikariConfig());
        ClassPathResource script = new ClassPathResource("init.sql");
        if(script.exists()) {
            DatabasePopulatorUtils.execute(createDatabasePopulator(script), dataSource);
        }
        return dataSource;
    }

    private DatabasePopulator createDatabasePopulator(ClassPathResource script) {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.setContinueOnError(false);
        databasePopulator.addScript(script);
        return databasePopulator;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public HikariConfig hikariConfig() {
        //TODO: Migrate to properties file, values injected into static vars
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setPoolName("springHikariCP");
        hikariConfig.setJdbcUrl("jdbc:h2:mem:Accenture");
        hikariConfig.setUsername("sa");
        hikariConfig.setPassword("sa");

        return hikariConfig;
    }

}
