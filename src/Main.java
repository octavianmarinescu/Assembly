import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Main {

	public static final String PATH = "C:\\Users\\Octavian Marinescu\\eclipse-workspace\\CDL";
	public static final String FILE_IN = "code.in";
	public static final String FILE_OUT = "code.out";


	public static void main(String[] args) {
		
		List<Processor> procs = new ArrayList<>();
		List<String> linesFromFile;
		int nrProc;


		setOutputStream();

		linesFromFile = readLinesFromFile();
		if (linesFromFile.isEmpty()) {
			throw new RuntimeException("Input file is empty!");
		}


		nrProc = Integer.parseInt(linesFromFile.get(0));
		int[] index = new int[nrProc];
		linesFromFile.remove(0);

		for (int i = 0; i < nrProc; i++) {
			index[i] = 0;
			procs.add(new Processor(i));
		}


		while (true) {

			int finishedProcs = 0;

			// test if all processors have finished
			for (int i = 0; i < nrProc; i++) {
				if ( index[i] >= linesFromFile.size() ) {
					finishedProcs++;
				} else {
					break;
				}
			}
			if (finishedProcs == nrProc) {
				break;
			}

			// execute command on each processor
			for (int i = 0; i < nrProc; i++) {
				if (index[i] < linesFromFile.size()) {
					String line = linesFromFile.get(index[i]);
					Command cmd = Command.valueOf(line.substring(0, 3));

					if (Command.JGZ == cmd) {
						int jump = procs.get(i).jgz(line);
						jump = (jump != 0) ? jump - 1 : jump;
						index[i] += jump;
					} else {
						switch (cmd) {
						case SET:
							procs.get(i).set(line);
							break;
						case ADD:
							procs.get(i).add(line);
							break;
						case MUL:
							procs.get(i).mul(line);
							break;
						case MOD:
							procs.get(i).mod(line);
							break;
						case SND:
							procs.get(i).snd(line);
							break;
						case RCV:
							int rez = procs.get(i).rcv(line, procs.get((i - 1 + nrProc) % nrProc));
							if (rez == -1) {
								index[i]--;
							}
							break;
						}}
					}
				else {
					procs.get(i).finish();
				}
				index[i]++;
			}

			// check for deadlock
			if (isDeadlock(procs)) {
				break;
			}
		}


		printResults(procs);

	}


	public static void setOutputStream() {

		try {
			PrintStream out = new PrintStream(new FileOutputStream(FILE_OUT));
			System.setOut(out);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Unable to set the output stream!");
		}

	}


	public static List<String> readLinesFromFile() {

		List<String> linesFromFile = new ArrayList<>();
		Path pathToFile = Paths.get(PATH, FILE_IN);

		try {
			linesFromFile = Files.readAllLines(pathToFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return linesFromFile;
	}


	public static boolean isDeadlock(List<Processor> procs) {

		int nrProc = procs.size();

		for (int i = 0; i < nrProc; i++) {
			if (procs.get(i).isNormal()) {
				return false;
			}
		}

		return true;
	}


	public static void printResults(List<Processor> procs) {

		int nrProc = procs.size();

		for (int i = 0; i < nrProc; i++) {
			procs.get(i).afisareRegistre();
			if ((i+1) != nrProc) {
				System.out.println();
			}
		}
	}
}

