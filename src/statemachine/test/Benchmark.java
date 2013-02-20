package statemachine.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import statemachine.year1.library.Event;
import statemachine.year1.library.IMachine;

/**
 * Benchmark the running times of the various state machine implementations, using randomized
 * sequence of events
 * @author ups
 *
 */
public class Benchmark {

	/**
	 * Run the benchmark according to a hard-coded set of parameters
	 */
	public static void main(String argv[]) {
		new Benchmark().main(10000,2000,87);
	}

	/**
	 * Run each experiment according to the parameters
	 * @param loops how many times to loop each experiment
	 * @param nevents how many events to deliver to each state machine
	 * @param seed the random seed to use for generating the events sequence
	 */
	private void main(int loops, int nevents, int seed) {
		for(Experiment e: createExperiments(nevents, seed)) {
			long start = System.nanoTime();
			for(int i=0; i<loops; i++)
				e.run();
			long end = System.nanoTime();
			long total = end-start;
			System.out.println("Time for "+e+": "+(total/1000000.0)/loops+"ms");
		}
			
	}

	// Lists of the various implementations of state machines, by type, as arrays
	
	private final IMachine[] machinesList_cdplayer = new IMachine[] {
		new statemachine.year1.cdplayer.CDPlayerMachine(), 
		new statemachine.year2.cdplayer.CDPlayerMachine(), 
		new statemachine.year3.cdplayer.CDPlayerMachine(), 
		new statemachine.year4.generated.CDPlayer4Impl(), 
	};
	
	private final IMachine[] machinesList_cookinghood = new IMachine[] {
			new statemachine.year1.cookinghood.CookingHoodMachine(),
			new statemachine.year2.cookinghood.CookingHoodMachine(),
			new statemachine.year3.cookinghood.CookingHoodMachine(),
			new statemachine.year4.generated.CookingHood4Impl(),
	};
		
	private final IMachine[] machinesList_microwaveoven = new IMachine[] {
			new statemachine.year1.microwaveoven.MicrowaveMachine(),
			new statemachine.year2.microwaveoven.MicrowaveMachine(),
			new statemachine.year3.microwaveoven.MicrowaveMachine(),
			new statemachine.year4.generated.MicroWaveOven4Impl(),
		};
		

	// Lists of the various implementations of state machines, by type
	
	private List<IMachine> machines_cdplayer = Arrays.asList(machinesList_cdplayer);
	private List<IMachine> machines_cookinghood = Arrays.asList(machinesList_cookinghood);
	private List<IMachine> machines_microwaveoven = Arrays.asList(machinesList_microwaveoven);

	// Enumeration of the events supported by each type of state machine
	
	private List<Event> events_cdplayer = makeEventsList("PLAY","STOP","PAUSE","FORWARD","BACK","TRACK_END");
	private List<Event> events_cookinghood = makeEventsList("PLUS","MINUS");
	private List<Event> events_microwaveoven = makeEventsList("START","STOP","OPEN","CLOSE","TIMER");
	
	/**
	 * An experiment: a machine and a sequence of events to feed that machine
	 */
	private class Experiment {
		private IMachine machine;
		private Event[] events;
		public Experiment(IMachine machine, Event[] events) {
			this.machine = machine; this.events = events;
			machine.initialize();
		}
		public String toString() {
			return machine.getClass().getCanonicalName();
		}
		/**
		 * Run a single experiment
		 */
		public void run() {
			for(int i=0; i<events.length; i++)
				machine.processEvent(events[i]);
		}
	}
	
	/**
	 * Convert the supplied names into a list of events
	 * @param names names to convert into event objects
	 * @return one event object per event name
	 */
	private List<Event> makeEventsList(String ...names) {
		List<Event> events = new ArrayList<Event>();
		for(int i=0; i<names.length; i++) events.add(new Event(names[i]));
		return events;
	}

	/**
	 * Create the experiment objects according to the parameters
	 * @param loops how many times to loop each experiment
	 * @param nevents how many events to deliver to each state machine
	 * @param seed the random seed to use for generating the events sequence
	 * @return an experiment object for each of the state machines
	 */
	private List<Experiment> createExperiments(int nevents, int seed) {
		Event[] ecdplayer = generateEventsList(events_cdplayer, nevents, new Random(seed));
		Event[] ecookinghood = generateEventsList(events_cookinghood, nevents, new Random(seed));
		Event[] emicrowave = generateEventsList(events_microwaveoven, nevents, new Random(seed));
		List<Experiment> result = new ArrayList<Experiment>();
		insertExperiments(result, machines_cdplayer,ecdplayer);
		insertExperiments(result, machines_cookinghood,ecookinghood);
		insertExperiments(result, machines_microwaveoven,emicrowave);
		return result;
	}

	/**
	 * Insert experiments for each machine and event combination
	 * @param result list to insert experiments into
	 * @param machines the machines to generate experiments for
	 * @param events the events to include in each experiment
	 */
	private void insertExperiments(List<Experiment> result, List<IMachine> machines, Event[] events) {
		for(IMachine m: machines) result.add(new Experiment(m,events));
	}

	/**
	 * Generate a random list of events
	 * @param events the enumeration of events to generate from
	 * @param nevents how many events to generate
	 * @param random the random number generator to use
	 * @return list of random events
	 */
	private Event[] generateEventsList(List<Event> events, int nevents, Random random) {
		Event[] result = new Event[nevents];
		for(int i=0; i<nevents; i++)
			result[i] = events.get(random.nextInt(events.size()));
		return result;
	}

}
