import java.util.ArrayList;
import java.util.List;

public class Student extends Member{
    private int id;
    private List<Book> borrowedBooks;
    private List<Book> extendedBooks;
    public static List<Student> studentList = new ArrayList<Student>();
    Student(int id){
        this.id = id;
        id = Member.getMemberId();
        setMemberId(Member.getMemberId()+1);
        borrowedBooks = new ArrayList<>();
        extendedBooks = new ArrayList<>();
    }
    public int getId() {
        return id;
    }
    public void addToBorrowedList(Book book){
        borrowedBooks.add(book);
    }
    public void removeFromBorrowedList(Book book){
        borrowedBooks.remove(book);
    }
    public List<Book> getBorrowedBooks(){
        return borrowedBooks;
    }
    public void addToExtendedList(Book book){
        extendedBooks.add(book);
    }
    public void removeFromExtendedList(Book book){
        extendedBooks.remove(book);
    }
    public List<Book> getExtendedBooks(){
        return extendedBooks;
    }

}
