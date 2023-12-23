package com.bengkel.booking.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MemberCustomer extends Customer {
	private double saldoCoin;
	
	public MemberCustomer(String customerId, String name, String address, String password, List<Vehicle> vehicles, double saldoCoin) {
		super(customerId, name, address, password, vehicles);
		this.saldoCoin = saldoCoin;
		setMaxNumberOfService(2);
	}
}
