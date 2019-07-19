package org.n52.testbed.routing.model.wps;

public interface Data {

    default boolean isComplex() {
        return false;
    }

    default boolean isLiteral() {
        return false;
    }

    default LiteralData asLiteral() {
        throw new UnsupportedOperationException();
    }

    default ComplexData asComplex() {
        throw new UnsupportedOperationException();
    }
}
