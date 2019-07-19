package org.n52.jackson.datatype.jts;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.locationtech.jts.geom.*;

import java.util.Optional;

/**
 * {@link com.fasterxml.jackson.databind.Module} to add serialization for JTS geometries.
 *
 * @author Christian Autermann
 */
public class JtsModule extends SimpleModule {
    private static final int MAJOR = 1;
    private static final int MINOR = 0;
    private static final int PATCH = 0;
    private static final long serialVersionUID = 1L;
    private static final String GROUP_ID = "org.n52.jackson.datatype";
    private static final String ARTIFACT_ID = "jackson-datatype-jts";
    private static final String SNAPSHOT_INFO = null;
    private static final String MODULE_NAME = "JtsModule";

    private final GeometryFactory geometryFactory;
    private final IncludeBoundingBox includeBoundingBox;

    /**
     * Creates a new {@link JtsModule}.
     */
    public JtsModule() {
        this(null, null);
    }

    /**
     * Creates a new {@link JtsModule}.
     *
     * @param geometryFactory The {@link GeometryFactory} to use to construct geometries.
     */
    public JtsModule(GeometryFactory geometryFactory) {
        this(geometryFactory, null);
    }

    /**
     * Creates a new {@link JtsModule}.
     *
     * @param includeBoundingBox The {@link IncludeBoundingBox} to use to serialize geometries.
     */
    public JtsModule(IncludeBoundingBox includeBoundingBox) {
        this(null, includeBoundingBox);
    }

    /**
     * Creates a new {@link JtsModule}.
     *
     * @param geometryFactory    The {@link GeometryFactory} to use to construct geometries.
     * @param includeBoundingBox The {@link IncludeBoundingBox} to use to serialize geometries.
     */
    public JtsModule(GeometryFactory geometryFactory, IncludeBoundingBox includeBoundingBox) {
        super(MODULE_NAME, new Version(MAJOR, MINOR, PATCH, SNAPSHOT_INFO, GROUP_ID, ARTIFACT_ID));
        this.geometryFactory = Optional.ofNullable(geometryFactory).orElseGet(JtsModule::getDefaultGeometryFactory);
        this.includeBoundingBox = Optional.ofNullable(includeBoundingBox).orElseGet(IncludeBoundingBox::never);
    }


    @Override
    public void setupModule(SetupContext context) {
        JsonDeserializer<Geometry> deserializer = getDeserializer();
        addSerializer(Geometry.class, getSerializer());
        addDeserializer(Geometry.class, deserializer);
        addDeserializer(Point.class, new TypeSafeJsonDeserializer<>(Point.class, deserializer));
        addDeserializer(LineString.class, new TypeSafeJsonDeserializer<>(LineString.class, deserializer));
        addDeserializer(Polygon.class, new TypeSafeJsonDeserializer<>(Polygon.class, deserializer));
        addDeserializer(MultiPoint.class, new TypeSafeJsonDeserializer<>(MultiPoint.class, deserializer));
        addDeserializer(MultiLineString.class, new TypeSafeJsonDeserializer<>(MultiLineString.class, deserializer));
        addDeserializer(MultiPolygon.class, new TypeSafeJsonDeserializer<>(MultiPolygon.class, deserializer));
        addDeserializer(GeometryCollection.class, new TypeSafeJsonDeserializer<>(GeometryCollection.class, deserializer));
        super.setupModule(context);
    }


    private JsonSerializer<Geometry> getSerializer() {
        return new GeometrySerializer(this.includeBoundingBox);
    }

    private JsonDeserializer<Geometry> getDeserializer() {
        return geometryFactory == null ? new GeometryDeserializer() : new GeometryDeserializer(geometryFactory);
    }

    private static GeometryFactory getDefaultGeometryFactory() {
        return new GeometryFactory(new PrecisionModel(), 4326);
    }

}
