package com.kdev5.cleanpick.manager.infra.repository;

import com.kdev5.cleanpick.manager.domain.Manager;
import com.kdev5.cleanpick.manager.infra.querydsl.ManagerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long>, ManagerRepositoryCustom {

    public Optional<Manager> findByUserId(Long userId);

    public boolean existsById(Long id);

    @Query(value = """
                SELECT *
                FROM Manager m
                WHERE ST_Distance_Sphere(
                    POINT(m.longitude, m.latitude),
                    POINT(:lon, :lat)
                ) <= :radiusMeters
            """, nativeQuery = true)
    List<Manager> findManagersWithinRadius(
            @Param("lat") double lat,
            @Param("lon") double lon,
            @Param("radiusMeters") double radiusMeters
    );
}
