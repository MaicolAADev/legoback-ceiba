package sura.pruebalegoback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*VAR_mainapplication_java_enabled_reactive_sura_security_import*/

/*VAR_mainapplication_java_enabled_reactive_sura_security_annotation*/
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
