package org.marceloleite.mercado.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
import org.marceloleite.mercado.CurrencyAmount;
import org.marceloleite.mercado.commons.Currency;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Entity
@Table(name = "ACCOUNTS")
@JsonIgnoreProperties({ "id" })
@JsonPropertyOrder({ "owner", "email", "tapiInformation", "balances", "withdrawals", "strategies", "orders" })
@XmlRootElement(name = "account")
@XmlType(propOrder = { "owner", "email", "tapiInformation", "balances", "withdrawals", "strategies", "orders" })
public class Account {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	@Column(name = "OWNER", length = 64, nullable = false)
	private String owner;

	@Column(name = "EMAIL", length = 64, nullable = false)
	private String email;

	@OneToOne(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private TapiInformation tapiInformation;

	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<Balance> balances;

	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<Withdrawal> withdrawals;

	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@Fetch(FetchMode.SUBSELECT)
	private List<Strategy> strategies;

	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@Fetch(FetchMode.SUBSELECT)
	private List<Order> orders;

	@XmlTransient
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlElement
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@XmlElement
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@XmlElement
	public TapiInformation getTapiInformation() {
		return tapiInformation;
	}

	public void setTapiInformation(TapiInformation tapiInformation) {
		this.tapiInformation = tapiInformation;
	}

	@XmlElementWrapper(name = "balances")
	@XmlElement
	public List<Balance> getBalances() {
		return balances;
	}

	public void setBalances(List<Balance> balances) {
		this.balances = balances;
	}

	@XmlElementWrapper(name = "withdrawals")
	@XmlElement(required = false)
	public List<Withdrawal> getWithdrawals() {
		return withdrawals;
	}

	public void setWithdrawals(List<Withdrawal> withdrawals) {
		this.withdrawals = withdrawals;
	}

	@XmlElementWrapper(name = "strategies")
	@XmlElement
	public List<Strategy> getStrategies() {
		return strategies;
	}

	public void setStrategies(List<Strategy> strategies) {
		this.strategies = strategies;
	}

	@XmlElementWrapper(name = "orders")
	@XmlElement
	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public void addStrategy(Strategy strategy) {
		strategy.setAccount(this);
		strategies.add(strategy);
	}

	public void addBalance(Balance balance) {
		balance.setAccount(this);
		balances.add(balance);
	}

	public void addWithdrawal(Withdrawal withdrawal) {
		withdrawal.setAccount(this);
		withdrawals.add(withdrawal);
	}

	public void addOrder(Order order) {
		order.setAccount(this);
		orders.add(order);
	}

	public void adjustReferences() {

		if (tapiInformation != null) {
			tapiInformation.setAccount(this);
		}

		if (balances != null) {
			for (Balance balance : balances) {
				balance.setAccount(this);
			}
		}

		if (withdrawals != null) {
			for (Withdrawal withdrawal : withdrawals) {
				withdrawal.setAccount(this);
			}
		}

		if (strategies != null) {
			for (Strategy strategy : strategies) {
				strategy.setAccount(this);
				strategy.adjustReferences();
			}
		}

		if (orders != null) {
			for (Order order : orders) {
				order.setAccount(this);
				order.adjustReferences();
			}
		}
	}

	public boolean hasPositiveBalanceOf(Currency currency) {
		BigDecimal balance = getBalanceFor(currency);
		return (balance.compareTo(BigDecimal.ZERO) > 0);
	}

	public boolean hasBalanceFor(CurrencyAmount currencyAmount) {
		return (getBalanceFor(currencyAmount.getCurrency()).compareTo(currencyAmount.getAmount()) >= 0);
	}
	
	public BigDecimal getBalanceFor(Currency currency) {
		balances.stream()
				.collect(Collectors.toMap(Balance::getCurrency, Balance::getAmount))
				.getOrDefault(currency, new BigDecimal("0"));
		return null;
	}
}