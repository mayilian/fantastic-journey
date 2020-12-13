package z.widgets.repository;

import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

import z.widgets.entity.Widget;

public interface WidgetRepository {
    void lock();

    void unlock();

    Widget save(Widget widget);

    void delete(UUID uuid);

    Widget get(UUID uuid);

    Widget getByZ(Long uuid);

    ConcurrentSkipListMap<Long, Widget> getAllZSorted();
}
