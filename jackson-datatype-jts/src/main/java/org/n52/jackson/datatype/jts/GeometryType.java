package org.n52.jackson.datatype.jts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.locationtech.jts.geom.*;

import java.util.Optional;

public enum GeometryType {
    /**
     * Geometries of type {@link Point}.
     */
    POINT("Point"),
    /**
     * Geometries of type {@link LineString}.
     */
    LINE_STRING("LineString"),
    /**
     * Geometries of type {@link Polygon}.
     */
    POLYGON("Polygon"),
    /**
     * Geometries of type {@link MultiPoint}.
     */
    MULTI_POINT("MultiPoint"),
    /**
     * Geometries of type {@link MultiLineString}.
     */
    MULTI_LINE_STRING("MultiLineString"),
    /**
     * Geometries of type {@link MultiPolygon}.
     */
    MULTI_POLYGON("MultiPolygon"),
    /**
     * Geometries of type {@link GeometryCollection}.
     */
    GEOMETRY_COLLECTION("GeometryCollection");

    private final String type;

    /**
     * Create a new {@link GeometryType}.
     *
     * @param type The string type.
     */
    GeometryType(String type) {
        this.type = type;
    }

    /**
     * The bit mask value of this {@link GeometryType}.
     *
     * @return The bit mask.
     */
    int mask() {
        return 1 << this.ordinal();
    }

    @Override
    @JsonValue
    public String toString() {
        return this.type;
    }

    /**
     * Get the geometry type from the supplied GeoJSON type value.
     *
     * @param value The GeoJSON type.
     * @return The {@link GeometryType}
     */
    @JsonCreator
    public static Optional<GeometryType> fromString(String value) {
        for (GeometryType type : GeometryType.values()) {
            if (type.toString().equals(value)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    /**
     * Get the geometry type from the supplied {@link Geometry}.
     *
     * @param geometry The {@link Geometry}.
     * @return The {@link GeometryType}.
     */
    public static Optional<GeometryType> forGeometry(Geometry geometry) {
        if (geometry == null) {
            return Optional.empty();
        } else if (geometry instanceof Polygon) {
            return Optional.of(POLYGON);
        } else if (geometry instanceof Point) {
            return Optional.of(POINT);
        } else if (geometry instanceof MultiPoint) {
            return Optional.of(MULTI_POINT);
        } else if (geometry instanceof MultiPolygon) {
            return Optional.of(MULTI_POLYGON);
        } else if (geometry instanceof LineString) {
            return Optional.of(LINE_STRING);
        } else if (geometry instanceof MultiLineString) {
            return Optional.of(MULTI_LINE_STRING);
        } else if (geometry instanceof GeometryCollection) {
            return Optional.of(GEOMETRY_COLLECTION);
        } else {
            return Optional.empty();
        }
    }

}
