package org.n52.testbed.routing.here;

import org.n52.janmayen.ConfigLocationProvider;

import java.io.File;

public class StaticConfigLocationProvider implements ConfigLocationProvider {

    @Override
    public String get() {
        return new File(getClass().getResource("/test.xml").getPath()).getParent();
    }
}
