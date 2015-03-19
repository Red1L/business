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


import org.seedstack.seed.security.spi.data.DataObfuscationHandler;
import org.seedstack.seed.security.spi.data.DataSecurityHandler;

/**
 *
 * @author epo.jemba@ext.mpsa.com
 */
class BusinessDataSecurityHandler implements DataSecurityHandler<Restriction> {

	@Override
	public Object securityExpression(Restriction candidate) {
		return candidate.value();
	}

	@Override
	public Class<? extends DataObfuscationHandler<?>> securityObfuscationHandler(Restriction candidate) {
		return candidate.obfuscation();
	}

}
