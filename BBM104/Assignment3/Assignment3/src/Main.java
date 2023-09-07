import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.DAYS;
public class Main {
    public static String writePath;
    public static void main(String[] args) {
        writePath = args[1];
        FileOutput.writeToFile(writePath,"",false,false); // to initialize empty output file
        FileInput.readFile(args[0], true, true);
        for (int i=0;i<FileInput.lines.size();i++) { // iterating over every line of input file
            String[] temp = FileInput.lines.get(i).split("\t");
            switch (temp[0]){
                case "addBook":
                    if (temp[1].equals("P")){
                        writeOutput("Created new book: Printed [id: "+Book.getBookId()+"]");
                        PrintedBook.printedBookList.add(new PrintedBook(Book.getBookId()));
                    } else if (temp[1].equals("H")) {
                        writeOutput("Created new book: Handwritten [id: "+Book.getBookId()+"]");
                        HandwrittenBook.handwrittenBookList.add(new HandwrittenBook(Book.getBookId()));
                    }
                    break;
                case "addMember":
                    if (temp[1].equals("S")){
                        writeOutput("Created new member: Student [id: "+Member.getMemberId()+"]");
                        Student.studentList.add(new Student(Member.getMemberId()));
                    } else if (temp[1].equals("A")) {
                        writeOutput("Created new member: Academic [id: "+Member.getMemberId()+"]");
                        Academic.academicList.add(new Academic(Member.getMemberId()));
                    }
                    break;
                case "borrowBook":
                    for (HandwrittenBook handwrittenBook:HandwrittenBook.handwrittenBookList){
                        if (Integer.parseInt(temp[1]) == handwrittenBook.getId()){
                            writeOutput("You cannot borrow this book!");
                            break;
                        }
                    }
                    for (Student student:Student.studentList){
                        if (Integer.parseInt(temp[2]) == student.getId()){
                            if (student.getBorrowedBooks() != null && student.getBorrowedBooks().size() == 2){
                                writeOutput("You have exceeded the borrowing limit!");
                                break;
                            }
                            for (PrintedBook printedBook:PrintedBook.printedBookList){
                                if (Integer.parseInt(temp[1]) == printedBook.getId()){
                                    if(!printedBook.isReturned()){
                                        writeOutput("You cannot borrow this book!");
                                        break;
                                    }
                                    printedBook.setBorrowedTime(LocalDate.parse(temp[3]));
                                    printedBook.setReturned(false);
                                    printedBook.setMustReturnedTime(printedBook.getBorrowedTime().plusWeeks(1));
                                    writeOutput("The book ["+printedBook.getId()+"] was borrowed by member ["+student.getId()+"] at "+printedBook.getBorrowedTime());
                                    student.addToBorrowedList(printedBook);
                                    printedBook.setStringBorrowed("The book ["+printedBook.getId()+"] was borrowed by member ["+student.getId()+"] at "+printedBook.getBorrowedTime());
                                }
                            }

                        }

                    }
                    for (Academic academic:Academic.academicList) {
                        if (Integer.parseInt(temp[2]) == academic.getId()) {
                            if (academic.getBorrowedBooks() != null && academic.getBorrowedBooks().size() == 4){
                                writeOutput("You have exceeded the borrowing limit!");
                                break;
                            }
                            for (PrintedBook printedBook : PrintedBook.printedBookList) {
                                if (Integer.parseInt(temp[1]) == printedBook.getId()) {
                                    if(!printedBook.isReturned()){
                                        writeOutput("You cannot borrow this book!");
                                        break;
                                    }
                                    printedBook.setBorrowedTime(LocalDate.parse(temp[3]));
                                    printedBook.setReturned(false);
                                    printedBook.setMustReturnedTime(printedBook.getBorrowedTime().plusWeeks(2));
                                    writeOutput("The book ["+printedBook.getId()+"] was borrowed by member ["+academic.getId()+"] at "+printedBook.getBorrowedTime());
                                    academic.addToBorrowedList(printedBook);
                                    printedBook.setStringBorrowed("The book ["+printedBook.getId()+"] was borrowed by member ["+academic.getId()+"] at "+printedBook.getBorrowedTime());
                                }
                            }
                        }
                    }
                    break;
                case "returnBook":
                    for (Student student:Student.studentList){
                        if(Integer.parseInt(temp[2]) == student.getId()){
                            for (PrintedBook printedBook:PrintedBook.printedBookList){
                                if(Integer.parseInt(temp[1]) == printedBook.getId()){
                                    if (printedBook.getMustReturnedTime() != null && LocalDate.parse(temp[3]).isAfter(printedBook.getMustReturnedTime())){
                                        writeOutput("You must pay a penalty!");
                                        writeOutput("The book ["+printedBook.getId()+"] was returned by member ["+student.getId()+"] at "+temp[3]+" Fee: "+ DAYS.between(printedBook.getMustReturnedTime(),LocalDate.parse(temp[3])));
                                    }else{
                                        writeOutput("The book ["+printedBook.getId()+"] was returned by member ["+student.getId()+"] at "+temp[3]+" Fee: 0");
                                    }
                                    printedBook.setReturned(true);
                                    printedBook.setBorrowedTime(null);
                                    printedBook.setMustReturnedTime(null);
                                    printedBook.setStringBorrowed(null);
                                    printedBook.setStringRead(null);
                                    student.removeFromBorrowedList(printedBook);
                                }
                            }
                        }
                    }
                    for (Academic academic:Academic.academicList){
                        if(Integer.parseInt(temp[2]) == academic.getId()){
                            for (PrintedBook printedBook:PrintedBook.printedBookList){
                                if(Integer.parseInt(temp[1]) == printedBook.getId()){
                                    if (printedBook.getMustReturnedTime() != null && LocalDate.parse(temp[3]).isAfter(printedBook.getMustReturnedTime())){
                                        writeOutput("You must pay a penalty!");
                                        writeOutput("The book ["+printedBook.getId()+"] was returned by member ["+academic.getId()+"] at "+temp[3]+" Fee: "+ DAYS.between(printedBook.getMustReturnedTime(),LocalDate.parse(temp[3])));
                                    }else{
                                        writeOutput("The book ["+printedBook.getId()+"] was returned by member ["+academic.getId()+"] at "+temp[3]+" Fee: 0");
                                    }
                                    printedBook.setReturned(true);
                                    printedBook.setBorrowedTime(null);
                                    printedBook.setMustReturnedTime(null);
                                    printedBook.setStringBorrowed(null);
                                    printedBook.setStringRead(null);
                                    academic.removeFromBorrowedList(printedBook);
                                }
                            }
                            for (HandwrittenBook handwrittenBook:HandwrittenBook.handwrittenBookList){
                                if(Integer.parseInt(temp[1]) == handwrittenBook.getId()){
                                    if (handwrittenBook.getMustReturnedTime() != null && LocalDate.parse(temp[3]).isAfter(handwrittenBook.getMustReturnedTime())){
                                        writeOutput("You must pay a penalty!");
                                        writeOutput("The book ["+handwrittenBook.getId()+"] was returned by member ["+academic.getId()+"] at "+temp[3]+" Fee: "+ DAYS.between(handwrittenBook.getMustReturnedTime(),LocalDate.parse(temp[3])));
                                    }else{
                                        writeOutput("The book ["+handwrittenBook.getId()+"] was returned by member ["+academic.getId()+"] at "+temp[3]+" Fee: 0");
                                    }
                                    handwrittenBook.setReturned(true);
                                    handwrittenBook.setBorrowedTime(null);
                                    handwrittenBook.setMustReturnedTime(null);
                                    handwrittenBook.setStringBorrowed(null);
                                    handwrittenBook.setStringRead(null);
                                    academic.removeFromBorrowedList(handwrittenBook);
                                }
                            }
                        }
                    }
                    break;
                case "extendBook":
                    for (Student student:Student.studentList){
                        if(Integer.parseInt(temp[2]) == student.getId()){
                            for(PrintedBook printedBook:PrintedBook.printedBookList){
                                if(Integer.parseInt(temp[1]) == printedBook.getId()){
                                    if (student.getExtendedBooks()!= null && student.getExtendedBooks().contains(printedBook)){
                                        writeOutput("You cannot extend the deadline!");
                                    }else{
                                        if (!LocalDate.parse(temp[3]).isAfter(printedBook.getMustReturnedTime())){
                                            printedBook.setMustReturnedTime(printedBook.getMustReturnedTime().plusWeeks(1));
                                            writeOutput("The deadline of book ["+printedBook.getId()+"] was extended by member ["+student.getId()+"] at "+temp[3]);
                                            writeOutput("New deadline of book ["+printedBook.getId()+"] is "+printedBook.getMustReturnedTime());
                                            student.addToExtendedList(printedBook);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    for (Academic academic:Academic.academicList){
                        if(Integer.parseInt(temp[2]) == academic.getId()){
                            for(PrintedBook printedBook:PrintedBook.printedBookList){
                                if(Integer.parseInt(temp[1]) == printedBook.getId()){
                                    if (academic.getExtendedBooks()!= null && academic.getExtendedBooks().contains(printedBook)){
                                        writeOutput("You cannot extend the deadline!");
                                    }else{
                                        if (!LocalDate.parse(temp[3]).isAfter(printedBook.getMustReturnedTime())){
                                            printedBook.setMustReturnedTime(printedBook.getMustReturnedTime().plusWeeks(1));
                                            writeOutput("The deadline of book ["+printedBook.getId()+"] was extended by member ["+academic.getId()+"] at "+temp[3]);
                                            writeOutput("New deadline of book ["+printedBook.getId()+"] is "+printedBook.getMustReturnedTime());
                                            academic.addToExtendedList(printedBook);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                case "readInLibrary":
                    for (Student student:Student.studentList){
                        if (Integer.parseInt(temp[2]) == student.getId()){
                            for (HandwrittenBook handwrittenBook:HandwrittenBook.handwrittenBookList){
                                if(Integer.parseInt(temp[1]) == handwrittenBook.getId()){
                                    writeOutput("Students can not read handwritten books!");
                                    break;
                                }
                            }
                            for (PrintedBook printedBook:PrintedBook.printedBookList){
                                if(Integer.parseInt(temp[1]) == printedBook.getId()){
                                    if (printedBook.getBorrowedTime() == null || LocalDate.parse(temp[3]).isBefore(printedBook.getBorrowedTime())){
                                        writeOutput("The book ["+printedBook.getId()+"] was read in library by member ["+student.getId()+"] at "+temp[3]);
                                        printedBook.setStringRead("The book ["+printedBook.getId()+"] was read in library by member ["+student.getId()+"] at "+temp[3]);
                                    }else{
                                        writeOutput("You can not read this book!");
                                    }
                                }
                            }
                        }
                    }
                    for (Academic academic:Academic.academicList){
                        if (Integer.parseInt(temp[2]) == academic.getId()){
                            for (PrintedBook printedBook:PrintedBook.printedBookList){
                                if(Integer.parseInt(temp[1]) == printedBook.getId()){
                                    if (printedBook.getBorrowedTime() == null || LocalDate.parse(temp[3]).isBefore(printedBook.getBorrowedTime())){
                                        writeOutput("The book ["+printedBook.getId()+"] was read in library by member ["+academic.getId()+"] at "+temp[3]);
                                        printedBook.setStringRead("The book ["+printedBook.getId()+"] was read in library by member ["+academic.getId()+"] at "+temp[3]);
                                    }else{
                                        writeOutput("You can not read this book!");
                                    }
                                }
                            }
                            for (HandwrittenBook handwrittenBook:HandwrittenBook.handwrittenBookList){
                                if(Integer.parseInt(temp[1]) == handwrittenBook.getId()){
                                    if (handwrittenBook.getBorrowedTime() == null || LocalDate.parse(temp[3]).isBefore(handwrittenBook.getBorrowedTime())){
                                        writeOutput("The book ["+handwrittenBook.getId()+"] was read in library by member ["+academic.getId()+"] at "+temp[3]);
                                        handwrittenBook.setStringRead("The book ["+handwrittenBook.getId()+"] was read in library by member ["+academic.getId()+"] at "+temp[3]);
                                    }else{
                                        writeOutput("You can not read this book!");
                                    }
                                }
                            }
                        }
                    }
                    break;
                case "getTheHistory":
                    writeOutput("History of library:\n");
                    writeOutput("Number of students: "+Student.studentList.size());
                    for (Student student:Student.studentList){
                        writeOutput("Student [id: "+student.getId()+"]");
                    }
                    writeOutput(""); // for an empty line
                    writeOutput("Number of academics: "+Academic.academicList.size());
                    for (Academic academic:Academic.academicList){
                        writeOutput("Academic [id: "+academic.getId()+"]");
                    }
                    writeOutput("");
                    writeOutput("Number of printed books: "+PrintedBook.printedBookList.size());
                    for (PrintedBook printedBook:PrintedBook.printedBookList){
                        writeOutput("Printed [id: "+printedBook.getId()+"]");
                    }
                    writeOutput("");
                    writeOutput("Number of handwritten books: "+HandwrittenBook.handwrittenBookList.size());
                    for (HandwrittenBook handwrittenBook:HandwrittenBook.handwrittenBookList){
                        writeOutput("Handwritten [id: "+handwrittenBook.getId()+"]");
                    }
                    writeOutput("");

                    int borrowedNum = 0;
                    for (PrintedBook printedBook:PrintedBook.printedBookList){
                        if(printedBook.getStringBorrowed() != null){
                            borrowedNum+=1;
                        }
                    }
                    writeOutput("Number of borrowed books: "+borrowedNum);
                    for (PrintedBook printedBook:PrintedBook.printedBookList){
                        if(printedBook.getStringBorrowed() != null){
                            writeOutput(printedBook.getStringBorrowed());
                        }
                    }
                    writeOutput("");

                    int readNum = 0;
                    for (PrintedBook printedBook:PrintedBook.printedBookList){
                        if(printedBook.getStringRead() != null){
                            readNum+=1;
                        }
                    }
                    for (HandwrittenBook handwrittenBook:HandwrittenBook.handwrittenBookList){
                        if(handwrittenBook.getStringRead() != null){
                            readNum+=1;
                        }
                    }
                    writeOutput("Number of books read in library: "+readNum);
                    for (PrintedBook printedBook:PrintedBook.printedBookList){
                        if(printedBook.getStringRead() != null){
                            writeOutput(printedBook.getStringRead());
                        }
                    }
                    for (HandwrittenBook handwrittenBook:HandwrittenBook.handwrittenBookList){
                        if(handwrittenBook.getStringRead() != null){
                            writeOutput(handwrittenBook.getStringRead());
                        }
                    }
                    break;
            }
        }
    }
    public static void writeOutput(String content){  // to shorten the writing method
        FileOutput.writeToFile(writePath, content, true, true);
    }
}