package Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Accounts 
{
	long account_number;
	Scanner sc = new Scanner(System.in);
	Connection con = DBConnection.getCon();

	public long open_acc(String email) {
		if (!acc_exist(email)) {
			System.out.print("Enter Full Name: ");
			String full_name = sc.nextLine();
			System.out.print("Enter Initial Amount: ");
			double balance = sc.nextDouble();
			System.out.print("Enter Security Pin: ");
			int security_pin = sc.nextInt();

			try {
				account_number = generateAccNumber();
				PreparedStatement ps = con.prepareStatement("insert into accounts values(?,?,?,?,?)");

				ps.setLong(1, account_number);
				ps.setString(2, full_name);
				ps.setString(3, email);
				ps.setDouble(4, balance);
				ps.setInt(5, security_pin);

				int k = ps.executeUpdate();
				if (k > 0) {
					return account_number;
				} else {
					throw new RuntimeException("Account creation failed !!");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		throw new RuntimeException("Account Already Exist");
	}

	public long getAccount_number(String email) {
		try {
			PreparedStatement ps = con.prepareStatement("SELECT account_number from accounts WHERE email = ?");
			ps.setString(1, email);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				return resultSet.getLong("account_number");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Account Number Doesn't Exist!");
	}

	private long generateAccNumber() {
		try {
//			PreparedStatement ps = con
//					.prepareStatement("select account_number from accounts order by account_number desc limit 1");
			Statement st = con.createStatement();
//			ResultSet rs = st.executeQuery();
			int k=st.executeUpdate("select account_number from accounts order by account_number desc limit 1");
			if (k>0) {
				long last_acc_no = ((ResultSet) st).getLong("account_number");
				return last_acc_no + 1;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 10000100;
	}

	public boolean acc_exist(String email) {
		try {
			PreparedStatement ps = con.prepareStatement("select account_number from accounts where email=?");
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
