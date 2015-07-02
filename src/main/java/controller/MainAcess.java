package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import model.Parametro;
import model.Servico;

import org.apache.log4j.Logger;

import dao.ServicosDAO;

@Path("/{servico}/")
public class MainAcess {

	final static Logger LOG = Logger.getLogger(MainAcess.class);

	@GET
	@Produces("text/xml")
	public String controlador(@PathParam("servico") String servico,
			@Context UriInfo ui) {
		MultivaluedMap<String, String> queryParams = ui.getQueryParameters();

		String retorno = "";
		List<String> lParamNaoExiste = new ArrayList<String>();
		ServicosDAO servicoDAO = new ServicosDAO();

		Servico dbServico = servicoDAO.find(servico);

		// PROCURA SERVICO
		if (dbServico != null) {
			for (Entry<String, List<String>> p : queryParams.entrySet()) {
				boolean parametrosValido = false;
				for (Parametro dbParam : dbServico.getParametros()) {
					// PROCURA SE PARAMETTRO GET EXISTE NO SERVIÇO
					if (p.getKey().equals(dbParam.getChave())) {
						parametrosValido = true;
						break;
					}
				}
				// ADICIONA OS PARAMETROS NÃO ENCONTRADOS
				if (parametrosValido == false) {
					lParamNaoExiste.add(p.getKey());
				}
			}

			// EXIBE PARAMETROS NÃO ENCONTRADOS
			if (lParamNaoExiste.size() > 0) {
				for (String paramNaoExiste : lParamNaoExiste) {
					retorno += "Nao foi encontrado o parametro: '"
							+ paramNaoExiste + "'<br>";
				}
			} else {
				// SERVIÇO E PARAMETROS ENCONTRADOS
				retorno = servicoDAO.execView(dbServico.getAcao(), queryParams);
			}

		} else {
			retorno = "servico invalido";
		}
				
		return retorno;

	}

}
