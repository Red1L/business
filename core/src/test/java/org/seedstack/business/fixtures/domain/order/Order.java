/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.fixtures.domain.order;

import org.seedstack.business.api.domain.base.BaseAggregateRoot;
import org.seedstack.business.fixtures.domain.customer.CustomerId;

import java.util.ArrayList;
import java.util.List;



public class Order extends BaseAggregateRoot<OrderId> {
	

	private CustomerId customerId;
	private String description;
	
	
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();


	private OrderId entityId;
	
	public Order() {		
	}
	
	Order(OrderId entityId) {
		this.entityId = entityId;
	}
	
	@Override
	public OrderId getEntityId() {
		
		return this.entityId;
	}
	
	public CustomerId getCustomerId() {
		return customerId;
	}

	public void setCustomerId(CustomerId customerId) {
		this.customerId = customerId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
