package dao;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import model.Servico;
import dao.DataSourceJTA;

public class ServicosDAO {

	final Logger LOG = Logger.getLogger(ServicosDAO.class);

	public ByteArrayInputStream execProcedure(HashMap<String, String> filtro,
			Servico service) {
		EntityManager entityManager = null;
		String xml = "";
		ByteArrayInputStream xmlByte = null;
		try {

			try {

				// EXISTIR FILTRO
				if (filtro.values().size() > 0) {
					entityManager = DataSourceJTA.load();
					LOG.info("Data source criado com sucesso");

					// REFERENCIA PROCEDIMENTO
					StoredProcedureQuery storedProcedure = entityManager
							.createStoredProcedureQuery(service.getAcao());

					// SETA PARAMETROS ENTRADA

					for (Entry<String, String> p : filtro.entrySet()) {
						storedProcedure.registerStoredProcedureParameter(
								p.getKey(), String.class, ParameterMode.IN);
						storedProcedure.setParameter(p.getKey(), p.getValue());
					}

					// SETA PARAMETRO DE SAIDA
					storedProcedure.registerStoredProcedureParameter("xml",
							String.class, ParameterMode.OUT);

					// EXECUTA
					storedProcedure.execute();
					xml = (String) storedProcedure
							.getOutputParameterValue("xml");

					entityManager.close();
				} else {
					xml = "<error>Não existe filtro</error>";
				}

				xmlByte = new ByteArrayInputStream(xml.getBytes("UTF-8"));

			} catch (Exception e) {
				LOG.error("Erro ao inserir serviço", e);
			}
		} catch (Exception e) {
			LOG.error("Erro ao carregar data source", e);
		}
		return xmlByte;
	}

	public Servico find(String uri) {
		EntityManager entityManager = null;
		Servico servico = null;
		ByteArrayInputStream xmlByte = null;
		try {

			try {

				entityManager = DataSourceJTA.load();
				LOG.info("Data source criado com sucesso");

				String consulta = "select s from Servico s where s.status = 1 and s.uri = :uri";
				TypedQuery<Servico> query = entityManager.createQuery(consulta,Servico.class);
				query.setParameter("uri", uri);
				
				try {
					servico = query.getSingleResult();
				} catch (NoResultException e) {
					return null;
				}

				entityManager.close();

			} catch (Exception e) {
				LOG.error("Erro ao buscar serviço", e);
				entityManager.close();
			}
		} catch (Exception e) {
			LOG.error("Erro ao carregar data source", e);
		}
		return servico;
	}
	
	public String execView(String uri, MultivaluedMap<String, String> hParam) {
		EntityManager entityManager = null;
		String xml = "";
		try {

			try {

				entityManager = DataSourceJTA.load();
				LOG.info("Data source criado com sucesso");
				
				String variavel = "";
				
				for (Entry<String, List<String>> p : hParam.entrySet()) {
					variavel = p.getValue().get(0);
				}
				
				Query query = entityManager.createNativeQuery("SELECT * FROM "+uri+"('"+variavel+"')");
				
				List lResult = query.getResultList(); 
				
				if(lResult.size() > 0){
					xml = (String) lResult.get(0);
				}
								
				entityManager.close();

			} catch (Exception e) {
				LOG.error("Erro ao realizar consulta", e);
				entityManager.close();
			}
		} catch (Exception e) {
			LOG.error("Erro ao carregar data source", e);
		}
		return xml;
	}

}
