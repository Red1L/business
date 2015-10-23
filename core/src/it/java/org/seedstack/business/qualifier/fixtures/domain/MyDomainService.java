/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.qualifier.fixtures.domain;

import org.seedstack.business.api.Producible;
import org.seedstack.business.api.Service;
import org.seedstack.business.api.domain.DomainObject;

/**
 * @author pierre.thirouin@ext.mpsa.com
 */
@Service
public interface MyDomainService extends DomainObject, Producible{
}
