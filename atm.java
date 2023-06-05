import java.util.Scanner;

class ATM {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int balance = 10000;
        int withdraw;
        int deposit;
        int choice;
        int pin = 12345;

        System.out.println("WELCOME TO MY ATM MACHINE");
        System.out.println("Enter Your Atm PIN");
        int userinput= input.nextInt();
        while ( userinput != pin )
        {
          System.out.println("INCORRECT PIN" +""+ " Try Again");
          System.out.print("ENTER YOUR PIN: ");
          userinput = input.nextInt();
        }
    
        System.out.println("\nPIN ACCEPTED. YOU NOW HAVE ACCESS TO YOUR ACCOUNT.");
// Operations in the atm machine and we have used scanner class for taking input from user console

        while (true) {
            System.out.println("Welcome to the ATM Machine");
            System.out.println("1. Withdraw");
            System.out.println("2. Deposit");
            System.out.println("3. Check Balance");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            choice = input.nextInt();

            // We have used switch case for jump between multiple cases according to condition given
            // as well as break for stop 

            switch (choice) {
                case 1:
                    System.out.print("Enter amount to withdraw: ");
                    withdraw = input.nextInt();
                    if (balance >= withdraw) {
                        balance -= withdraw;
                        System.out.println("You have withdrawn " + withdraw + ". Your new balance is " + balance);
                    } else {
                        System.out.println("Insufficient balance");
                    }
                    break;

                case 2:
                    System.out.print("Enter amount to deposit: ");
                    deposit = input.nextInt();
                    balance += deposit;
                    System.out.println("You have deposited " + deposit + ". Your new balance is " + balance);
                    break;

                case 3:
                    System.out.println("Your balance is " + balance);
                    break;

                case 4:
                    System.exit(0);

                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }
}