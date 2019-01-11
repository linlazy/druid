package com.example.druid;

import com.example.druid.example.dao.PlayerDAO;
import com.example.druid.example.entity.PlayerEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DruidApplicationTests {


	@Autowired
	private PlayerDAO playerDAO;
	@Autowired
	private DataSource dataSource;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	public void contextLoads() {
	}

	@Test
	public void getPlayerEntity() {
		long  actorId = 4194306;
		PlayerEntity playerEntity = playerDAO.getPlayerEntity(4194306);

	}

	@Test
	public void updatePlayerEntity() {
		PlayerEntity playerEntity = new PlayerEntity();
		playerEntity.setPlayerId(4194306);
		playerEntity.setHp(20);
		playerDAO.updateQueue(playerEntity);
	}

	@Test
	public void batchSqlUpdate() {
		String prepareSQL = "update player  set username=?,password=?,profession=?,hp=?,mp=? where playerId=?";

		int update = jdbcTemplate.update(prepareSQL, null, null, 0, 50, 0, 4194306);
		System.out.println(update);
	}


}

