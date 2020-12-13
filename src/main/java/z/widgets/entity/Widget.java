package z.widgets.entity;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

public class Widget {
    private UUID id;

    private long X;

    private long Y;

    private long Z;

    private double width;

    private double height;

    private ZonedDateTime lastChangesDate;

    public UUID getId() {
        return id;
    }

    public Widget setId(UUID id) {
        this.id = id;
        return this;
    }

    public long getX() {
        return X;
    }

    public Widget setX(long x) {
        this.X = x;
        return this;
    }

    public long getY() {
        return Y;
    }

    public Widget setY(long y) {
        this.Y = y;
        return this;
    }

    public long getZ() {
        return Z;
    }

    public Widget setZ(long z) {
        this.Z = z;
        return this;
    }

    public double getWidth() {
        return width;
    }

    public Widget setWidth(double width) {
        this.width = width;
        return this;
    }

    public double getHeight() {
        return height;
    }

    public Widget setHeight(double height) {
        this.height = height;
        return this;
    }

    public ZonedDateTime getLastChangesDate() {
        return lastChangesDate;
    }

    public Widget setLastChangesDate(ZonedDateTime lastChangesDate) {
        this.lastChangesDate = lastChangesDate;
        return this;
    }

    // Z is unique, so this should be enough
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Widget widget = (Widget) o;
        return Z == widget.Z;
    }

    // Z is unique, so this should be enough
    @Override
    public int hashCode() {
        return Objects.hash(Z);
    }

    @Override
    public String toString() {
        return "Widget{" +
                "id=" + id +
                ", X=" + X +
                ", Y=" + Y +
                ", Z=" + Z +
                ", width=" + width +
                ", height=" + height +
                ", lastChangesDate=" + lastChangesDate +
                '}';
    }
}
