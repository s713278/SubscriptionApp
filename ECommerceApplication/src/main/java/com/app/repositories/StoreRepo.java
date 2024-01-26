package com.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entites.Store;

public interface StoreRepo extends JpaRepository<Store, Long> {

}
