package generic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.regex.Pattern;

import generic.Operand.OperandType;

public class Simulator {

   static FileInputStream inputcodeStream = null;

   public static void setupSimulation(String assemblyProgramFile) {
      int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
      ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
      ParsedProgram.printState();   // prints symbol table and instructions to verify the parsed output

   }

   public static void assemble(String objectProgramFile) {
      try {
         String file = "";
         FileOutputStream outputStream = new FileOutputStream(objectProgramFile);
//It creates an output stream to write binary data into a file named objectProgramFile.
         HashMap<String, String> ISAMAP = new HashMap<>();
//Each instruction (e.g., add, load, jmp) is mapped to its 5-bit opcode.
         ISAMAP.put("add", "00000");
         ISAMAP.put("addi", "00001");
         ISAMAP.put("sub", "00010");
         ISAMAP.put("subi", "00011");
         ISAMAP.put("mul", "00100");
         ISAMAP.put("muli", "00101");
         ISAMAP.put("div", "00110");
         ISAMAP.put("divi", "00111");
         ISAMAP.put("and", "01000");
         ISAMAP.put("andi", "01001");
         ISAMAP.put("or", "01010");
         ISAMAP.put("ori", "01011");
         ISAMAP.put("xor", "01100");
         ISAMAP.put("xori", "01101");
         ISAMAP.put("slt", "01110");
         ISAMAP.put("slti", "01111");
         ISAMAP.put("sll", "10000");
         ISAMAP.put("slli", "10001");
         ISAMAP.put("srl", "10010");
         ISAMAP.put("srli", "10011");
         ISAMAP.put("sra", "10100");
         ISAMAP.put("srai", "10101");

         ISAMAP.put("load", "10110");
         ISAMAP.put("store", "10111");

         ISAMAP.put("jmp", "11000");
         ISAMAP.put("beq", "11001");
         ISAMAP.put("bne", "11010");
         ISAMAP.put("blt", "11011");
         ISAMAP.put("bgt", "11100");

         ISAMAP.put("end", "11101");

         int firstCodeAddress = ParsedProgram.firstCodeAddress;

         // Writing the first code address to the output stream
         outputStream.write(ByteBuffer.allocate(4).putInt(firstCodeAddress).array());
         
         file = ExtendTo32(firstCodeAddress) + "\n";
         // writing all the data elements
         for (int element : ParsedProgram.data) {
            outputStream.write(ByteBuffer.allocate(4).putInt(element).array());
            file += ExtendTo32(element) + "\n";

         }

         HashMap<Integer, String> regBinary = new HashMap<>();

         regBinary.put(0, "00000");
         regBinary.put(1, "00001");
         regBinary.put(2, "00010");
         regBinary.put(3, "00011");
         regBinary.put(4, "00100");
         regBinary.put(5, "00101");
         regBinary.put(6, "00110");
         regBinary.put(7, "00111");
         regBinary.put(8, "01000");
         regBinary.put(9, "01001");
         regBinary.put(10, "01010");
         regBinary.put(11, "01011");
         regBinary.put(12, "01100");
         regBinary.put(13, "01101");
         regBinary.put(14, "01110");
         regBinary.put(15, "01111");
         regBinary.put(16, "10000");
         regBinary.put(17, "10001");
         regBinary.put(18, "10010");
         regBinary.put(19, "10011");
         regBinary.put(20, "10100");
         regBinary.put(21, "10101");
         regBinary.put(22, "10110");
         regBinary.put(23, "10111");
         regBinary.put(24, "11000");
         regBinary.put(25, "11001");
         regBinary.put(26, "11010");
         regBinary.put(27, "11011");
         regBinary.put(28, "11100");
         regBinary.put(29, "11101");
         regBinary.put(30, "11110");
         regBinary.put(31, "11111");

         // System.out.println(ParsedProgram.getRegisterOperandFromString(
         // ParsedProgram.code.get(0).sourceOperand1));

         for (Instruction InstructionLine : ParsedProgram.code) {
            // System.out.println(element.operationType);
            System.out.println(InstructionLine.programCounter);
            // binStringToWrite encodes each line instruction
            String binStringToWrite = "";

            switch (InstructionLine.operationType) {
               // R3I type
               case add:
               case sub:
               case mul:
               case div:
               case and:
               case or:
               case xor:
               case slt:
               case sll:
               case srl:
               case sra: {

                  binStringToWrite += ISAMAP.get(InstructionLine.operationType.toString());
                  binStringToWrite += regBinary.get(InstructionLine.getSourceOperand1().value);
                  binStringToWrite += regBinary.get(InstructionLine.getSourceOperand2().value);
                  binStringToWrite += regBinary.get(InstructionLine.getDestinationOperand().value);
                  binStringToWrite = ExtendToFourByteString(binStringToWrite);
                  break;
               }

               // R2I type
               case addi:
               case subi:
               case muli:
               case divi:
               case andi:
               case ori:
               case xori:
               case slti:
               case slli:
               case srli:
               case srai:
               case load:
               case store: {
                  binStringToWrite += ISAMAP.get(InstructionLine.operationType.toString());
                  binStringToWrite += regBinary.get(InstructionLine.getSourceOperand1().value);
                  binStringToWrite += regBinary.get(InstructionLine.getDestinationOperand().value);
                  binStringToWrite += ExtendTo17(Integer.toBinaryString(InstructionLine.getSourceOperand2().value));
                  break;

               }

               case beq:
               case bne:
               case blt:
               case bgt: {
                  binStringToWrite += ISAMAP.get(InstructionLine.operationType.toString());
                  binStringToWrite += regBinary.get(InstructionLine.getSourceOperand1().value);
                  binStringToWrite += regBinary.get(InstructionLine.getSourceOperand2().value);
                  binStringToWrite += ExtendTo17(
                        (Integer.toBinaryString(
                              ParsedProgram.symtab.get(InstructionLine.getDestinationOperand().labelValue)-InstructionLine.programCounter)));
                  break;
               }

               // RI type :
               case jmp: {
                  binStringToWrite += ISAMAP.get(InstructionLine.operationType.toString());
                  binStringToWrite += "00000";
                  binStringToWrite += ExtendTo22(
                        (Integer.toBinaryString(
                              ParsedProgram.symtab.get(InstructionLine.getDestinationOperand().labelValue)-InstructionLine.programCounter)));
                  break;
               }

               case end: {
                  binStringToWrite += "11101000000000000000000000000000";
                  break;
               }

               default:
                  Misc.printErrorAndExit("unknown instruction!!");
            }
            //This block:  Converts binary instruction → integer → 4-byte format
            int instInteger = (int) Long.parseLong(binStringToWrite, 2);
            byte[] instBinary = ByteBuffer.allocate(4).putInt(instInteger).array();
            outputStream.write(instBinary);
            file += binStringToWrite + "\n";
         }
         System.out.println(file);
         outputStream.close();
      } catch (Exception e) {
         System.out.println(e.getMessage());
      }
   }

   // Extends a string to 4 byte
   public static String ExtendToFourByteString(String number) {
      int templen = number.length();
      String binaryString = number;
      for (int i = 0; i < 32 - templen; i++) {
         binaryString = binaryString + "0";
      }
      return binaryString;
   }

   public static String ExtendTo17(String x) {

      String number = x;

      int templen = number.length();

      if (templen > 17) {
         return number.substring(templen - 17);
      }

      String binaryString = number;
      for (int i = 0; i < 17 - templen; i++) {
         binaryString = "0" + binaryString;
      }
      return binaryString;
   }

   public static String ExtendTo22(String x) {

      String number = x;

      int templen = number.length();

      if (templen > 22) {
         return number.substring(templen - 22);
      }

      String binaryString = number;
      for (int i = 0; i < 22 - templen; i++) {
         binaryString = "0" + binaryString;
      }
      return binaryString;
   }

   public static String ExtendTo32(int x) {

      String number = Integer.toBinaryString(x);

      int templen = number.length();

      if (templen > 32) {
         return number.substring(templen - 32);
      }

      String binaryString = number;
      for (int i = 0; i < 32 - templen; i++) {
         binaryString = "0" + binaryString;
      }
      return binaryString;
   }
}