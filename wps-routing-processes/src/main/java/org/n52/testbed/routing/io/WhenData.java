package org.n52.testbed.routing.io;

import org.n52.javaps.io.complex.ComplexData;
import org.n52.testbed.routing.model.routing.When;

public class WhenData implements ComplexData<When> {
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
