package com.kdev5.cleanpick.contract.infra;

import com.kdev5.cleanpick.contract.domain.Nominee;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class NomineeBulkRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<Nominee> nomineeList) {
        String sql = "INSERT INTO nominee (contract_id, manager_id, status, created_at) VALUES(?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, nomineeList, nomineeList.size(), (
                PreparedStatement ps, Nominee n) -> {
            ps.setLong(1, n.getContract().getId());
            ps.setLong(2, n.getManager().getId());
            ps.setString(3, n.getStatus().name());
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
        });
    }
}
