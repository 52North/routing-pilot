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
package org.n52.testbed.routing.io;

import org.n52.javaps.io.complex.ComplexData;
import org.n52.testbed.routing.model.routing.When;

public class WhenData implements ComplexData<When> {
    private static final long serialVersionUID = -8575657210450363718L;
    private final When payload;

    public WhenData(When payload) {
        this.payload = payload;
    }

    @Override
    public When getPayload() {
        return this.payload;
    }

    @Override
    public Class<?> getSupportedClass() {
        return When.class;
    }
}
