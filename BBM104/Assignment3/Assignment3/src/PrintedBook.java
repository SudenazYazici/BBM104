import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrintedBook extends Book{
    private int id;
    private LocalDate borrowedTime;
    private LocalDate mustReturnedTime;
    private boolean isReturned;
    private String stringBorrowed;
    private String stringRead;
    public static List<PrintedBook> printedBookList = new ArrayList<PrintedBook>();
    PrintedBook(int id){
        this.id = id;
        id = Book.getBookId();
        setBookId(Book.getBookId()+1);
        isReturned = true;
        stringBorrowed = null;
        stringRead = null;
    }
    public int getId() {
        return id;
    }
    public LocalDate getBorrowedTime() {
        return borrowedTime;
    }
    public LocalDate getMustReturnedTime() {
        return mustReturnedTime;
    }
    public boolean isReturned() {
        return isReturned;
    }

    public String getStringBorrowed() {
        return stringBorrowed;
    }

    public String getStringRead() {
        return stringRead;
    }

    public void setBorrowedTime(LocalDate borrowedTime) {
        this.borrowedTime = borrowedTime;
    }
    public void setMustReturnedTime(LocalDate mustReturnedTime) {
        this.mustReturnedTime = mustReturnedTime;
    }
    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    public void setStringBorrowed(String stringBorrowed) {
        this.stringBorrowed = stringBorrowed;
    }

    public void setStringRead(String stringRead) {
        this.stringRead = stringRead;
    }
}
