package org.n52.testbed.routing.io;

import org.n52.javaps.io.InputHandler;

public class WhenInputHandler extends AbstractJacksonInputOutputHandler implements InputHandler {
    public WhenInputHandler() {
        addSupportedBinding(WhenData.class);
    }
}
