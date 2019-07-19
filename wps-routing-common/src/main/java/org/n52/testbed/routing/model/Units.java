package org.n52.testbed.routing.model;

import org.n52.testbed.routing.model.wps.NamedReference;

public interface Units {
    String TON = "ton";
    NamedReference TON_REFERENCE = new NamedReference(TON);
    String METER = "meter";
    NamedReference METER_REFERENCE = new NamedReference(METER);
}
