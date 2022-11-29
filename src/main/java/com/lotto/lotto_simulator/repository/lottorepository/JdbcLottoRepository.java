package com.lotto.lotto_simulator.repository.lottorepository;

import com.lotto.lotto_simulator.entity.Lotto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcLottoRepository {
    private final JdbcTemplate jdbcTemplate;

        public void batchInsertLottos(List<Lotto> lottos) {
            String sql = "INSERT INTO lotto " +
                    "(first_num, second_num, third_num, fourth_num, fifth_num, sixth_num, unique_code, store_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Lotto lotto = lottos.get(i);
    //            ps.setLong(1, lotto.getLotto_id());
                ps.setLong(1, lotto.getFirstNum());
                ps.setLong(2, lotto.getSecondNum());
                ps.setLong(3, lotto.getThirdNum());
                ps.setLong(4, lotto.getFourthNum());
                ps.setLong(5, lotto.getFifthNum());
                ps.setLong(6, lotto.getSixthNum());
                ps.setString(7,lotto.getUniqueCode());
                ps.setLong(8, lotto.getStore().getId());
            }

            @Override
            public int getBatchSize() {
                return lottos.size();
            }
        });
    }
}
