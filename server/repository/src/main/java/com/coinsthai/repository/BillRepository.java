package com.coinsthai.repository;

import com.coinsthai.model.Bill;
import com.coinsthai.pojo.intenum.BillStatus;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author
 */
public interface BillRepository extends AbstractRepository<Bill>, BillCustomRepository {

}
