package Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class User 
{

	Scanner sc = new Scanner(System.in);
	Connection con = DBConnection.getCon();

	public void register() {
		System.out.print("Full Name: ");
		String full_name = sc.nextLine();
		System.out.print("Email: ");
		String email = sc.nextLine();
		System.out.print("Password: ");
		String password = sc.nextLine();
		
		if (user_exist(email)) {
			System.out.println("User Already Exists for this Email Address ||");
			return;
		}

		try {
			PreparedStatement ps = con.prepareStatement("insert into user1 values(?,?,?)");

			ps.setString(1, full_name);
			ps.setString(2, email);
			ps.setString(3, password);
			
			int k = ps.executeUpdate();
			if (k > 0) {
				System.out.println("Registration Successfull!");
			} else {
				System.out.println("Registration Failed!");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String login() 
	{
		System.out.print("Email: ");
		String email = sc.nextLine();
		System.out.print("Password: ");
		String password = sc.nextLine();
		sc.nextLine();
		
		try {
			PreparedStatement ps = con.prepareStatement("select * from user1 where email=? and password=?");
			ps.setString(1, email);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) 
			{
				return email;
				
			} 
			else 
			{
				return null;
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	public boolean user_exist(String email) {
		try {
			PreparedStatement ps = con.prepareStatement("select * from user1 where email=?");
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}