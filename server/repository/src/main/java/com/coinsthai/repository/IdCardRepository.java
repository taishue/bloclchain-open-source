package com.coinsthai.repository;

import com.coinsthai.model.IdCard;

/**
 * @author
 */
public interface IdCardRepository extends AbstractRepository<IdCard> {

    IdCard findByUserId(String userId);
}
