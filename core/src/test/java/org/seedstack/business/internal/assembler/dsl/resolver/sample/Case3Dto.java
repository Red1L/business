/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.internal.assembler.dsl.resolver.sample;

import org.seedstack.business.assembler.MatchingEntityId;

/**
 * @author pierre.thirouin@ext.mpsa.com (Pierre Thirouin)
 */
public class Case3Dto {

    String customerName;

    String orderItem;

    public Case3Dto(String customerName, String orderItem) {
        this.customerName = customerName;
        this.orderItem = orderItem;
    }

    @MatchingEntityId(typeIndex = 0)
    public String getCustomerName() {
        return customerName;
    }

    @MatchingEntityId(typeIndex = 1)
    public String getOrderItem() {
        return orderItem;
    }

}
