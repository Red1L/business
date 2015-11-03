/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.fixtures.infrastructure.presentation.product1;

import org.seedstack.business.finder.Range;
import org.seedstack.business.finder.Result;
import org.seedstack.business.fixtures.interfaces.product.presentationsimple1.ProductSimple1RangeFinder;
import org.seedstack.business.fixtures.interfaces.product.presentationsimple1.ProductSimple1Representation;

public class JpaProductSimple1RangeFinder implements ProductSimple1RangeFinder{

	@Override
	public Result<ProductSimple1Representation> find(Range range, String criteria) {
		return null;
	}
}
