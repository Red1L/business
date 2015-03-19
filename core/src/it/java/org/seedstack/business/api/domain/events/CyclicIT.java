/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.api.domain.events;

import org.seedstack.business.api.EventService;
import org.seedstack.business.api.domain.events.fixtures.cyclic.Event1;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.seed.core.api.SeedException;
import org.seedstack.seed.it.SeedITRunner;

import javax.inject.Inject;

/**
 * @author pierre.thirouin@ext.mpsa.com
 *         Date: 11/06/2014
 */
@RunWith(SeedITRunner.class)
public class CyclicIT {

    @Inject
    private EventService eventService;

    @Test(expected = SeedException.class)
    public void fire_cyclic_events() {
        eventService.fire(new Event1());
    }
}
