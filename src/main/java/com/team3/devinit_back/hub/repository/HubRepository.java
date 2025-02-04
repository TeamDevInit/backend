package com.team3.devinit_back.hub.repository;

import com.team3.devinit_back.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HubRepository extends JpaRepository<Profile, String>, HubCustomRepository {
}