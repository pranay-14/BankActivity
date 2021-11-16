import java.sql.*;
import java.util.Scanner;

public class BankActivity
{
    Scanner scannerInt = new Scanner(System.in);
    public static final String URLTOCONNECT = "jdbc:mysql://localhost:3306/bank";
    public static final String USERNAME = "root";
    public static final String USERPASSWORD = "root";
    String qry;
    Connection dbCon;
    Statement theStatement;
    ResultSet theResultSet;
    PreparedStatement preparedStatement;

    BankActivity()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            dbCon = DriverManager.getConnection(URLTOCONNECT, USERNAME, USERPASSWORD);
            theStatement = dbCon.createStatement();
            System.out.println("Connected to the database now...");
        } catch (ClassNotFoundException e) {
            System.out.println("Can't load the driver : " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Can't connect to the database : " + e.getMessage());
        }
    }
    void showDetails()
    {
        qry = "select * from account";
        try {
            theResultSet = theStatement.executeQuery(qry);
            while(theResultSet.next())
            {
                Integer number = theResultSet.getInt("account_no");
                Integer balance = theResultSet.getInt("account_balance");
                String type = theResultSet.getString("account_type");
                String name = theResultSet.getString("userName");
                System.out.println(number + "," + balance + "," + type + "," + name );
            }
        } catch (SQLException e) {
            System.out.println("Can't retrive " + e.getMessage());
        }
    }
    void showBalance(Integer account_number)
    {
        qry = "select account_balance from account where account_no ="+account_number;
        try {
            theResultSet = theStatement.executeQuery(qry);
            while (theResultSet.next())
            {
                System.out.println("The balance in account is : " + theResultSet.getInt("account_balance"));
            }

        } catch (SQLException e) {
            System.out.println("account balance couldn't be retrived" + e.getMessage());
        }
    }
    void deposit(Integer account_number, Integer amount_to_deposit)
    {
        qry = "update account set account_balance = account_balance + " + amount_to_deposit + " where account_no = "+ account_number;
        try {
            if(theStatement.executeUpdate(qry) > 0)
            {
                System.out.println("The balance is updated");
                showBalance(account_number);
            }
        } catch (SQLException e) {
            System.out.println("Invalid sql "+ e.getMessage());
        }
    }
    void withdraw(Integer account_number, Integer amount_to_withdraw)
    {

    }
    void validate(String username, Integer password)
    {
        qry = "select * from user where userName='" + username + "' and userPin='" + password + "'";
        try {
            theResultSet = theStatement.executeQuery(qry);
            if (theResultSet.next())
            {
                System.out.println("Welcome to squid bank");
                int input;
                do
                {
                 System.out.println("1.Show details");
                 System.out.println("2.Show Balance");
                 System.out.println("3.Deposit");
                 System.out.println("4.Withdraw");
                 System.out.println("5.Fund Transfer");
                 System.out.println("6.Print Transactions");
                 System.out.println("7.Create Account");
                 System.out.println("Enter input from the above options : ");
                 input = scannerInt.nextInt();
                 switch (input)
                 {
                     case 0:
                         System.out.println("see you soon");
                         break;
                     case 1:
                         System.out.println("The details are");
                         showDetails();
                         break;
                     case 2:
                         System.out.println("Enter the account number ");
                         showBalance(scannerInt.nextInt());
                         break;
                     case 3:
                         System.out.println("Welcome to Deposit portal");
                         System.out.println("Enter the account number and amount to be deposited");
                         deposit(scannerInt.nextInt(),scannerInt.nextInt());
                         break;
                     case 4:
                         System.out.println("Welcome to Withdraw portal");
                         System.out.println("Enter the amount and account number you want to withdraw");
                         withdraw(scannerInt.nextInt(),scannerInt.nextInt());

                     default:
                         System.out.println("Entered input doesn't exists");
                 }
                }while(input!=0);
            }
            else
            {
                System.out.println("Invalid user name and password");
            }
        } catch (SQLException e) {
            System.out.println("Couldn't find here " + e.getMessage());
        }
    }
    void register(String username, Integer password)
    {
        qry = "insert into user (userName, userPin) values (?,?)";
        try {
            preparedStatement = dbCon.prepareStatement(qry);
            preparedStatement.setString(1,username);
            preparedStatement.setInt(2,password);
            if(preparedStatement.executeUpdate() > 0)
            {
                System.out.println("Registered Successfully and can login");
            }
        } catch (SQLException e) {
            System.out.println("There is error in query" + e.getMessage());
        }
    }
    public static void main(String[] args)
    {
        BankActivity bankActivity = new BankActivity();
        int ch;
        Scanner sc = new Scanner(System.in);
        Scanner scanner = new Scanner(System.in);
        do
        {
            System.out.println("*****Welcome to Bank*****");
            System.out.println("1.Login");
            System.out.println("2.Register");
            System.out.println("3.Exit");
            ch = sc.nextInt();
            switch (ch)
            {
                case 1:
                System.out.println("Enter the username and password : ");
                bankActivity.validate(scanner.nextLine(), sc.nextInt());
                break;
                case 2:
                System.out.println("Please register here");
                System.out.println("Enter the username and password to register : ");
                bankActivity.register(scanner.nextLine(), sc.nextInt());
                break;
                case 3:
                System.out.println("see you soon");
                break;
            }
        }while (ch!=3);
    }
}
