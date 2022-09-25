package com.scheduler.poc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scheduler.poc.entity.ConfigItem;

public interface ConfigRepo extends JpaRepository<ConfigItem, String> {

}
