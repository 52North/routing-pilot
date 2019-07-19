package org.n52.jackson.datatype.jts;

/**
 * GeoJSON constants.
 */
public interface GeoJsonConstants {
    /**
     * JSON field names.
     */
    interface Fields {
        /**
         * The {@value} field.
         */
        String TYPE = "type";
        /**
         * The {@value} field.
         */
        String GEOMETRIES = "geometries";
        /**
         * The {@value} field.
         */
        String COORDINATES = "coordinates";
        /**
         * The {@value} field.
         */
        String BOUNDING_BOX = "bbox";
    }
}
