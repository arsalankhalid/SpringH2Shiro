import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dao.UserDaoImpl;
import model.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;


/**
 * Created by arsalan.khalid on 9/20/2016.
 */
public class UserDaoTest {
    //TODO test with hikariCP
    private HikariDataSource db;

    @Before
    public void setUp() {
        HikariDataSource dataSource = new HikariDataSource(hikariConfig());
        db=dataSource;
        ClassPathResource script = new ClassPathResource("init.sql");
        //Only exists when developing locally with h2
        if(script.exists()) {
            DatabasePopulatorUtils.execute(createDatabasePopulator(script), dataSource);
        }
    }

    private DatabasePopulator createDatabasePopulator(ClassPathResource script) {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.setContinueOnError(false);
        databasePopulator.addScript(script);
        return databasePopulator;
    }

    private HikariConfig hikariConfig() {
        //TODO: Migrate to properties file, values injected into static vars
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setPoolName("springHikariCP");
        hikariConfig.setJdbcUrl("jdbc:h2:mem:Accenture");
        hikariConfig.setUsername("sa");
        hikariConfig.setPassword("sa");

        return hikariConfig;
    }

    @Test
    public void testFindByname() {
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db);
        UserDaoImpl userDao = new UserDaoImpl();
        userDao.setNamedParameterJdbcTemplate(template);

        User user = userDao.findByName("mkyong");

        Assert.assertNotNull(user);
        Assert.assertEquals(1, user.getId().intValue());
        Assert.assertEquals("mkyong", user.getName());
        System.out.println(user.getName());
        Assert.assertEquals("mkyong@gmail.com", user.getEmail());
    }

    @After
    public void tearDown() {
        db.close();
    }

}
