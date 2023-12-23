package com.bengkel.booking.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.repositories.CustomerRepository;
import com.bengkel.booking.repositories.ItemServiceRepository;

public class MenuService {
	private static List<Customer> listAllCustomers = CustomerRepository.getAllCustomer();
	private static List<ItemService> listAllItemService = ItemServiceRepository.getAllItemService();
	private static List<BookingOrder> listBookingOrder = new ArrayList<>();
	private static Scanner input = new Scanner(System.in);
	private static String customerId = "";
	public static boolean isLooping = true;

	public static void run() {
		int inputNum;
		String[] listMenu = {"Login", "Exit"};
		if(isLooping){
			PrintService.printMenu(listMenu, "Aplikasi Booking Bengkel");
			do {
				inputNum = Validation.validasiNumberWithRange("Masukan aksi : ", "!!! Aksi tidak tersedia!", "[0-9]", 1, 0);
				switch (inputNum) {
					case 1:
						BengkelService.login(listAllCustomers);
						break;
					case 0:
						isLooping = false;
						break;
				}
			} while (isLooping);
			System.out.println("BYE");
		}
	}
	
	public static void mainMenu() {
		String[] listMenu = {"Informasi Customer", "Booking Bengkel", "Top Up Bengkel Coin", "Informasi Booking", "Logout"};
		int menuChoice = 0;
		boolean isLoopingMainMenu = true;
		
		do {
			PrintService.printMenu(listMenu, "Booking Bengkel Menu");
			menuChoice = Validation.validasiNumberWithRange("Masukan Pilihan Menu: ", "!!! Input Harus Berupa Angka", "^[0-9]+$", listMenu.length-1, 0);
			
			switch (menuChoice) {
				case 1:
					BengkelService.customerInfoService(listAllCustomers);
					break;
				case 2:
					//panggil fitur Booking Bengkel
					BengkelService.booking(listAllCustomers, listAllItemService, listBookingOrder);
					break;
				case 3:
					//panggil fitur Top Up Saldo Coin
					BengkelService.topUpSaldoCoin(listAllCustomers);
					break;
				case 4:
					//panggil fitur Informasi Booking Order
					BengkelService.showBookingOrder(listBookingOrder);
					break;
				case 0:
					customerId = "";
					System.out.println("Logout");
					isLoopingMainMenu  = false;
					run();
					break;
				default:
					System.out.println("Logout");
					isLoopingMainMenu  = false;
					break;
			}
		} while (isLoopingMainMenu );
	}

	//Silahkan tambahkan kodingan untuk keperluan Menu Aplikasi
}
