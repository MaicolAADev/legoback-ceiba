package sura.pruebalegoback.test;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import sura.pruebalegoback.MainApplication;

public class MainApplicationTest {

    @Test
    public void shouldStartApp() {
        try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run(MainApplication.class, new String[] {}))
                    .thenReturn(Mockito.mock(ConfigurableApplicationContext.class));

            MainApplication.main(new String[] {});

            mocked.verify(() -> SpringApplication.run(MainApplication.class, new String[] {}));
        }
    }
}
