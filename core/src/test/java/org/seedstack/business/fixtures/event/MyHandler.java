/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.fixtures.event;

import org.seedstack.business.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pierre.thirouin@ext.mpsa.com
 */
public class MyHandler implements EventHandler<MyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyHandler.class);

    @Override
    public void handle(MyEvent event) {
        LOGGER.info("MyHandler gets a MyEvent event.");
    }
}
