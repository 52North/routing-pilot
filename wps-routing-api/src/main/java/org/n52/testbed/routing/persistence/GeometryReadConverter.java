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

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.mongodb.DBObject;
import org.bson.BSONObject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
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
        String typeName = node.get(GeoBSONConstants.TYPE).toString();

        switch (typeName) {
            case GeoBSONConstants.POINT:
                return deserializePoint(node);
            case GeoBSONConstants.MULTI_POINT:
                return deserializeMultiPoint(node);
            case GeoBSONConstants.LINE_STRING:
                return deserializeLineString(node);
            case GeoBSONConstants.MULTI_LINE_STRING:
                return deserializeMultiLineString(node);
            case GeoBSONConstants.POLYGON:
                return deserializePolygon(node);
            case GeoBSONConstants.MULTI_POLYGON:
                return deserializeMultiPolygon(node);
            case GeoBSONConstants.GEOMETRY_COLLECTION:
                return deserializeGeometryCollection(node);
            default:
                throw new IllegalArgumentException(String.format("Invalid geometry type: %s", typeName));
        }
    }

    private Point deserializePoint(BSONObject node) {
        return this.geometryFactory.createPoint(deserializeCoordinate((List) node.get(GeoBSONConstants.COORDINATES)));
    }

    private Polygon deserializePolygon(BSONObject node) {
        return deserializeLinearRings((List<?>) node.get(GeoBSONConstants.COORDINATES));
    }

    private MultiPolygon deserializeMultiPolygon(BSONObject node) {
        List<?> coordinates = (List<?>) node.get(GeoBSONConstants.COORDINATES);
        Polygon[] polygons = new Polygon[coordinates.size()];
        for (int i = 0; i != coordinates.size(); ++i) {
            polygons[i] = deserializeLinearRings((List<?>) coordinates.get(i));
        }
        return this.geometryFactory.createMultiPolygon(polygons);
    }

    private MultiPoint deserializeMultiPoint(BSONObject node) {
        Coordinate[] coordinates = deserializeCoordinates((List) node.get(GeoBSONConstants.COORDINATES));
        return this.geometryFactory.createMultiPointFromCoords(coordinates);
    }

    private GeometryCollection deserializeGeometryCollection(BSONObject node) {
        List<?> geometries = (List<?>) node.get(GeoBSONConstants.GEOMETRIES);
        Geometry[] geom = new Geometry[geometries.size()];
        for (int i = 0; i != geometries.size(); ++i) {
            geom[i] = deserializeGeometry((BSONObject) geometries.get(i));
        }
        return this.geometryFactory.createGeometryCollection(geom);
    }

    private MultiLineString deserializeMultiLineString(BSONObject node) {
        LineString[] lineStrings = lineStringsFromJson((List) node.get(GeoBSONConstants.COORDINATES));
        return this.geometryFactory.createMultiLineString(lineStrings);
    }

    private LineString[] lineStringsFromJson(List<?> node) {
        LineString[] strings = new LineString[node.size()];
        for (int i = 0; i != node.size(); ++i) {
            Coordinate[] coordinates = deserializeCoordinates((List) node.get(i));
            strings[i] = this.geometryFactory.createLineString(coordinates);
        }
        return strings;
    }

    private LineString deserializeLineString(BSONObject node) {
        Coordinate[] coordinates = deserializeCoordinates((List) node.get(GeoBSONConstants.COORDINATES));
        return this.geometryFactory.createLineString(coordinates);
    }

    private Coordinate[] deserializeCoordinates(List<?> node) {
        Coordinate[] points = new Coordinate[node.size()];
        for (int i = 0; i != node.size(); ++i) {
            points[i] = deserializeCoordinate((List) node.get(i));
        }
        return points;
    }

    private Polygon deserializeLinearRings(List<?> node) {
        LinearRing shell = deserializeLinearRing((List<?>) node.get(0));
        LinearRing[] holes = new LinearRing[node.size() - 1];
        for (int i = 1; i < node.size(); ++i) {
            holes[i - 1] = deserializeLinearRing((List<?>) node.get(i));
        }
        return this.geometryFactory.createPolygon(shell, holes);
    }

    private LinearRing deserializeLinearRing(List<?> node) {
        Coordinate[] coordinates = deserializeCoordinates(node);
        return this.geometryFactory.createLinearRing(coordinates);
    }

    private Coordinate deserializeCoordinate(List<?> node) {
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
