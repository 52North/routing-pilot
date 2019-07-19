package org.n52.testbed.routing.model.wps;

import org.n52.testbed.routing.model.MediaTypes;
import org.n52.testbed.routing.model.wps.Format;

public interface Formats {

    Format JSON_FORMAT = new Format(MediaTypes.APPLICATION_JSON);
    Format GEO_JSON_FORMAT = new Format(MediaTypes.APPLICATION_GEO_JSON);
}
