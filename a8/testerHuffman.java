import java.util.*;
import java.io.*;
public class testerHuffman{
   public static void main(String[] args) throws FileNotFoundException{
      Scanner input = new Scanner(new File("simple.code.txt"));
      HuffmanCode test = new HuffmanCode(input);
      PrintStream output = new PrintStream("test.txt");
      System.out.print("hi");
      test.save(output);
      
   }
}