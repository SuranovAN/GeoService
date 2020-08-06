package SenderTests;

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
import java.util.Map;

public class MessageSenderTest {
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
    GeoServiceImpl geoServiceImlMock;

    @Mock
    LocalizationServiceImpl localizationServiceImplMock;

    @Test
    void messageSenderImplTestRussianIP(){
        GeoServiceImpl geoServiceImpl = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoServiceImpl.byIp("172."))
                .thenReturn(new Location("Moscow", Country.RUSSIA, null, 0));

        LocalizationServiceImpl localizationServiceImpl = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(localizationServiceImpl.locale(Country.RUSSIA)).thenReturn("Добро пожаловать");

        MessageSender messageSender = new MessageSenderImpl(geoServiceImpl, localizationServiceImpl);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.");
        Assertions.assertEquals("Добро пожаловать", messageSender.send(headers));

    }

    @Test
    void messageSenderImplTestAmericanIP(){
        GeoService geoServiceImpl = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoServiceImpl.byIp("96."))
                .thenReturn(new Location("New York", Country.USA, null,  0));

        LocalizationService localizationServiceImpl = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(localizationServiceImpl.locale(Country.USA)).thenReturn("Welcome");

        Map<String, String> headers = new HashMap<String, String>();
        MessageSender messageSender = new MessageSenderImpl(geoServiceImpl, localizationServiceImpl);
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "96.");
        Assertions.assertEquals("Welcome", messageSender.send(headers));
    }
}
