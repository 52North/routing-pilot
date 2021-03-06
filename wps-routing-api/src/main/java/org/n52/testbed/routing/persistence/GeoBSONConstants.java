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

public interface          GeoBSONConstants {
    String TYPE = "type";
    String GEOMETRIES = "geometries";
    String COORDINATES = "coordinates";
    String POINT = "Point";
    String LINE_STRING = "LineString";
    String POLYGON = "Polygon";
    String MULTI_POINT = "MultiPoint";
    String MULTI_LINE_STRING = "MultiLineString";
    String MULTI_POLYGON = "MultiPolygon";
    String GEOMETRY_COLLECTION = "GeometryCollection";
}
