/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.internal.event;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Injector;
import org.seedstack.business.Event;
import org.seedstack.business.EventHandler;
import org.seedstack.business.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collection;

public abstract class AbstractEventService implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEventService.class);

    private final ImmutableListMultimap<Class<? extends Event>, Class<? extends EventHandler>> eventHandlerClassesByEvent;

    private final Injector injector;
    @Inject
    AbstractEventService(Injector injector, Multimap<Class<? extends Event>, Class<? extends EventHandler>> eventHandlerClassesByEvent) {
        this.injector = injector;
        this.eventHandlerClassesByEvent = ImmutableListMultimap.copyOf(eventHandlerClassesByEvent);
    }


    @Override
    public <E extends Event> void fire(E event) {
        for (Class<? extends Event> eventClass : eventHandlerClassesByEvent.keys().elementSet()) {
            if (eventClass.isAssignableFrom(event.getClass())) {
                doFire(eventClass, event);
            }
        }
    }

    protected abstract <E extends Event> void doFire(Class<? extends Event> eventClass, E event);


    protected  <E extends Event> void notifyHandlers(Class<? extends E> eventClass, E event) {
        Collection<Class<? extends EventHandler>> eventHandlers = eventHandlerClassesByEvent.get(eventClass);
        for (Class<? extends EventHandler> eventHandlerClass : eventHandlers) {
            LOGGER.debug("Notify handler {}", eventHandlerClass.getName());
            EventHandler eventHandler = injector.getInstance(eventHandlerClass);
            eventHandler.handle(event);
        }
    }
}
