package de.farberg.sumo.convert;

import java.io.File;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class CommandLineOptions {

	@Option(name = "--in", usage = "", required = true)
	public File inFile = null;

	@Option(name = "--out", usage = "", required = true)
	public File outFile = null;

	@Option(name = "--offsetx", usage = "<number>", required = true)
	public double offsetx = -1;

	@Option(name = "--offsety", usage = "<number>", required = true)
	public double offsety = -1;

	@Option(name = "--zone", usage = "<number>", required = true)
	public int zone = -1;

	@Option(name = "--hemisphere", usage = "SOUTH or NORTH (default: NORTH).", required = false)
	public String hemisphere = "NORTH";

	@Option(name = "-v", aliases = {
			"--verbose" }, usage = "Verbose (DEBUG) logging output (default: INFO).", required = false)
	public boolean verbose = false;

	@Option(name = "-h", aliases = { "--help" }, usage = "This help message.", required = false)
	public boolean help = false;

	public static CommandLineOptions parseCmdLineOptions(final String[] args, Class<?> mainClass) {
		CommandLineOptions options = new CommandLineOptions();
		CmdLineParser parser = new CmdLineParser(options);

		try {
			parser.parseArgument(args);
			if (options.help)
				printHelpAndExit(parser, mainClass);
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			printHelpAndExit(parser, mainClass);
		}

		return options;
	}

	public static void printHelpAndExit(CmdLineParser parser, Class<?> mainClass) {
		System.err.print("Usage: java " + mainClass.getCanonicalName());
		parser.printSingleLineUsage(System.err);
		System.err.println();
		parser.printUsage(System.err);
		System.exit(1);
	}
}
