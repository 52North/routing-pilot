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
package org.n52.testbed.routing.model;

public interface ConformanceClasses {
    String PROCESSES_API = "http://www.opengis.net/spec/ogcapi-processes-1/1.0/conf/core";
    String CORE = "http://www.opengis.net/orapip/routing/1.0/conf/core";
    String INTERMEDIATE_WAYPOINTS = "http://www.opengis.net/orapip/routing/1.0/conf/intermediate-waypoints";
    String MAX_HEIGHT = "http://www.opengis.net/orapip/routing/1.0/conf/max-height";
    String MAX_WEIGHT = "http://www.opengis.net/orapip/routing/1.0/conf/max-weight";
    String OBSTACLES = "http://www.opengis.net/orapip/routing/1.0/conf/obstacles";
    String ROUTING_ENGINE = "http://www.opengis.net/orapip/routing/1.0/conf/routing-engine";
    String ROUTING_ALGORITHM = "http://www.opengis.net/orapip/routing/1.0/conf/routing-algorithm";
    String SOURCE_DATASET = "http://www.opengis.net/orapip/routing/1.0/conf/source-dataset";
    String TIME = "http://www.opengis.net/orapip/routing/1.0/conf/time";
    String CALLBACK = "http://www.opengis.net/orapip/routing/1.0/conf/callback";
    String RESULT_SET = "http://www.opengis.net/orapip/routing/1.0/conf/result-set";
    String SYNC_MODE = "http://www.opengis.net/orapip/routing/1.0/conf/sync-mode";
    String DELETE_ROUTE = "http://www.opengis.net/orapip/routing/1.0/conf/delete-route";
}
