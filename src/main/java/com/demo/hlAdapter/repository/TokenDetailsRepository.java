package com.demo.hlAdapter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.demo.hlAdapter.entity.TokenDetails;

@Repository
public interface TokenDetailsRepository extends JpaRepository<TokenDetails, Long> {

}
