package z.widgets.repository;

import org.springframework.stereotype.Repository;
import z.widgets.entity.Widget;
import z.widgets.exception.WidgetException;
import z.widgets.exception.WidgetErrorEnum;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantLock;

@Repository
public class WidgetInMemoryRepositoryImpl implements WidgetRepository {
    private final ReentrantLock lock = new ReentrantLock();
    private final ConcurrentSkipListMap<Long, Widget> sortedWidgets = new ConcurrentSkipListMap<>();
    private final Map<UUID, Widget> widgets = new HashMap<>();

    @Override
    public void lock() {
        try {
            this.lock.lockInterruptibly();
        } catch (InterruptedException e) {
            throw new WidgetException(WidgetErrorEnum.UNEXPECTED_ERROR, e);
        }
    }

    @Override
    public void unlock() {
        this.lock.unlock();
    }

    @Override
    public Widget save(Widget widget) {
        widgets.put(widget.getId(), widget);
        sortedWidgets.put(widget.getZ(), widget);
        return widget;
    }

    @Override
    public void delete(UUID uuid) {
        Widget removed = widgets.remove(uuid);
        sortedWidgets.remove(removed.getZ());
    }

    @Override
    public Widget get(UUID uuid) {
        Widget widget = widgets.get(uuid);
        if (widget == null) {
            throw new WidgetException(WidgetErrorEnum.WIDGET_NOT_FOUND, String.format("uuid=%s", uuid));
        }
        return widget;
    }

    @Override
    public Widget getByZ(Long z) {
        Widget widget = sortedWidgets.get(z);
        if (widget == null) {
            throw new WidgetException(WidgetErrorEnum.WIDGET_NOT_FOUND, String.format("z=%s", z));
        }
        return widget;
    }

    @Override
    public ConcurrentSkipListMap<Long, Widget> getAllZSorted() {
        return sortedWidgets;
    }
}
