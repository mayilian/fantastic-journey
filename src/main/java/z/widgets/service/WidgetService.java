package z.widgets.service;

import java.util.List;
import java.util.UUID;

import z.widgets.entity.Widget;
import z.widgets.entity.request.CreateWidgetRequest;
import z.widgets.entity.request.ModifyWidgetRequest;

public interface WidgetService {
    Widget create(CreateWidgetRequest request);

    Widget modify(ModifyWidgetRequest request);

    void delete(UUID uuid);

    Widget get(UUID uuid);

    List<Widget> getAll();
}
