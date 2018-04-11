package org.marceloleite.mercado.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Entity
@Table(name = "STRATEGIES")
@JsonIgnoreProperties({"id", "account"})
@JsonPropertyOrder({ "className", "parameters", "variables" })
@XmlRootElement(name = "strategy")
@XmlType(propOrder = { "className", "parameters", "variables" })
public class Strategy {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long Id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ACCO_ID", nullable = false, foreignKey = @ForeignKey(name = "STRA_ACCO_FK"))
	private Account account;

	@Column(name = "CLASS_NAME", length = 128, nullable = false)
	private String className;

	@OneToMany(mappedBy = "strategy", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@Fetch(FetchMode.SUBSELECT)
	private List<Parameter> parameters;

	@OneToMany(mappedBy = "strategy", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@Fetch(FetchMode.SUBSELECT)
	private List<Variable> variables;

	@XmlTransient
	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	@XmlTransient
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@XmlElement(name = "className")
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@XmlElementWrapper(name = "parameters")
	@XmlElement(name = "parameter", required = false)
	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		for (Parameter parameter : parameters) {
			parameter.setStrategy(this);
		}
		this.parameters = parameters;
	}

	@XmlElementWrapper(name = "variables")
	@XmlElement(name = "variables", required = false)
	public List<Variable> getVariables() {
		return variables;
	}

	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}
}
