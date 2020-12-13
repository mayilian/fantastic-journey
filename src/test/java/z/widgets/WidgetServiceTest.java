package z.widgets;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import z.widgets.entity.Widget;
import z.widgets.entity.request.CreateWidgetRequest;
import z.widgets.entity.request.ModifyWidgetRequest;
import z.widgets.exception.WidgetException;
import z.widgets.repository.WidgetInMemoryRepositoryImpl;
import z.widgets.repository.WidgetRepository;
import z.widgets.service.WidgetService;
import z.widgets.service.WidgetServiceImpl;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class WidgetServiceTest {
    @TestConfiguration
    static class RectangleWidgetControllerTestContextConfiguration {

        @Bean
        public WidgetService service() {
            return new WidgetServiceImpl(repository());
        }

        @Bean
        public WidgetRepository repository() {
            return new WidgetInMemoryRepositoryImpl();
        }
    }

    @Autowired
    private WidgetServiceImpl service;

    @Test
    public void create() {
        CreateWidgetRequest request = new CreateWidgetRequest(1L, 1L, 1L, 1d, 1d);
        Widget widget = service.create(request);
        assertNotNull(widget);
    }

    @Test
    public void modify() {
        CreateWidgetRequest request = new CreateWidgetRequest(1L, 1L, 1L, 1d, 1d);
        Widget widget = service.create(request);

        ModifyWidgetRequest modifyRequest = new ModifyWidgetRequest(widget.getId(), 1L, 1L, 2L, 1d, 1d);
        Widget modifiedWidget = service.modify(modifyRequest);

        assertEquals(widget.setZ(2), modifiedWidget);
    }

    @Test
    public void get() {
        CreateWidgetRequest request = new CreateWidgetRequest(1L, 1L, 1L, 1d, 1d);
        Widget widget = service.create(request);

        Widget widgetFromRepository = service.get(widget.getId());

        assertEquals(widget, widgetFromRepository);
    }

    @Test(expected = WidgetException.class)
    public void delete() {
        CreateWidgetRequest request = new CreateWidgetRequest(1L, 1L, 1L, 1d, 1d);
        Widget widget = service.create(request);

        service.delete(widget.getId());

        service.get(widget.getId());
    }

    @Test
    public void createMultiThreaded() throws InterruptedException {
        ExecutorService tp = Executors.newFixedThreadPool(4);
        int expectedNum = 1000;
        CountDownLatch latch = new CountDownLatch(1000);

        for (int i = 0; i < expectedNum; i++) {
            tp.submit(() -> {
                CreateWidgetRequest widget = new CreateWidgetRequest(1L, 1L, 1L, 1D, 1D);
                service.create(widget);
                latch.countDown();
            });
        }

        tp.shutdown();
        tp.awaitTermination(2L, TimeUnit.MINUTES);


        latch.await();
        assertEquals(expectedNum, service.getAll().size());
    }
}
