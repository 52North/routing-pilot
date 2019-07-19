package org.n52.testbed.routing.persistence;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.locationtech.jts.geom.*;
import org.n52.jackson.datatype.jts.GeoJsonConstants;
import org.n52.jackson.datatype.jts.GeometryType;
import org.n52.jackson.datatype.jts.IncludeBoundingBox;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

/**
 * {@link JsonSerializer} for {@link Geometry}.
 *
 * @author Christian Autermann
 */
@MongoConverter
@WritingConverter
public class GeometryWriteConverter implements Converter<Geometry, DBObject> {

    private final IncludeBoundingBox includeBoundingBox;

    /**
     * Creates a new {@link GeometryWriteConverter}.
     */
    public GeometryWriteConverter() {
        this(null);
    }

    /**
     * Creates a new {@link GeometryWriteConverter}.
     *
     * @param includeBoundingBox when to include a bounding box for a {@link Geometry}.
     */
    public GeometryWriteConverter(IncludeBoundingBox includeBoundingBox) {
        this.includeBoundingBox = Optional.ofNullable(includeBoundingBox)
                .orElseGet(IncludeBoundingBox::never);
    }

    @Override
    public DBObject convert(Geometry geometry) {
        if (geometry == null) {
            return null;
        } else if (geometry instanceof Polygon) {
            return serialize((Polygon) geometry);
        } else if (geometry instanceof Point) {
            return serialize((Point) geometry);
        } else if (geometry instanceof MultiPoint) {
            return serialize((MultiPoint) geometry);
        } else if (geometry instanceof MultiPolygon) {
            return serialize((MultiPolygon) geometry);
        } else if (geometry instanceof LineString) {
            return serialize((LineString) geometry);
        } else if (geometry instanceof MultiLineString) {
            return serialize((MultiLineString) geometry);
        } else if (geometry instanceof GeometryCollection) {
            return serialize((GeometryCollection) geometry);
        } else {
            throw new IllegalArgumentException(String.format("Geometry type %s is not supported.",
                    geometry.getClass().getName()));
        }
    }

    private BasicDBObject serializeTypeAndBoundingBox(Geometry geometry) {
        BasicDBObject o = new BasicDBObject();
        GeometryType type = GeometryType.forGeometry(geometry).orElseThrow(IllegalArgumentException::new);
        o.put(GeoJsonConstants.Fields.TYPE, type.toString());
        if (this.includeBoundingBox.shouldIncludeBoundingBoxFor(type) && !geometry.isEmpty()) {
            Envelope envelope = geometry.getEnvelopeInternal();
            BasicDBList list = new BasicDBList();
            list.add(envelope.getMinX());
            list.add(envelope.getMinY());
            list.add(envelope.getMaxX());
            list.add(envelope.getMaxY());
            o.put(GeoJsonConstants.Fields.BOUNDING_BOX, list);
        }
        return o;
    }

    private DBObject serialize(GeometryCollection value) {
        return serializeTypeAndBoundingBox(value)
                .append(GeoJsonConstants.Fields.GEOMETRIES,
                        IntStream.range(0, value.getNumGeometries())
                                .mapToObj(value::getGeometryN)
                                .map(this::convert)
                                .collect(toCollection(BasicDBList::new)));
    }

    private DBObject serialize(MultiPoint value) {
        return serializeTypeAndBoundingBox(value)
                .append(GeoJsonConstants.Fields.COORDINATES,
                        IntStream.range(0, value.getNumGeometries())
                                .mapToObj(value::getGeometryN)
                                .map(Point.class::cast)
                                .map(this::serializeCoordinate)
                                .collect(toCollection(BasicDBList::new)));
    }

    private DBObject serialize(MultiLineString value) {
        return serializeTypeAndBoundingBox(value)
                .append(GeoJsonConstants.Fields.COORDINATES,
                        IntStream.range(0, value.getNumGeometries())
                                .mapToObj(value::getGeometryN)
                                .map(LineString.class::cast)
                                .map(this::serializeCoordinates)
                                .collect(toCollection(BasicDBList::new)));
    }

    private DBObject serialize(MultiPolygon value) {
        return serializeTypeAndBoundingBox(value)
                .append(GeoJsonConstants.Fields.COORDINATES,
                        IntStream.range(0, value.getNumGeometries())
                                .mapToObj(value::getGeometryN)
                                .map(Polygon.class::cast)
                                .map(this::serializeCoordinates)
                                .collect(toCollection(BasicDBList::new)));
    }

    private DBObject serialize(Polygon value) {
        return serializeTypeAndBoundingBox(value)
                .append(GeoJsonConstants.Fields.COORDINATES, serializeCoordinates(value));
    }

    private DBObject serialize(LineString value) {
        return serializeTypeAndBoundingBox(value)
                .append(GeoJsonConstants.Fields.COORDINATES, serializeCoordinates(value));
    }

    private DBObject serialize(Point value) {
        return serializeTypeAndBoundingBox(value)
                .append(GeoJsonConstants.Fields.COORDINATES, serializeCoordinate(value));
    }

    private DBObject serializeCoordinates(Polygon value) {
        return Stream.concat(
                Stream.of(value.getExteriorRing()),
                IntStream.range(0, value.getNumInteriorRing())
                        .mapToObj(value::getInteriorRingN))
                .map(this::serializeCoordinates)
                .collect(toCollection(BasicDBList::new));
    }

    private DBObject serializeCoordinates(LineString value) {
        return serializeCoordinates(value.getCoordinateSequence());
    }

    private DBObject serializeCoordinates(CoordinateSequence value) {
        return IntStream.range(0, value.size())
                .mapToObj(value::getCoordinate)
                .map(this::serializeCoordinate)
                .collect(toCollection(BasicDBList::new));
    }

    private DBObject serializeCoordinate(Point value) {
        return serializeCoordinate(value.getCoordinate());
    }

    private DBObject serializeCoordinate(Coordinate value) {
        BasicDBList list = new BasicDBList();
        list.add(value.getX());
        list.add(value.getY());
        if (!Double.isNaN(value.getZ()) && Double.isFinite(value.getZ())) {
            list.add(value.getZ());
        }
        return list;
    }

}
