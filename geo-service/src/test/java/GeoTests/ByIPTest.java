package GeoTests;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;
import java.util.HashMap;

public class ByIPTest {
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

    @Captor
    ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

    @Test
    void testLocation(){
        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        Location location = new Location(null, null, null, 0);

        Mockito.when(geoService.byIp(Mockito.anyString())).thenReturn(location);
        messageSender.send(new HashMap<String, String>(){{put(MessageSenderImpl.IP_ADDRESS_HEADER, "127.0.0.1");}});

        Mockito.verify(geoService).byIp(argumentCaptor.capture());
        Assertions.assertEquals("127.0.0.1", argumentCaptor.getValue());
        Mockito.verify(geoService, Mockito.times(1)).byIp(GeoServiceImpl.LOCALHOST);
        Assertions.assertEquals(geoService.byIp(MessageSenderImpl.IP_ADDRESS_HEADER), location);
//        Location location2 = new Location("Moscow", Country.RUSSIA, null, 0);
//        Assertions.assertEquals(location.getCountry(), geoService.byIp(GeoServiceImpl.LOCALHOST).getCountry());
//        Assertions.assertEquals(location2.getCountry(), geoService.byIp(GeoServiceImpl.MOSCOW_IP).getCountry());
    }
}

