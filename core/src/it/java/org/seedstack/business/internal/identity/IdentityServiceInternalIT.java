/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
/**
 * 
 */
package org.seedstack.business.internal.identity;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.business.api.domain.identity.*;
import org.seedstack.seed.core.api.SeedException;
import org.seedstack.seed.it.SeedITRunner;

import javax.inject.Inject;
import java.util.UUID;



/**
 * IdentityServiceInternalIT
 * 
 * @author redouane.loulou@ext.mpsa.com
 * 
 */
@RunWith(SeedITRunner.class)
public class IdentityServiceInternalIT {

	@Inject
	IdentityService identityService;

	@Test
	public void identify_entity() {
		MyAggregate myAggregate = new MyAggregate();
		identityService.identify(myAggregate);
		Assertions.assertThat(myAggregate.getEntityId()).isNotNull();
	}

	@Test
	public void allready_identify_entity() {
		MyAggregate myAggregate = new MyAggregate();
		myAggregate.setId(UUID.randomUUID());
		try {

			identityService.identify(myAggregate);
			Assertions.fail("no exception occured");
		} catch (SeedException e) {
			Assertions.assertThat(IdentityErrorCodes.ID_MUST_BE_NULL)
					.isEqualTo(e.getErrorCode());
		}
	}

	@Test
	public void aggregate_with_bad_identity_Management() {
		MyAggregateWithBadIdentityManagement myAggregate = new MyAggregateWithBadIdentityManagement();
		try {

			identityService.identify(myAggregate);
			Assertions.fail("no exception occured");
		} catch (SeedException e) {
			Assertions.assertThat(IdentityErrorCodes.BAD_IDENTITY_HANDLER_DEFINE_FOR_ENTITY_ID)
					.isEqualTo(e.getErrorCode());
		}
	}

	@Test
	public void aggregate_with_no_identity_Management() {
		MyAggregateWithNoIdentityManagement myAggregate = new MyAggregateWithNoIdentityManagement();
		try {

			identityService.identify(myAggregate);
			Assertions.fail("no exception occured");
		} catch (SeedException e) {
			Assertions.assertThat(IdentityErrorCodes.NO_IDENTITY_HANDLER_DEFINE_FOR_ENTITY_ID)
					.isEqualTo(e.getErrorCode());
		}
	}

}
