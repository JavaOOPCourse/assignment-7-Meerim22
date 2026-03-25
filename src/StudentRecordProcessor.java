import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentRecordProcessor {
    // Поля для хранения данных
    private final List<Student> students = new ArrayList<>();

    private double averageScore;
    private Student highestStudent;

    /**
     * Task 1 + Task 2 + Task 5 + Task 6
     */
    public void readFile() {
        String filename = "data/students.txt";

        // Task 5: Use try-with-resources for reading
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                try {
                    String[] parts = line.split(",");
                    if (parts.length != 2) {
                        throw new NumberFormatException(); // Trigger skip for bad format
                    }

                    String name = parts[0].trim();
                    int score = Integer.parseInt(parts[1].trim());

                    // Task 6: Throw custom exception for invalid scores
                    if (score < 0 || score > 100) {
                        throw new InvalidScoreException("Score out of bounds");
                    }

                    students.add(new Student(name, score));
                    // Task 1: Print all valid lines
                    System.out.println("Valid record read: " + name + ", " + score);

                } catch (NumberFormatException e) {
                    // Task 2: Skip the line and print error for invalid format
                    System.out.println("Invalid data: " + line);
                } catch (InvalidScoreException e) {
                    // Task 6: Provide informative message for invalid scores
                    System.out.println("Invalid score: " + e.getMessage() + " in line: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: The file '" + filename + "' was not found.");
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    /**
     * Task 3 + Task 8
     */
    public void processData() {
        if (students.isEmpty()) {
            return;
        }

        // Task 8: Sort students descending
        Collections.sort(students);

        int totalScore = 0;
        // Since the list is sorted descending, the first element is the highest score
        highestStudent = students.get(0); 

        for (Student student : students) {
            totalScore += student.score;
        }

        averageScore = (double) totalScore / students.size();
    }

    /**
     * Task 4 + Task 5 + Task 8
     */
    public void writeFile() {
        if (students.isEmpty()) {
            System.out.println("No valid student data available to write.");
            return;
        }

        String filename = "output/report.txt";
        File file = new File(filename);
        
        // Ensure the parent directories exist
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        // Task 5: Use try-with-resources for writing
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            
            bw.write("Average: " + averageScore + "\n");
            bw.write("Highest: " + highestStudent.name + " - " + highestStudent.score + "\n");
            bw.write("\n--- Full Sorted List ---\n");
            
            // Task 8: Save full sorted list to file
            for (Student student : students) {
                bw.write(student.name + ", " + student.score + "\n");
            }

        } catch (IOException e) {
            System.out.println("Error writing to the file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        StudentRecordProcessor processor = new StudentRecordProcessor();

        try {
            processor.readFile();
            processor.processData();
            processor.writeFile();
            System.out.println("Processing completed. Check output/report.txt");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}

// Task 6: Custom Exception
class InvalidScoreException extends Exception {
    public InvalidScoreException(String message) {
        super(message);
    }
}

// Helper class to store student data and implement sorting
class Student implements Comparable<Student> {
    String name;
    int score;

    public Student(String name, int score) {
        this.name = name;
        this.score = score;
    }

    // Task 8: Sort students by score (descending)
    @Override
    public int compareTo(Student other) {
        return Integer.compare(other.score, this.score); 
    }
}