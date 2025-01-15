package com.team3.devinit_back.profile.repository;

import com.team3.devinit_back.profile.entity.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {
    @Query("SELECT p FROM Profile p ORDER BY function('RAND')")
    List<Profile> findRandomProfiles(Pageable pageable);

    Optional<Profile> findByMemberId(String memberId);
}
