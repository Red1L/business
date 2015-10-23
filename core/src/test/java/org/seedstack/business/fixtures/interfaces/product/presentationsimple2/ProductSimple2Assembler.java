/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.fixtures.interfaces.product.presentationsimple2;

import org.seedstack.business.api.interfaces.assembler.BaseAssembler;
import org.seedstack.business.fixtures.domain.product.Product;

public class ProductSimple2Assembler extends BaseAssembler<Product, ProductSimple2Representation> {

	@Override
	protected void doAssembleDtoFromAggregate(ProductSimple2Representation targetDto , Product sourceEntity)
	{
		targetDto.fillProductId(sourceEntity.getEntityId().getStoreId(), sourceEntity.getEntityId().getProductCode());
		targetDto.setName(sourceEntity.getName());
		targetDto.setDescription(sourceEntity.getDescription());
	}

	@Override
	protected void doMergeAggregateWithDto(Product targetEntity , ProductSimple2Representation sourceDto)
	{
		targetEntity.setName(sourceDto.getName());
		targetEntity.setDescription(sourceDto.getDescription());
	}

}
