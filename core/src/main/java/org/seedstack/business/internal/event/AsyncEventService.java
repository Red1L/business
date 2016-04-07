/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.internal.event;

import com.google.common.collect.Multimap;
import com.google.inject.Injector;
import org.seedstack.business.Event;
import org.seedstack.business.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.ExecutorService;

@Named("async")
public class AsyncEventService extends AbstractEventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncEventService.class);

    @Inject
    private ExecutorService executorService;

    @Inject
    public AsyncEventService(Injector injector, Multimap<Class<? extends Event>, Class<? extends EventHandler>> eventHandlerClassesByEvent) {
        super(injector, eventHandlerClassesByEvent);
    }

    @Override
    protected <E extends Event> void doFire(final Class<? extends Event> eventClass, final E event) {
        LOGGER.trace("Asynchronously fired {}", event.getClass().getName());
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                notifyHandlers(eventClass, event);
            }
        });
    }
}
