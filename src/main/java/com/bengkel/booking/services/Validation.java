package com.bengkel.booking.services;

import java.util.List;
import java.util.Scanner;

import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.models.Vehicle;


public class Validation {
	
	public static String validasiInput(String question, String errorMessage, String regex) {
	    Scanner input = new Scanner(System.in);
	    String result;
	    boolean isLooping = true;
	    do {
			System.out.print(question);
			result = input.nextLine();

			//validasi menggunakan matches
			if (result.matches(regex)) {
				isLooping = false;
			}else {
				result = "";
				System.out.println(errorMessage);
			}

	    } while (isLooping);

	    return result;
	}
	
	public static int validasiNumberWithRange(String question, String errorMessage, String regex, int max, int min) {
	    int result;
	    boolean isLooping = true;
	    do {
	      result = Integer.valueOf(validasiInput(question, errorMessage, regex));
	      if (result >= min && result <= max) {
	        isLooping = false;
	      }else {
	        System.out.println("Pilihan angka " + min + " s.d " + max);
	      }
	    } while (isLooping);

	    return result;
	}

	public static boolean validateCustomerId(List<Customer> listAllCustomers, String customerId){
		return listAllCustomers.stream().anyMatch(customer -> customer.getCustomerId().equals(customerId));
	}

	public static boolean validatePassword(List<Customer> listAllCustomers, String customerId, String password){
		return listAllCustomers.stream().filter(customer -> customer.getCustomerId().equals(customerId)).anyMatch(customer -> customer.getPassword().equals(password));
	}

	public static boolean validateVehicleId(Customer customerTemp, String vehicleId){
		return customerTemp.getVehicles().stream().anyMatch(vehicle -> vehicle.getVehiclesId().equals(vehicleId));
	}

	public static boolean validateServiceId(List<ItemService> listAllItemService, String serviceId, Vehicle vehicleTemp){
		return listAllItemService.stream().filter(service -> service.getVehicleType().equals(vehicleTemp.getVehicleType())).anyMatch(service -> service.getServiceId().equals(serviceId));
	}

	public static boolean validatePaymentMethod(String paymentMethod){
		boolean value = false;

		if(paymentMethod.equalsIgnoreCase("Saldo Coin") || paymentMethod.equalsIgnoreCase("Cash")){
			value = true;
		}
		else{
			System.out.println("!!! Opsi tersebut tidak tersedia");
		}

		return value;
	}
	
	public static boolean validatePayment(String paymentMethod, List<Customer> listAllCustomers, String customerId, double bookingOrderTotalPayment){
		boolean value = false;
		
		for (Customer customer : listAllCustomers) {
			if(customer.getCustomerId().equalsIgnoreCase(customerId)){
				if(customer instanceof MemberCustomer){
					double saldoCoinTemp = ((MemberCustomer)customer).getSaldoCoin();
					if(paymentMethod.equalsIgnoreCase("Saldo coin") && bookingOrderTotalPayment <= saldoCoinTemp){
						value = true;
						((MemberCustomer)customer).setSaldoCoin(saldoCoinTemp - bookingOrderTotalPayment);
					}
					else if(paymentMethod.equalsIgnoreCase("Saldo coin") && bookingOrderTotalPayment > ((MemberCustomer)customer).getSaldoCoin()){
						System.out.println("!!! Saldo Coin anda tidak mencukupi, harap gunakan metode pembayaran lain");
					}
					else{
						value = true;
					}
				}
				else{
					if(paymentMethod.equalsIgnoreCase("Saldo coin")){
						System.out.println("!!! Metode pembayaran ini hanya khusus untuk member saja");
						break;
					}
					else{
						value = true;
					}
				}
			}
		}

		return value;
	}
}
