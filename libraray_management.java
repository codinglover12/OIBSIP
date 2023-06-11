import java.util.Scanner;
public class libraray_management {

    public static void main(String[] args) {
        char r;
        do{
            
        
        System.out.println("*******libraray_management system********");
        System.out.println("press 1 to add book");
        System.out.println("press 2 to issue book");
        System.out.println("press 3 to return a book");
        System.out.println("press 4 to print complete issue details ");
        Scanner obj1 = new Scanner(System.in);
        System.out.println("Enter any Number");
        int a = obj1.nextInt();
        switch(a){
            case 1:
            libraray aa =new libraray();
            aa.add();
            break;

            case 2:
            libraray bb =new libraray();
            bb.issue();
            break;

            case 3:
            libraray cc =new libraray();
            cc.ret();
            break;

            case 4:
            libraray issue =new libraray();
            issue.detail();
            break;

            case 5:
            libraray add =new libraray();
            add.exit();
            break;

            default:
            System.out.println("invalid number");
        }
            System.out.println("you want to select next option Y/N");
            r=obj1.next().charAt(0);
    }
            while(r=='y'|| r=='Y');
            if(r=='n'|| r=='N')
            {
                libraray z =new libraray();
                z.exit();

            }
      
       }

    }

    
    
 
class libraray{
    static String Name,date,b;
    static int a,c;
    
    void add(){

        System.out.println("Enter Book Name, Price and Book_No");
        Scanner obj2= new Scanner(System.in);
        String Name =obj2.nextLine();
        float Price =obj2.nextInt();
        int Book_No=obj2.nextInt();
System.out.println("Your details is " +Name+" "+  Price+" "+ Book_No );

    }




void issue(){
    Scanner obj3 = new Scanner(System.in);
    System.out.println("Book Name");
    Name=obj3.nextLine();
    System.out.println("Book_id");
    a=obj3.nextInt();
    obj3.nextLine();
    System.out.println("Issue Date");
    b= obj3.nextLine();
    System.out.println("Total Book Issued");
    c=obj3.nextInt();
    obj3.nextLine();
    System.out.println("Return Book Date");
    date=obj3.nextLine();
}

int getid(){
    return a;
}

void ret(){
    System.out.println("Enter Student_name & book_id");
    Scanner obj4 =new Scanner(System.in);
    Name=obj4.nextLine();
    int book_id=obj4.nextInt();
    if(a==book_id){

        System.out.println("All Deatils");
        System.out.println("Book Name ::" +libraray.Name);
        System.out.println("Book_id ::" +libraray.a);
        System.out.println("Issue Date ::" +libraray.b);
        System.out.println("Total Book Issued ::" +libraray.c);
        System.out.println("Return Date ::" +libraray.date);
        System.out.println("You have Returned the Book");
    }

    else{
        System.out.println("Wrong Id" +"\n" +"Please Enter Correct Id");
    }
} 

void detail(){
    System.out.println("Book Name ::" +libraray.Name);
        System.out.println("Book_id ::"  +libraray.a);
        System.out.println("Issue Date ::" +libraray.b);
        System.out.println("Total Book Issued::" +libraray.c);
        System.out.println("Return Date ::" +libraray.date);
}

void exit()
   {
     System.exit(0);
   }


}





















