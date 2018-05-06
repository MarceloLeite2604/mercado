package org.marceloleite.mercado.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import org.marceloleite.mercado.strategy.StrategyExecutor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Entity
@Table(name = "ACCOUNTS")
@JsonIgnoreProperties({ "id" })
@JsonPropertyOrder({ "owner", "email", "tapiInformation", "wallet", "withdrawals", "strategies", "orders" })
@XmlRootElement(name = "account")
@XmlType(propOrder = { "owner", "email", "tapiInformation", "wallet", "withdrawals", "strategies", "orders" })
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
	@JsonProperty("wallet")
	private Wallet wallet;

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

	private List<StrategyExecutor> strategyExecutors;

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

	@XmlElementWrapper(name = "wallet")
	@XmlElement(name = "balance")
	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		wallet.forEach(balance -> balance.setAccount(this));
		this.wallet = wallet;
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
		wallet.add(balance);
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

		if (wallet != null) {
			for (Balance balance : wallet) {
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

	@XmlTransient
	public List<StrategyExecutor> getStrategyExecutors() {
		return Optional.ofNullable(strategyExecutors)
				.orElse(new ArrayList<>());
	}

	public boolean addStrategyExecutor(StrategyExecutor strategyExecutor) {
		List<StrategyExecutor> strategyExecutors = getStrategyExecutors();
		return strategyExecutors.add(strategyExecutor);
	}
}