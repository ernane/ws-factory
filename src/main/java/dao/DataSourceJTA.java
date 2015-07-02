package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DataSourceJTA {

	private static String DataSource = "wsFactoryDS";

	private static EntityManagerFactory entityManagerFactory = Persistence
			.createEntityManagerFactory(DataSource);

	public static EntityManager load() {
		EntityManager entityManager = entityManagerFactory
				.createEntityManager();
		return entityManager;
	}

}
