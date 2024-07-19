package Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountsManager 
{
	
    Scanner sc=new Scanner(System.in);
    Connection con= DBConnection.getCon();
    
    public void credit_money(long account_number) throws SQLException
    {
    	System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
       
        System.out.print("Enter Security Pin: ");
        Integer security_pin = sc.nextInt();
    
    
        try 
        {
    	con.setAutoCommit(false);
    	PreparedStatement ps=con.prepareStatement("select * from accounts where account_number=? and security_pin=?");
        ps.setLong(1, account_number);
        ps.setInt(2, security_pin);
        
        ResultSet rs= ps.executeQuery();
         if(rs.next()) 
         {
        	 PreparedStatement ps1= con.prepareStatement("update accounts set balance=balance + ? where account_number=?");
        	 ps1.setDouble(1, amount);
        	 ps1.setLong(2, account_number);
        	 
        	 int k=ps.executeUpdate();
        	 if(k>0)
        	 {
        		 System.out.println();System.out.println("Rs."+amount+" credited Successfully");
                 con.commit();
                 con.setAutoCommit(true);
                 return;
             } else {
                 System.out.println("Transaction Failed!");
                 con.rollback();
                 con.setAutoCommit(true);
        	 }
         }
         else
         {
             System.out.println("Invalid Security Pin!");
         }
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
		con.setAutoCommit(true);
		
    }
    
    public void debit_money(long account_number) throws SQLException 
    {
    	System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = sc.nextLine();
    
        try
        {
        	con.setAutoCommit(false);
        	if(account_number!=0)
        	{
        		PreparedStatement ps=con.prepareStatement("select * from accounts where account_number =? and security_pin =?");
        		ps.setLong(1, account_number);
        		ps.setString(2, security_pin);
    			
        		ResultSet rs=ps.executeQuery();
    		if(rs.next()) 
    		{
    			double curr_bal=rs.getDouble("balance");
    			if(amount<=curr_bal)
    			{
    				PreparedStatement ps1=con.prepareStatement("update accounts set balance=balance-? where account_number=?");
    				ps1.setDouble(1, amount);
    				ps1.setLong(2, account_number);
    				int k=ps1.executeUpdate();
    				if(k>0) {
    					System.out.println("Rs."+amount+" debited Successfully");
                        con.commit();
                        con.setAutoCommit(true);
                        return;
    				}
    				else {
                        System.out.println("Transaction Failed!");
                        con.rollback();
                        con.setAutoCommit(true);
                    }
    			}
    			else
    			{
                    System.out.println("Insufficient Balance!");
                }
    		}
    		else 
    		{
    			System.out.println("Transaction Failed!");
                con.rollback();
                con.setAutoCommit(true);
    		}
    	}
    	
    }
    catch(Exception e) {
    	e.printStackTrace();
    }
        con.setAutoCommit(true);
    }
    
    public void transfer_money(long sender_account_number) throws SQLException
    {
    	System.out.print("Enter Receiver Account Number: ");
        long receiver_account_number = sc.nextLong();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = sc.nextLine();
        
        try
        {
        	con.setAutoCommit(false);
            if(sender_account_number!=0 && receiver_account_number!=0)
            {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ? ");
                ps.setLong(1, sender_account_number);
                ps.setString(2, security_pin);
                ResultSet rs= ps.executeQuery();
                
                if(rs.next()) {
                	double current_balance=rs.getDouble("balance");
                	if(amount<=current_balance)
                	{
                		PreparedStatement cps=con.prepareStatement("UPDATE Accounts SET balance = balance - ? WHERE account_number = ?");
                		PreparedStatement dps=con.prepareStatement("UPDATE Accounts SET balance = balance - ? WHERE account_number = ?");
                		cps.setDouble(1, amount);
                		cps.setLong(2, receiver_account_number);
                		dps.setDouble(1, amount);
                		dps.setLong(2, sender_account_number);
                	
                		int rs1=dps.executeUpdate();
                		int rs2=cps.executeUpdate();
                	
                		if(rs1>0 && rs2>0)
                		{
                			System.out.println("Transaction Successful!");
                			System.out.println("Rs."+amount+" Transferred Successfully");
                			con.commit();
                			con.setAutoCommit(true);
                			return;
                		}
                		else 
                		{
                			System.out.println("Transaction Failed");
                			con.rollback();
                			con.setAutoCommit(true);
                		}
                	}
                	else
                	{
                		System.out.println("Insufficient Balance!");
                	}
                }
                else
                {
                	System.out.println("Invalid Security Pin!");
                }
            }
            else
            {
                System.out.println("Invalid account number");
            }
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        con.setAutoCommit(true);
    }
    
    public void getBalance(long account_number)
    {
    	System.out.print("Enter Security Pin: ");
        String security_pin = sc.nextLine();
        try
        {
            PreparedStatement ps=con.prepareStatement("SELECT balance FROM Accounts WHERE account_number = ? AND security_pin = ?");
            ps.setLong(1, account_number);
            ps.setString(2, security_pin);
            
            ResultSet rs=ps.executeQuery();
            if(rs.next()) {
            	double balance = rs.getDouble("balance");
                System.out.println("Balance: "+balance);
            }else{
                System.out.println("Invalid Pin!");
            }
        }
        catch(Exception e) 
        {
        	e.printStackTrace();
        }
    }
}