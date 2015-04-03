/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.internal;

import org.seedstack.business.api.Event;
import org.seedstack.business.api.EventHandler;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.inject.Inject;

/**
 * This class intercepts the handle method of each EventHandlers and store the calls in a context.
 *
 * @author pierre.thirouin@ext.mpsa.com
 *         Date: 06/06/2014
 */
class EventHandlerInterceptor implements MethodInterceptor {

    @Inject
    private ContextLink contextLink;

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        contextLink.put((Class<? extends EventHandler>) invocation.getMethod().getDeclaringClass(), (Event) invocation.getArguments()[0]);
        return invocation.proceed();
    }
}
