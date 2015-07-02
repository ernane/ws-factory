package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name="parametros")
public class Parametro {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private int id;
	@OneToOne
	private Servico servico;
	private String chave;
		
	public Parametro(){
	}
	
	public Parametro(Servico servico, String chave) {
		this.servico = servico;
		this.chave = chave;
	}
	
	public String getChave() {
		return chave;
	}
	
	public void setChave(String chave) {
		this.chave = chave;
	}

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	
		
	
}
