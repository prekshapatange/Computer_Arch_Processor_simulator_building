package main;
import configuration.Configuration;
import generic.Misc;
import generic.Statistics;
import generic.Simulator;

public class Main {

	public static void main(String[] args) {
		if(args.length != 2)
		{
			Misc.printErrorAndExit("usage: java -jar <path-to-jar-file> <path-to-assembly-program> <path-to-object-file>\n");
		}		
		
		Simulator.setupSimulation(args[0]);  // Pass only one argument
Simulator.assemble(args[1]);         // Pass one argument to assemble()

	}

}
