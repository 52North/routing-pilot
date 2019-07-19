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
package org.n52.testbed.routing.io.geojson;

import org.locationtech.jts.geom.GeometryCollection;

public class GeometryCollectionData implements GeoJsonData<GeometryCollection> {
    private static final long serialVersionUID = 7093055868691003738L;
    private final GeometryCollection payload;

    public GeometryCollectionData(GeometryCollection payload) {
        this.payload = payload;
    }

    @Override
    public GeometryCollection getPayload() {
        return this.payload;
    }

    @Override
    public Class<GeometryCollection> getSupportedClass() {
        return GeometryCollection.class;
    }
}
