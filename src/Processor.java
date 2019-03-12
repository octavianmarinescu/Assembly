import java.util.LinkedList;

public class Processor {
	
	private int[] registre;
	private LinkedList<Integer> send;
	private State state;

	public Processor(int nrProcessor) {

		//initializam registrele cu 0, cu exceptia primului care se init cu nr reg -1
		send = new LinkedList<>();
		state = State.NORMAL;
		registre = new int[32];
		registre[0] = nrProcessor;
		for (int i = 1; i < 32; i++) {
			registre[i] = 0; 
		}
	}
	
	public void set(String line) {
		// trb sa tranform R4 in index si val(poate sa fie val = R2) in int
		String[] parts = line.split(" ");

		int nrReg = Integer.parseInt(parts[1].substring(1));
		
		if (parts[2].charAt(0) == 'R' ) {
			int y = Integer.parseInt(parts[2].substring(1));
			registre[nrReg] = registre[y];
		} else {
			int y = Integer.parseInt(parts[2]);
			registre[nrReg] = y;
		}
	}
	
	public void add(String line) {		
		String[] parts = line.split(" ");

		int nrReg = Integer.parseInt(parts[1].substring(1));
		
		if (parts[2].charAt(0) == 'R' ) {
			int y = Integer.parseInt(parts[2].substring(1));
			registre[nrReg] += registre[y];
		} else {
			int y = Integer.parseInt(parts[2]);
			registre[nrReg] += y;
		}
	}
	
	public void mul(String line) {
		String[] parts = line.split(" ");

		int nrReg = Integer.parseInt(parts[1].substring(1));
		
		if (parts[2].charAt(0) == 'R' ) {
			int y = Integer.parseInt(parts[2].substring(1));
			registre[nrReg] *= registre[y];
		} else {
			int y = Integer.parseInt(parts[2]);
			registre[nrReg] *= y;
		}
	}
	
	public void mod(String line) {
		String[] parts = line.split(" ");

		int nrReg = Integer.parseInt(parts[1].substring(1));
		
		if (parts[2].charAt(0) == 'R' ) {
			int y = Integer.parseInt(parts[2].substring(1));
			registre[nrReg] = registre[nrReg] % registre[y];
		} else {
			int y = Integer.parseInt(parts[2]);
			registre[nrReg] = registre[nrReg] % y;
		}
	}
	
	public void snd(String line) {
		if (line != null && line.charAt(4) == 'R' ) {
			int y = Integer.parseInt(line.substring(5));
			
			send.add(registre[y]);
		} else {
			int yy = Integer.parseInt(line.substring(4));
			
			send.add(yy);
		}
	}
	
	public int rcv(String line, Processor other) {
		if (!other.send.isEmpty() && line != null) {
			int y = Integer.parseInt(line.substring(5));
			
			registre[y] = other.send.getFirst();
			other.send.removeFirst();
			if (state.equals(State.BLOCKED))
				state = State.NORMAL;
			return 1;
		} //else -> daca send e gol, trb sa astepte un snd
		else {
			state = State.BLOCKED;
			return -1;
		}
	}
	
	public int jgz(String line) {
		String[] parts = line.split(" ");
		int x;
		int y;
		
		if (parts[1].charAt(0) == 'R') {
			x = Integer.parseInt(parts[1].substring(1));
		} else {
			x = Integer.parseInt(parts[1]);
		}
		
		if (registre[x] > 0) {
			if (parts[2].charAt(0) == 'R' ) {
				y = Integer.parseInt(parts[2].substring(1));
			} else {
				y = Integer.parseInt(parts[2]);
			}
			return y;
		} else {
			return 0;
		}
		
	}
	
	public void afisareRegistre () {	
		for (int i = 0; i < 32; i++) {
			if (registre[i] != 0) {
				System.out.print(registre[i] + " ");
			}
		}
	}

	public boolean isBlocked() {
		return state.equals(State.BLOCKED);
	}
	
	public boolean isFinished() {
		return state.equals(State.FINISHED);
	}

	public void finish() {
		this.state = State.FINISHED;
	}

	public boolean isNormal() {
		return state.equals(State.NORMAL);
	}
}
