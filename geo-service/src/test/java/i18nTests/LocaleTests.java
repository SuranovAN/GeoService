package i18nTests;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;

public class LocaleTests {
    private static long startTests;
    private long startTestTime;

    @BeforeAll
    static void init() {
        System.out.println("Starting Tests");
        startTests = System.currentTimeMillis();
    }

    @BeforeEach
    void initTest() {
        System.out.println("Start new test");
        startTestTime = System.currentTimeMillis();
    }

    @AfterEach
    void completeTest() {
        System.out.println("\nTest complete " + (System.currentTimeMillis() - startTestTime) + " milliseconds");
    }

    @AfterAll
    static void completeTests() {
        System.out.println("Tests complete " + (System.currentTimeMillis() - startTests) + " milliseconds");
    }

    @Mock
    GeoService geoService = Mockito.mock(GeoServiceImpl.class);
    LocalizationService localizationService = Mockito.mock(LocalizationServiceImpl.class);

    @Test
    void testText(){
        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        Location location = new Location(null, null, null, 0);

        Mockito.when(geoService.byIp(Mockito.anyString())).thenReturn(location);
        Mockito.when(localizationService.locale(Country.RUSSIA)).thenReturn("Добро пожаловать");
        Mockito.when(localizationService.locale(Country.USA)).thenReturn("Welcome");

        messageSender.send(new HashMap<String, String>(){{put(MessageSenderImpl.IP_ADDRESS_HEADER, "127.0.0.1");}});
        Assertions.assertEquals("Добро пожаловать", localizationService.locale(Country.RUSSIA));
        Assertions.assertEquals("Welcome", localizationService.locale(Country.USA));
    }
}