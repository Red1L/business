/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.internal.event;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.business.EventService;
import org.seedstack.business.events.fixtures.MyEvent;
import org.seedstack.business.events.fixtures.cyclic.MyAsyncEvent;
import org.seedstack.seed.it.SeedITRunner;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

@RunWith(SeedITRunner.class)
public class AsyncEventServiceIT {

    @Inject
    @Named("async")
    private EventService eventService;

    public static CountDownLatch countDownLatch = new CountDownLatch(2);

    @Test
    public void test() throws InterruptedException {
        assertThat(eventService).isNotNull();
        eventService.fire(new MyAsyncEvent());
        assertThat(countDownLatch.await(50L, TimeUnit.MILLISECONDS)).isTrue();
        assertThat(countDownLatch.getCount()).isEqualTo(0);
    }
}