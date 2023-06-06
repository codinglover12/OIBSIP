import java.util.Random;
import java.util.Scanner;

public class guesserGame{
public static void main(String[] args) {

    // Used Scanner For Taking User Input 
    Scanner sc = new Scanner(System.in);
    Random rand= new Random();
    // We Have Used Random function for generating random number.     

        int randomNumber=rand.nextInt();
        int Guess=0;
        int attempts=2;
        System.out.println("Welcome to the Number Guessing Game." + "\n" + 
         "This is a number guessing game between 1 to 100." +
         "\n"+ "Can You Guess the Number Exactly What it is !");
        
        System.out.print("Enter your guess number");
        Guess=sc.nextInt();

        // Here is the Problem Statement Using If and Else & While Conditions......
        if(Guess < randomNumber){
        System.out.println("Too Low! Try Again." );
        }


        else if(randomNumber > Guess){
        System.out.println("Too High! Try Again.");
        }


        else{
        System.out.println("Congratulations! You have guessed the number in" + attempts + "attempts.");
        }
        

        while(Guess != randomNumber);
        sc.close();

        }
        
     }

    