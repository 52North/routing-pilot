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
package org.n52.testbed.routing.persistence.converters;

import com.fasterxml.jackson.databind.JsonDeserializer;
import org.bson.Document;
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
import org.n52.testbed.routing.persistence.GeoBSONConstants;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * {@link JsonDeserializer} for {@link Geometry}.
 *
 * @author Christian Autermann
 */
@SuppressWarnings("unchecked")
public abstract class AbstractGeometryReadConverter<T extends Geometry>
        implements GenericConverter, Converter<Document, Geometry> {

    private final GeometryFactory geometryFactory;
    private final Class<T> geometryType;

    protected AbstractGeometryReadConverter(Class<T> geometryType) {
        this.geometryFactory = defaultGeometryFactory();
        this.geometryType = Objects.requireNonNull(geometryType);
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(Document.class, geometryType));
    }

    @Override
    public Geometry convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return convert((Document) source);
    }

    @Override
    public Geometry convert(Document document) {
        return deserializeGeometry(document);
    }

    private Geometry deserializeGeometry(Document node) {
        String typeName = node.getString(GeoBSONConstants.TYPE);

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

    private Point deserializePoint(Document node) {
        return this.geometryFactory.createPoint(
                deserializeCoordinate((List<Number>) node.get(GeoBSONConstants.COORDINATES)));
    }

    private Polygon deserializePolygon(Document node) {
        return deserializeLinearRings((List<List<List<Number>>>) node.get(GeoBSONConstants.COORDINATES));
    }

    private MultiPolygon deserializeMultiPolygon(Document node) {
        List<List<List<List<Number>>>> coordinates = (List<List<List<List<Number>>>>)
                                                             node.get(GeoBSONConstants.COORDINATES);
        Polygon[] polygons = new Polygon[coordinates.size()];
        for (int i = 0; i != coordinates.size(); ++i) {
            polygons[i] = deserializeLinearRings(coordinates.get(i));
        }
        return this.geometryFactory.createMultiPolygon(polygons);
    }

    private MultiPoint deserializeMultiPoint(Document node) {
        Coordinate[] coordinates = deserializeCoordinates((List<List<Number>>) node.get(GeoBSONConstants.COORDINATES));
        return this.geometryFactory.createMultiPointFromCoords(coordinates);
    }

    private GeometryCollection deserializeGeometryCollection(Document node) {
        List<Document> geometries = (List<Document>) node.get(GeoBSONConstants.GEOMETRIES);
        Geometry[] geom = new Geometry[geometries.size()];
        for (int i = 0; i != geometries.size(); ++i) {
            geom[i] = deserializeGeometry(geometries.get(i));
        }
        return this.geometryFactory.createGeometryCollection(geom);
    }

    private MultiLineString deserializeMultiLineString(Document node) {
        List<List<List<Number>>> coordinates = (List<List<List<Number>>>) node.get(GeoBSONConstants.COORDINATES);
        LineString[] lineStrings = lineStringsFromJson(coordinates);
        return this.geometryFactory.createMultiLineString(lineStrings);
    }

    private LineString[] lineStringsFromJson(List<List<List<Number>>> node) {
        LineString[] strings = new LineString[node.size()];
        for (int i = 0; i != node.size(); ++i) {
            Coordinate[] coordinates = deserializeCoordinates(node.get(i));
            strings[i] = this.geometryFactory.createLineString(coordinates);
        }
        return strings;
    }

    private LineString deserializeLineString(Document node) {
        Coordinate[] coordinates = deserializeCoordinates(node.get(GeoBSONConstants.COORDINATES, List.class));
        return this.geometryFactory.createLineString(coordinates);
    }

    private Coordinate[] deserializeCoordinates(List<List<Number>> node) {
        Coordinate[] points = new Coordinate[node.size()];
        for (int i = 0; i != node.size(); ++i) {
            points[i] = deserializeCoordinate(node.get(i));
        }
        return points;
    }

    private Polygon deserializeLinearRings(List<List<List<Number>>> node) {
        LinearRing shell = deserializeLinearRing(node.get(0));
        LinearRing[] holes = new LinearRing[node.size() - 1];
        for (int i = 1; i < node.size(); ++i) {
            holes[i - 1] = deserializeLinearRing(node.get(i));
        }
        return this.geometryFactory.createPolygon(shell, holes);
    }

    private LinearRing deserializeLinearRing(List<List<Number>> node) {
        Coordinate[] coordinates = deserializeCoordinates(node);
        return this.geometryFactory.createLinearRing(coordinates);
    }

    private Coordinate deserializeCoordinate(List<Number> node) {
        if (node.size() < 2) {
            throw new IllegalArgumentException(String.format("Invalid number of ordinates: %d", node.size()));
        } else {
            double x = node.get(0).doubleValue();
            double y = node.get(1).doubleValue();
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
