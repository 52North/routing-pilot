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

/**
 * Link relation types.
 */
public interface LinkRelation {
    String PROCESSES = "processes";
    String CONFORMANCE = "conformance";
    String SERVICE = "service";
    String SELF = "self";
    String CANONICAL = "canonical";
    String DESCRIBED_BY = "describedby";
    String ITEM = "item";
    String DATA = "data";
}
