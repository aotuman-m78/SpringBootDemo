import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by fangrui on 2018/2/7.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.example.web", "com.example.service"})
@ServletComponentScan(value = {})
@EnableAutoConfiguration
//@MapperScan("cn.com.weidai.wow.dal.mapper")
//@ImportResource(locations = {"classpath:dubbo_config.xml","classpath:spring_bean.xml"})
public class AppStart {

    public static void main(String[] args) {
        SpringApplication.run(AppStart.class,args);
    }
}
