package z.widgets.service;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;


import org.springframework.stereotype.Service;

import z.widgets.entity.request.CreateWidgetRequest;
import z.widgets.entity.request.ModifyWidgetRequest;
import z.widgets.entity.Widget;
import z.widgets.repository.WidgetRepository;

@Service
public class WidgetServiceImpl implements WidgetService {
    private final WidgetRepository repository;
    private static final Comparator<Widget> zCoordinateComparator = Comparator.comparingLong(Widget::getZ);

    public WidgetServiceImpl(WidgetRepository repository) {this.repository = repository;}

    @Override
    public Widget create(CreateWidgetRequest request) {
        Widget widget = new Widget()
                .setId(UUID.randomUUID())
                .setX(request.getX())
                .setY(request.getY())
                .setWidth(request.getWidth())
                .setHeight(request.getHeight())
                .setLastChangesDate(ZonedDateTime.now());

        repository.lock();
        try {
            widget.setZ(request.getZ() == null ? getTopZ() : request.getZ());
            SortedMap<Long, Widget> tail = getAllOrderedByZ().tailMap(widget.getZ());
            reorderZ(tail.keySet(), widget);

            repository.save(widget);
        } finally {
            repository.unlock();
        }
        return widget;
    }

    @Override
    public Widget modify(ModifyWidgetRequest request) {
        Widget widget = repository.get(request.getId());
        setChangedFields(widget, request);

        repository.lock();
        try {
            SortedMap<Long, Widget> tail  = getAllOrderedByZ().tailMap(widget.getZ());
            reorderZ(tail.keySet(), widget);

            repository.save(widget);
        } finally {
            repository.unlock();
        }
        return widget;
    }

    @Override
    public void delete(UUID uuid) {
        repository.lock();
        try {
            repository.delete(uuid);
        } finally {
            repository.unlock();
        }
    }

    @Override
    public Widget get(UUID uuid) {
        return repository.get(uuid);
    }

    @Override
    public List<Widget> getAll() {
        return new ArrayList<>(repository.getAllZSorted().values());
    }

    private void reorderZ(Set<Long> zTail, Widget specifiedWidget) {
        if (zTail.size() == 0) {
            return;
        }

        long previousZ = specifiedWidget.getZ();
        for (Long z : zTail) {
            Widget widget = repository.getByZ(z);
            if (Objects.equals(specifiedWidget.getId(), repository.getByZ(z).getId())) {
                continue;
            }
            if (previousZ < z) {
                return;
            }
            previousZ = z + 1;
            widget.setZ(z + 1);
            widget.setLastChangesDate(ZonedDateTime.now());
            repository.save(widget);
        }
    }

    private long getTopZ() {
        if (repository. getAllZSorted().isEmpty()) {
            return 0;
        }

        Widget maxZWidget = repository.getAllZSorted().lastEntry().getValue();
        return maxZWidget.getZ() + 1;
    }


    private ConcurrentSkipListMap<Long, Widget> getAllOrderedByZ() {
        return repository.getAllZSorted();
    }

    private void setChangedFields(Widget widget, ModifyWidgetRequest request) {
        if (Objects.nonNull(request.getCoordinateX())) {
            widget.setX(request.getCoordinateX());
        }
        if (Objects.nonNull(request.getCoordinateY())) {
            widget.setY(request.getCoordinateY());
        }
        if (Objects.nonNull(request.getCoordinateZ())) {
            widget.setZ(request.getCoordinateZ());
        }
        if (Objects.nonNull(request.getWidth())) {
            widget.setWidth(request.getWidth());
        }
        if (Objects.nonNull(request.getHeight())) {
            widget.setHeight(request.getHeight());
        }

        widget.setLastChangesDate(ZonedDateTime.now());
    }
}
