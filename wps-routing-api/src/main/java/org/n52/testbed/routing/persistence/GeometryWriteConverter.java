/*
 * Copyright 2019 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.testbed.routing.persistence;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

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

        if (geometry instanceof Point) {
            o.put(GeoBSONConstants.TYPE, GeoBSONConstants.POINT);
        } else if (geometry instanceof LineString) {
            o.put(GeoBSONConstants.TYPE, GeoBSONConstants.LINE_STRING);
        } else if (geometry instanceof Polygon) {
            o.put(GeoBSONConstants.TYPE, GeoBSONConstants.POLYGON);
        } else if (geometry instanceof MultiPoint) {
            o.put(GeoBSONConstants.TYPE, GeoBSONConstants.MULTI_POINT);
        } else if (geometry instanceof MultiLineString) {
            o.put(GeoBSONConstants.TYPE, GeoBSONConstants.MULTI_LINE_STRING);
        } else if (geometry instanceof MultiPolygon) {
            o.put(GeoBSONConstants.TYPE, GeoBSONConstants.MULTI_POLYGON);
        } else if (geometry instanceof GeometryCollection) {
            o.put(GeoBSONConstants.TYPE, GeoBSONConstants.GEOMETRY_COLLECTION);
        } else {
            throw new IllegalArgumentException();
        }

        return o;
    }

    private DBObject serialize(GeometryCollection value) {
        return serializeTypeAndBoundingBox(value)
                .append(GeoBSONConstants.GEOMETRIES,
                        IntStream.range(0, value.getNumGeometries())
                                .mapToObj(value::getGeometryN)
                                .map(this::convert)
                                .collect(toCollection(BasicDBList::new)));
    }

    private DBObject serialize(MultiPoint value) {
        return serializeTypeAndBoundingBox(value)
                .append(GeoBSONConstants.COORDINATES,
                        IntStream.range(0, value.getNumGeometries())
                                .mapToObj(value::getGeometryN)
                                .map(Point.class::cast)
                                .map(this::serializeCoordinate)
                                .collect(toCollection(BasicDBList::new)));
    }

    private DBObject serialize(MultiLineString value) {
        return serializeTypeAndBoundingBox(value)
                .append(GeoBSONConstants.COORDINATES,
                        IntStream.range(0, value.getNumGeometries())
                                .mapToObj(value::getGeometryN)
                                .map(LineString.class::cast)
                                .map(this::serializeCoordinates)
                                .collect(toCollection(BasicDBList::new)));
    }

    private DBObject serialize(MultiPolygon value) {
        return serializeTypeAndBoundingBox(value)
                .append(GeoBSONConstants.COORDINATES,
                        IntStream.range(0, value.getNumGeometries())
                                .mapToObj(value::getGeometryN)
                                .map(Polygon.class::cast)
                                .map(this::serializeCoordinates)
                                .collect(toCollection(BasicDBList::new)));
    }

    private DBObject serialize(Polygon value) {
        return serializeTypeAndBoundingBox(value)
                .append(GeoBSONConstants.COORDINATES, serializeCoordinates(value));
    }

    private DBObject serialize(LineString value) {
        return serializeTypeAndBoundingBox(value)
                .append(GeoBSONConstants.COORDINATES, serializeCoordinates(value));
    }

    private DBObject serialize(Point value) {
        return serializeTypeAndBoundingBox(value)
                .append(GeoBSONConstants.COORDINATES, serializeCoordinate(value));
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
