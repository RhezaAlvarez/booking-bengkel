package com.bengkel.booking.services;

import java.security.Provider.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.models.Vehicle;

public class BengkelService {
	private static Scanner input = new Scanner(System.in);
	private static String customerId = "";
	
	//Silahkan tambahkan fitur-fitur utama aplikasi disini
	
	//Login
	public static void login(List<Customer> listAllCustomers) {
		int countTry = 0;
		String password = "";
		System.out.println("Login");
		do {
			customerId = Validation.validasiInput("Masukan Customer Id : ", "!!! Format penulisan customer id salah, tidak boleh ada spasi", "[^ ]+");
			if(!Validation.validateCustomerId(listAllCustomers, customerId)){
				System.out.println("Customer Id Tidak Ditemukan atau Salah!");
			}
			else{
				password = Validation.validasiInput("Masukan Password : ", "!!! Format penulisan password salah", "[^ ]+");
				if(!Validation.validatePassword(listAllCustomers, customerId, password)){
					System.out.println("Password yang anda Masukan Salah!");
				}
				else{
					MenuService.mainMenu();
				}
			}
			countTry++;
		} while (countTry != 3 && (!Validation.validateCustomerId(listAllCustomers, customerId) || !Validation.validatePassword(listAllCustomers, customerId, password)));

		MenuService.isLooping = false;
		MenuService.run();
	}
	
	//Info Customer
	public static void customerInfoService(List<Customer> listAllCustomers){
		int num = 1;
		for (Customer customer : listAllCustomers) {
			if(customer.getCustomerId().equals(customerId)){
				if(customer instanceof MemberCustomer){
					customer = (MemberCustomer) customer;
					System.out.println("Customer id 	: " + customer.getCustomerId());
					System.out.println("Customer status	: Member");
					System.out.println("Alamat 			: " + customer.getAddress());
					System.out.println("Saldo Koin 		: " + ((MemberCustomer)customer).getSaldoCoin());
					System.out.println("List Kendaraan");
					System.out.println("+================================================================================================+");
					System.out.printf("| %-2s | %-15s | %-10s | %-15s | %-5s |\n", "No", "Vehicle Id", "Warna", "Tipe Kendaraan", "Tahun");
					System.out.println("+================================================================================================+");
					for (Vehicle vehicle : customer.getVehicles()) {
						System.out.printf("| %-2s | %-15s | %-10s | %-15s | %-5s |\n", num, vehicle.getVehiclesId(), vehicle.getColor(), vehicle.getVehicleType(), vehicle.getYearRelease());
						num++;
					}
					System.out.println("+================================================================================================+");
				}
				else{
					System.out.println("Customer id 	: " + customer.getCustomerId());
					System.out.println("Customer status	: Non Member");
					System.out.println("Alamat 			: " + customer.getAddress());
					System.out.println("List Kendaraan");
					System.out.println("+================================================================================================+");
					System.out.printf("| %-2s | %-15s | %-10s | %-15s | %-5s |\n", "No", "Vehicle Id", "Warna", "Tipe Kendaraan", "Tahun");
					System.out.println("+================================================================================================+");
					for (Vehicle vehicle : customer.getVehicles()) {
						System.out.printf("| %-2s | %-15s | %-10s | %-15s | %-5s |\n", num, vehicle.getVehiclesId(), vehicle.getColor(), vehicle.getVehicleType(), vehicle.getYearRelease());
						num++;
					}
					System.out.println("+================================================================================================+");
				}
			}	
		}
	}

