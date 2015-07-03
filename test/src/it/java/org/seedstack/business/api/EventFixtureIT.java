/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.api;

import org.seedstack.business.api.fixtures.MyEvent;
import org.seedstack.business.api.fixtures.MyHandler;
import org.seedstack.business.api.fixtures.MyHandler2;
import org.seedstack.business.api.fixtures.MyHandler3;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.business.test.event.EventFixture;
import org.seedstack.seed.it.SeedITRunner;

import javax.inject.Inject;

/**
 * @author pierre.thirouin@ext.mpsa.com
 */
@RunWith(SeedITRunner.class)
public class EventFixtureIT {

    @Inject
    private EventFixture fixture;

    @SuppressWarnings("unchecked")
    @Test
    public void expect_was_handler_by() {
         fixture.given(new MyEvent("")).whenFired().wasHandledBy(MyHandler.class);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = Exception.class)
    public void expect_was_handler_by_failed() {
        fixture.given(new MyEvent("")).whenFired().wasHandledBy(MyHandler3.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void expect_was_exactly_handler_by() {
        fixture.given(new MyEvent("")).whenFired().wasHandledExactlyBy(MyHandler.class, MyHandler2.class);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = Exception.class)
    public void expect_was_exactly_handler_by_failed() {
        fixture.given(new MyEvent("")).whenFired().wasHandledExactlyBy(MyHandler.class, MyHandler2.class, MyHandler3.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void expect_was_not_handler_by() {
        fixture.given(new MyEvent("")).whenFired().wasNotHandledBy(MyHandler3.class);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = Exception.class)
    public void expect_was_not_handler_by_failed() {
        fixture.given(new MyEvent("")).whenFired().wasNotHandledBy(MyHandler.class);
    }
}
