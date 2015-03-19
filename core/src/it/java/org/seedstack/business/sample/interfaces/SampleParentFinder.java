/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.sample.interfaces;

import org.seedstack.business.api.interfaces.query.finder.Finder;

import java.util.List;

@Finder
public interface SampleParentFinder<ITEM> {

	List<ITEM> findAll();
	
}
