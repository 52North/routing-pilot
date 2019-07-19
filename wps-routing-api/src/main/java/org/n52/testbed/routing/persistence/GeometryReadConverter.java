package org.n52.testbed.routing.persistence;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.mongodb.DBObject;
import org.bson.BSONObject;
import org.locationtech.jts.geom.*;
import org.n52.jackson.datatype.jts.GeoJsonConstants;
import org.n52.jackson.datatype.jts.GeometryType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.List;
import java.util.Optional;

/**
 * {@link JsonDeserializer} for {@link Geometry}.
 *
 * @author Christian Autermann
 */
@MongoConverter
@ReadingConverter
public class GeometryReadConverter implements Converter<DBObject, Geometry> {
    private final GeometryFactory geometryFactory;

    /**
     * Creates a new {@link GeometryReadConverter}.
     */
    public GeometryReadConverter() {
        this(null);
    }

    /**
     * Creates a new {@link GeometryReadConverter}.
     *
     * @param geometryFactory The {@link GeometryFactory} to use to construct geometries.
     */
    public GeometryReadConverter(GeometryFactory geometryFactory) {
        this.geometryFactory = Optional.ofNullable(geometryFactory)
                .orElseGet(GeometryReadConverter::defaultGeometryFactory);
    }

    @Override
    public Geometry convert(DBObject document) {
        return deserializeGeometry(document);
    }

    private Geometry deserializeGeometry(BSONObject node) {
        String typeName = node.get(GeoJsonConstants.Fields.TYPE).toString();

        GeometryType type = GeometryType.fromString(typeName)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Invalid geometry type: %s", typeName)));

        switch (type) {
            case POINT:
                return deserializePoint(node);
            case MULTI_POINT:
                return deserializeMultiPoint(node);
            case LINE_STRING:
                return deserializeLineString(node);
            case MULTI_LINE_STRING:
                return deserializeMultiLineString(node);
            case POLYGON:
                return deserializePolygon(node);
            case MULTI_POLYGON:
                return deserializeMultiPolygon(node);
            case GEOMETRY_COLLECTION:
                return deserializeGeometryCollection(node);
            default:
                throw new IllegalArgumentException(String.format("Invalid geometry type: %s", typeName));
        }
    }

    private Point deserializePoint(BSONObject node) {
        return this.geometryFactory.createPoint(deserializeCoordinate((List) node.get(GeoJsonConstants.Fields.COORDINATES)));
    }

    private Polygon deserializePolygon(BSONObject node) {
        return deserializeLinearRings((List) node.get(GeoJsonConstants.Fields.COORDINATES));
    }

    private MultiPolygon deserializeMultiPolygon(BSONObject node) {
        List coordinates = (List) node.get(GeoJsonConstants.Fields.COORDINATES);
        Polygon[] polygons = new Polygon[coordinates.size()];
        for (int i = 0; i != coordinates.size(); ++i) {
            polygons[i] = deserializeLinearRings((List) coordinates.get(i));
        }
        return this.geometryFactory.createMultiPolygon(polygons);
    }

    private MultiPoint deserializeMultiPoint(BSONObject node) {
        return this.geometryFactory.createMultiPointFromCoords(deserializeCoordinates((List) node.get(GeoJsonConstants.Fields.COORDINATES)));
    }

    private GeometryCollection deserializeGeometryCollection(BSONObject node) {
        List geometries = (List) node.get(GeoJsonConstants.Fields.GEOMETRIES);
        Geometry[] geom = new Geometry[geometries.size()];
        for (int i = 0; i != geometries.size(); ++i) {
            geom[i] = deserializeGeometry((BSONObject) geometries.get(i));
        }
        return this.geometryFactory.createGeometryCollection(geom);
    }

    private MultiLineString deserializeMultiLineString(BSONObject node) {
        LineString[] lineStrings = lineStringsFromJson((List) node.get(GeoJsonConstants.Fields.COORDINATES));
        return this.geometryFactory.createMultiLineString(lineStrings);
    }

    private LineString[] lineStringsFromJson(List node) {
        LineString[] strings = new LineString[node.size()];
        for (int i = 0; i != node.size(); ++i) {
            Coordinate[] coordinates = deserializeCoordinates((List) node.get(i));
            strings[i] = this.geometryFactory.createLineString(coordinates);
        }
        return strings;
    }

    private LineString deserializeLineString(BSONObject node) {
        return this.geometryFactory.createLineString(deserializeCoordinates((List) node.get(GeoJsonConstants.Fields.COORDINATES)));
    }

    private Coordinate[] deserializeCoordinates(List node) {
        Coordinate[] points = new Coordinate[node.size()];
        for (int i = 0; i != node.size(); ++i) {
            points[i] = deserializeCoordinate((List) node.get(i));
        }
        return points;
    }

    private Polygon deserializeLinearRings(List node) {
        LinearRing shell = deserializeLinearRing((List) node.get(0));
        LinearRing[] holes = new LinearRing[node.size() - 1];
        for (int i = 1; i < node.size(); ++i) {
            holes[i - 1] = deserializeLinearRing((List) node.get(i));
        }
        return this.geometryFactory.createPolygon(shell, holes);
    }

    private LinearRing deserializeLinearRing(List node) {
        Coordinate[] coordinates = deserializeCoordinates(node);
        return this.geometryFactory.createLinearRing(coordinates);
    }

    private Coordinate deserializeCoordinate(List node) {
        if (node.size() < 2) {
            throw new IllegalArgumentException(String.format("Invalid number of ordinates: %d", node.size()));
        } else {
            double x = ((Number) node.get(0)).doubleValue();
            double y = ((Number) node.get(1)).doubleValue();
            if (node.size() < 3) {
                return new Coordinate(x, y);
            } else {
                double z = ((Number) node.get(2)).doubleValue();
                return new Coordinate(x, y, z);
            }
        }
    }

    private static GeometryFactory defaultGeometryFactory() {
        return new GeometryFactory(new PrecisionModel(), 4326);
    }

}
