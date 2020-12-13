package z.widgets.entity.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CreateWidgetRequest {

    @NotNull
    private Long X;
    @NotNull
    private Long Y;

    private Long Z;

    @Positive
    @NotNull
    private Double width;

    @Positive
    @NotNull
    private Double height;

    public CreateWidgetRequest(@NotNull Long coordinateX, @NotNull Long coordinateY, Long coordinateZ,
            @Positive @NotNull Double width, @Positive @NotNull Double height) {
        this.X = coordinateX;
        this.Y = coordinateY;
        this.Z = coordinateZ;
        this.width = width;
        this.height = height;
    }

    public Long getX() {
        return X;
    }

    public void setX(Long x) {
        this.X = x;
    }

    public Long getY() {
        return Y;
    }

    public void setY(Long y) {
        this.Y = y;
    }

    public Long getZ() {
        return Z;
    }

    public void setZ(Long z) {
        this.Z = z;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "CreateWidgetRequest{" +
                "coordinateX=" + X +
                ", coordinateY=" + Y +
                ", coordinateZ=" + Z +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
