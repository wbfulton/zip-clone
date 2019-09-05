// 3/14/2019
// CSE 143
// TA: Tejasvini Sareen
// Assignment #8
// This program uses the Huffman Algorithm to compress a text file to a smaller size and
// to also expland this compressed file back to normal. The user has control over the program
// through the HuffmanCompressor class. The user can create a HuffmanTree, compress the file,
// and uncompress the file.
import java.util.*;
import java.io.*;

public class HuffmanTree{
   
   private HuffmanNode root; // the root of the Huffman Tree
   
   // post: constructs a huffman tree through an array of frequencies of 
   //       characters in ASCII code
   public HuffmanTree(int[] count){
      Queue<HuffmanNode> huffmanTree = new PriorityQueue<>();
      for(int i = 0; i < count.length; i++){
         if(count[i] > 0){
            huffmanTree.add(new HuffmanNode((char) i, count[i]));
         }
      }
      // queue with single characters arranged now
      while(huffmanTree.size() > 1){
         HuffmanNode add1 = huffmanTree.remove();
         HuffmanNode add2 = huffmanTree.remove();
         HuffmanNode total = new HuffmanNode( add1.frequency + add2.frequency, add1, add2);
         huffmanTree.add(total);
      }
      this.root = huffmanTree.peek();
   }
   
   // pre: assumes Scanner is not null and .code file is in correct format
   // post: constructs a new HuffmanTree by reading previously contructed code
   //       in a .code file
   public HuffmanTree(Scanner input){
      while(input.hasNextLine()){
         String asciiValue = input.nextLine();
         String code = input.nextLine();
         root = scannerHelper(root, asciiValue, code);
      }
   }
   
   // pre: assumes String asciiValue is not null
   // post: helps to construct a new HuffmanTree by reading a previously constructed
   //       .code file
   private HuffmanNode scannerHelper(HuffmanNode node, String asciiValue, String code){
      if(code.isEmpty()){ // prints leaf
         node = new HuffmanNode((char)Integer.parseInt(asciiValue), 0);
      } else {
         if(node == null){ // prints branch
            node = new HuffmanNode( -1, null, null);
         }
         if(code.charAt(0) == 48){ // chooses direcition to go in
            node.zero = scannerHelper(node.zero, asciiValue, code.substring(1));
         } else{
            node.one = scannerHelper(node.one, asciiValue, code.substring(1));
         }
      }
      return node;
   }
   
   // post: stores the current HuffmanTree in huffman code format and
   //       saves the data to the given output stream
   public void save(PrintStream output){
      saveHelper(root, "", output);
   }
   
   // post: helps store the current HuffmanTree in huffman code format and
   //       saves the data to the given output stream
   private void saveHelper(HuffmanNode position, String path, PrintStream output){
      if(position.zero == null || position.one == null){
         output.println((int)position.text);
         output.println(path);
      } else{
         this.saveHelper(position.zero, path + "0", output);
         this.saveHelper(position.one, path + "1", output);
      }
   }
   
   // pre: assumes Scanner input given goes to a non-null bit stream
   // post: Reads individual bits from the input stream and
   //       writes the corresponding characters to the huffman tree
   //       in the given output stream
   public void translate(BitInputStream input, PrintStream output){
      HuffmanNode location = root;
      while(input.hasNextBit() || location.one == null && location.zero == null) {
         if(location.one == null && location.zero == null){
            output.write(location.text);
            location = root; // resets search
         } else {
            
            int direction = input.nextBit();
            if(direction == 0){
               location = location.zero;
            } else {
               location = location.one;
            }
         }
      }
   }
   
   // post: node class that allows for the creation of a tree to be used by the HuffmanTree class
   private static class HuffmanNode implements Comparable<HuffmanNode>{
      public char text; // text being represented
      public int frequency; // frequency of that text
      public HuffmanNode zero; //left
      public HuffmanNode one; //right
      
      // post: constructs huffman node without links. Used to build leaves of tree
      public HuffmanNode(char text, int frequency){
         this(text, frequency, null, null);
      }
      
      
      // post: constructs huffman node without text. Builds nodes of tree
      public HuffmanNode(int frequency, HuffmanNode zero, HuffmanNode one){
         this.frequency = frequency;
         this.zero = zero;
         this.one = one;
      }
      
      // post: constucts huffman node with text, frequency, and two links to other nodes
      public HuffmanNode(char text, int frequency, HuffmanNode zero, HuffmanNode one){
         this.text = text;
         this.frequency = frequency;
         this.zero = zero;
         this.one = one;
      }
      
      // post: compares two nodes, required to compile
      public int compareTo(HuffmanNode other){
         return this.frequency - other.frequency;
      }
   }
}
