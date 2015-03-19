/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.api.interfaces.assembler;

import org.seedstack.business.api.domain.AggregateRoot;
import org.seedstack.seed.core.utils.SeedReflectionUtils;

import java.lang.reflect.ParameterizedType;

/**
 * This class is the class to be extended by the users in order to create an Assembler.
 * <p/>
 * User must implements
 * {@link BaseAssembler#doAssembleDtoFromAggregate(Object, org.seedstack.business.api.domain.AggregateRoot)} and
 * {@link BaseAssembler#doMergeAggregateWithDto(org.seedstack.business.api.domain.AggregateRoot, Object)} to provide
 * implementation of the copy.
 * <p/>
 * For instance:
 * <pre>
 * public class ProductAssembler extends BaseAssembler{@literal <Product,ProductRepresentation>} {
 *
 *     {@literal @}Override
 *     protected void doAssembleDtoFromAggregate(ProductRepresentation targetDto, Product sourceAggregate) {
 *     	  targetDto.fillProductId(sourceAggregate.getEntityId().getStoreId(),
 *     	      sourceAggregate.getEntityId().getProductCode());
 *     	  targetDto.setName(sourceAggregate.getName());
 *     	  targetDto.setDescription(sourceAggregate.getDescription());
 *     }
 *
 *     {@literal @}Override
 *     protected void doMergeAggregateWithDto(Product targetAggregate, ProductRepresentation sourceDto) {
 *     	  targetAggregate.setName(sourceDto.getName());
 *     	  targetAggregate.setDescription(sourceDto.getDescription());
 *     }
 * }
 * </pre>
 * Then the assembler can be used via:
 * <pre>
 * {@literal @}Inject
 * ProductAssembler productAssembler;
 * </pre>
 * And used like this:
 * <pre>
 * ProductRepresentation productRepresentation = productAssembler.assembleDtoFromAggregate(productFromRepo);
 * </pre>
 * or
 * <pre>
 * productAssembler.mergeAggregateWithDto(productToMerge, productRepresentationSource);
 * </pre>
 * Note that {@link Assemblers} will automatically have a reference of {@code ProductAssembler} and the following will
 * be respectively equivalent to the previous use.
 * <pre>
 * {@literal @}Inject
 * Assemblers assemblers;
 * ...
 * ProductRepresentation productRepresentation = assemblers.assembleDtoFromAggregate(ProductRepresentation.class, productFromRepo);
 * ...
 * assemblers.mergeAggregateWithDto(productToMerge, productRepresentationSource);
 * </pre>
 *
 * @param <AGGREGATE_ROOT> the aggregate root type
 * @param <DTO>            the dto type
 * @author epo.jemba@ext.mpsa.com
 */
public abstract class BaseAssembler<AGGREGATE_ROOT extends AggregateRoot<?>, DTO extends Object>
                    extends AbstractBaseAssembler<AGGREGATE_ROOT, DTO, Class<AGGREGATE_ROOT>>  {

	private Class<AGGREGATE_ROOT> aggregateRootClass;

    /**
     * Default needed constructor.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public BaseAssembler() {
        Class<? extends BaseAssembler> class1 = (Class<? extends BaseAssembler>) SeedReflectionUtils.cleanProxy(getClass());
        this.aggregateRootClass = (Class<AGGREGATE_ROOT>) ((ParameterizedType) class1.getGenericSuperclass()).getActualTypeArguments()[0];
        dtoClass = (Class<DTO>) ((ParameterizedType) class1.getGenericSuperclass()).getActualTypeArguments()[1];
    }

    /**
     * Returns the type of the aggregate used by the assembler.
     *
     * @return a Class descendant of {@link AggregateRoot}
     * @see Assembler#getAggregateClass()
     */
    @Override
    public Class<AGGREGATE_ROOT> getAggregateClass() {
        return this.aggregateRootClass;
    }

    /**
     * This method is used by developers or by {@link Assemblers} to assemble a new DTO from the given aggregate.
     * <ul>
     * <li>It calls {@link AbstractBaseAssembler#newDto()} for the DTO creation
     * <li>and {@link #doAssembleDtoFromAggregate(Object, org.seedstack.business.api.domain.AggregateRoot)}
     * for the assembly algorithm.
     * </ul>
     * this Method adds data security. It intercepts and secures data according to the {@link DataSecurityService}.
     * @param sourceAggregate The aggregate from which create the DTO.
     * @return the assembled DTO
     * @see Assembler#assembleDtoFromAggregate(Object)
     */
	@Override
	public DTO assembleDtoFromAggregate(AGGREGATE_ROOT sourceAggregate) {
		DTO newDto = newDto();
		doAssembleDtoFromAggregate(newDto, sourceAggregate);

		return newDto;
	}

	@Override
	public void updateDtoFromAggregate(DTO sourceDto, AGGREGATE_ROOT sourceAggregate) {
		doAssembleDtoFromAggregate(sourceDto, sourceAggregate);
	}

    /**
     * This method has to be overridden by users to actually assemble the DTO from the aggregate.
     * <pre>
     * targetDto.fillProductId(sourceAggregate.getEntityId().getStoreId(),
     *     sourceAggregate.getEntityId().getProductCode());
     * targetDto.setName(sourceAggregate.getName());
     * targetDto.setDescription(sourceAggregate.getDescription());
     * </pre>
     * This method will be called by the public method
     * {@link #assembleDtoFromAggregate(org.seedstack.business.api.domain.AggregateRoot)}
     *
     * @param targetDto       the target dto
     * @param sourceAggregate the source aggregate
     */
    protected abstract void doAssembleDtoFromAggregate(DTO targetDto, AGGREGATE_ROOT sourceAggregate);

    /**
     * This method is used by developers or by {@link Assemblers} to actually merge the aggregate.
     * <p/>
     * It will call {@link #doMergeAggregateWithDto(org.seedstack.business.api.domain.AggregateRoot, Object)}, which
     * is overridden by developers.
     *
     * @param targetAggregate the target aggregate
     * @param sourceDto       the source dto
     * @see Assembler#mergeAggregateWithDto(Object, Object)
     */
    @Override
    public void mergeAggregateWithDto(AGGREGATE_ROOT targetAggregate, DTO sourceDto) {
        doMergeAggregateWithDto(targetAggregate, sourceDto);
    }

    /**
     * This method has to be overridden by users to actually merge an aggregate with the DTO.
     * <pre>
     * ...
     * targetAggregate.setName(sourceDto.getName());
     * targetAggregate.setDescription(sourceDto.getDescription());
     * ...
     * </pre>
     * This method will be called by the public method
     * {@link #mergeAggregateWithDto(org.seedstack.business.api.domain.AggregateRoot, Object)}.
     *
     * @param sourceDto       the source dto
     * @param targetAggregate the target aggregate
     */
    protected abstract void doMergeAggregateWithDto(AGGREGATE_ROOT targetAggregate, DTO sourceDto);

}
