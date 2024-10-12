package com.pedrocosta.exchangelog.currency;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Currency implements Cloneable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true)
	private String code;
	private String name;

	public Currency() {}

	public Currency(String code) {
		this(code, null);
	}

	public Currency(String code, String name) {
		setCode(code);
		setName(name);
	}
	
	public long getId() {
		return id;
	}

	public Currency setId(long id) {
		this.id = id;
		return this;
	}

	public String getCode() {
		return code;
	}
	
	public Currency setCode(String code) {
		this.code = code;
		return this;
	}
	
	public String getName() {
		return name;
	}
	
	public Currency setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Currency currency = (Currency) o;
		return Objects.equals(code, currency.code);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, code, name);
	}

	@Override
	public Currency clone() throws CloneNotSupportedException {
		return (Currency) super.clone();
	}

	@Override
	public String toString() {
		return "Currency{" +
				"id=" + id +
				", code='" + code + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
