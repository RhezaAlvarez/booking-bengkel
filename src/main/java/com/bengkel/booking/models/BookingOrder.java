package com.bengkel.booking.models;

import java.util.Date;
import java.util.List;
import com.bengkel.booking.interfaces.IBengkelPayment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingOrder implements IBengkelPayment{
	private static int idNum = 1;
	private String bookingId;
	private Customer customer;
	private List<ItemService> services;
	private String paymentMethod;
	private double totalServicePrice;
	private double totalPayment;

	public BookingOrder(Customer customer, List<ItemService> services, String paymentMethod, double totalServicePrice) {
		this.customer = customer;
		this.services = services;
		this.paymentMethod = paymentMethod;
		this.totalServicePrice = totalServicePrice;
		this.bookingId = generateBookingId();
		calculatePayment();
	}

	@Override
	public void calculatePayment() {
		double discount = 0;
		if (paymentMethod.equalsIgnoreCase("Saldo Coin")) {
			discount = getTotalServicePrice() * RATES_DISCOUNT_SALDO_COIN;
		}else {
			discount = getTotalServicePrice() * RATES_DISCOUNT_CASH;
		}
		
		setTotalPayment(getTotalServicePrice() - discount);
	}

	public String generateBookingId(){
		String bookingIdTemp = "";
		if(idNum < 10){
			bookingIdTemp = "Book-" + getCustomer().getCustomerId() + "00" + idNum;
		}
		else if(idNum < 100){
			bookingIdTemp = "Book-" + getCustomer().getCustomerId() + "0" + idNum;
		}
		else{
			bookingIdTemp = "Book-" + getCustomer().getCustomerId() + idNum;
		}
		idNum++;
		return bookingIdTemp;
	} 
}
