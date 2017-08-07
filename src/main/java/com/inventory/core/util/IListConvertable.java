package com.inventory.core.util;

import java.util.List;

/**
 * @author manohar-td-003
 *
 * @param <E>
 * @param <D>
 */
public interface IListConvertable<E, D> {

	/***
	 * Convert Entity List to DTO list
	 * 
	 * @param entities
	 * @return
	 */
    List<D> convertToDtoList(List<E> entities);

	/***
	 * Convert DTO List to Entity List
	 * 
	 * @param dtoList
	 * @return
	 */
    List<E> convertToEntityList(List<D> dtoList);

	
}
