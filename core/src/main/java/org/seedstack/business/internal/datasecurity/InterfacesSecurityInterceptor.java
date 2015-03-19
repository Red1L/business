/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.internal.datasecurity;

import org.seedstack.business.api.interfaces.annotations.Secured;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seedstack.seed.core.utils.SeedReflectionUtils;
import org.seedstack.seed.security.api.data.DataSecurityService;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.seedstack.seed.core.utils.SeedReflectionUtils.isPresent;
import static org.seedstack.seed.core.utils.SeedReflectionUtils.methodsFromAncestors;

/**
 * This interceptor will apply Data Security Service on the dto
 * inside the interfaces.
 * 
 * @author epo.jemba@ext.mpsa.com
 * @author pierre.thirouin@ext.mpsa.com
 *
 */
class InterfacesSecurityInterceptor implements MethodInterceptor {

    @Inject
	private DataSecurityService dataSecurityService;
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		Method candidate = invocation.getMethod();

        boolean returnToFilter = isPresent(methodsFromAncestors(candidate), Secured.class);

        Annotation[] parameterToSecured = SeedReflectionUtils.parameterAnnotationsFromAncestors(candidate, Secured.class);
		
		Object[] arguments = invocation.getArguments();
		Object o = invocation.proceed();
		
		if (returnToFilter) {
			dataSecurityService.secure(o);
		}
		
		for (int i = 0; parameterToSecured != null && i < parameterToSecured.length; i++) {
			if (parameterToSecured[i] != null) {
				dataSecurityService.secure(arguments[i]);
			}
		}
		
		return o;
	}

}