	//Booking atau Reservation
	public static void booking(List<Customer> listAllCustomers, List<ItemService> listAllItemService, List<BookingOrder> listBookingOrder){
		String vehicleId = "";
		Vehicle vehicleTemp;
		Customer customerTemp = getCustomerByCustomerId(listAllCustomers);
		System.out.println("Booking Bengkel");
		do {
			vehicleId = Validation.validasiInput("Silahkan masukan Vehicle Id :\n", "!!! Format penulisan vechicle id salah, tidak boleh ada spasi", "[^ ]+");
			System.out.println(customerTemp);
			System.out.println(vehicleId);
			if(!Validation.validateVehicleId(customerTemp, vehicleId)){
				System.out.println("Kendaraan tidak ditemukan");
			}
		} while (!Validation.validateVehicleId(customerTemp, vehicleId));
		
		vehicleTemp = getVechicleByVehicleId(customerTemp, vehicleId);
		System.out.println(vehicleTemp);
		PrintService.showServices(listAllItemService, vehicleTemp.getVehicleType());

		int countServices = 0;
		String serviceId = "";
		String addMoreService = "";
		List<ItemService> listItemServicesTemp = new ArrayList<>();
		double listItemServicesTempTotalPrice = 0;
		BookingOrder bookingOrderTemp;
		do {
			do {
				serviceId = Validation.validasiInput("Silahkan masukan Service Id :\n", "!!! Format penulisan Service id salah, tidak boleh ada spasi", "[^ ]+");
				if(!Validation.validateServiceId(listAllItemService, serviceId, vehicleTemp)){
					System.out.println("Service tersebut tidak tersedia");
				}
			} while (!Validation.validateServiceId(listAllItemService, serviceId, vehicleTemp));

			listItemServicesTemp.add(getItemServiceByServiceId(listAllItemService, serviceId));
			countServices++;
			addMoreService = Validation.validasiInput("Apakah anda ingin menambahkan Service lainnya? (Y/T)\n", "!!! Aksi tersebut tidak tersedia, hanya ada opsi \"Y/T\"", "[y|Y|t|T]");
		} while (countServices != customerTemp.getMaxNumberOfService() && addMoreService.equalsIgnoreCase("Y"));

		listItemServicesTempTotalPrice = calculateListItemServicesTempTotalPrice(listItemServicesTemp);
		
		String paymentMethod = "";
		do {
			do {
				System.out.println("Silahkan pilih metode pembayaran (Saldo Coin atau Cash)");
				paymentMethod = input.nextLine();
			} while (!Validation.validatePaymentMethod(paymentMethod));
		} while (!Validation.validatePayment(paymentMethod, listAllCustomers, customerId, listItemServicesTempTotalPrice));

		
		bookingOrderTemp = new BookingOrder(customerTemp, listItemServicesTemp, paymentMethod, listItemServicesTempTotalPrice);
		listBookingOrder.add(bookingOrderTemp);

		System.out.println("Booking berhasil!!!");
		System.out.println("Total Harga Service : " + listItemServicesTempTotalPrice);
		System.out.println("Total Pembayaran : " + bookingOrderTemp.getTotalPayment());
	}

	//Top Up Saldo Coin Untuk Member Customer
	public static void topUpSaldoCoin(List<Customer> listAllCustomers){
		double topUpValue = 0;
		for (Customer customer : listAllCustomers) {
			if(customer.getCustomerId().equals(customerId) && customer instanceof MemberCustomer){
				topUpValue = Double.valueOf(Validation.validasiInput("Masukan besaran Top Up : \n", "Nilai top up harus berupa angka", "[0-9]+"));
				((MemberCustomer)customer).setSaldoCoin(((MemberCustomer)customer).getSaldoCoin()+topUpValue);
			}
			else{
				System.out.println("!!! Fitur ini hanya untuk Member saja");
			}
		}
	}

	//Show booking order
	public static void showBookingOrder(List<BookingOrder> listBookingOrder){
		PrintService.showBookingOrder(listBookingOrder, customerId);
	}

	public static Customer getCustomerByCustomerId(List<Customer> listAllCustomers){
		return listAllCustomers.stream().filter(customer -> customer.getCustomerId().equals(customerId)).findFirst().orElse(null);
	}

	public static Vehicle getVechicleByVehicleId(Customer customer, String vehicleId){
		return customer.getVehicles().stream().filter(vehicle -> vehicle.getVehiclesId().equals(vehicleId)).findFirst().orElse(null);
	}

	public static ItemService getItemServiceByServiceId(List<ItemService> listAllItemService, String serviceId){
		return listAllItemService.stream().filter(itemService -> itemService.getServiceId().equals(serviceId)).findFirst().orElse(null);
	}

	public static double calculateListItemServicesTempTotalPrice(List<ItemService> listItemServicesTemp){
		return listItemServicesTemp.stream().mapToDouble(itemService -> itemService.getPrice()).sum();
	}
}
